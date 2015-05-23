package ourTwitter;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.advisory.DestinationSource;
import org.apache.activemq.command.ActiveMQTopic;

public class ITwitterImpl extends UnicastRemoteObject implements ITwitter {
	private static final long serialVersionUID = 1L;
	private HashMap<String, String> usersMap;
	private List<String> hashtagsList;

	private Session session;

	protected ITwitterImpl() throws RemoteException {
		super();
		usersMap = new HashMap<>();
		hashtagsList = new ArrayList<>();
		connectToBroker();
	}

	private void connectToBroker() {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");

		// Create a Connection
		ActiveMQConnection connection = null;
		try {
			connection = (ActiveMQConnection)connectionFactory.createConnection();

			connection.start();
			DestinationSource ds = connection.getDestinationSource();
			Set<ActiveMQTopic> topics = ds.getTopics();
			for (ActiveMQTopic t : topics) {
				hashtagsList.add(t.getTopicName());
			}
			// Create a Session
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("couldn't connect to Apache");
		}
	}

	@Override
	public boolean connect(String pseudo, String pass) throws RemoteException {
		return usersMap.get(pseudo).equals(pass);
	}

	@Override
	public void newHashtag(String hashtag) throws RemoteException {
		// Create the destination (topic)
		try {
			session.createTopic(hashtag);
		} catch (JMSException e) {
			System.out.println("couldn't create the hashtag ! :(");
		}
		hashtagsList.add(hashtag);
	}

	@Override
	public List<String> retrieveTopics() throws RemoteException {
		return hashtagsList;
	}

	@Override
	public boolean createAccount(String pseudo, String pass) throws RemoteException {
		if(usersMap.get(pseudo) != null)
			return false;
				
		usersMap.put(pseudo, pass);
		return true;
	}

	public HashMap<String, String> getUsersMap() {
		return usersMap;
	}

	public void setUsersMap(HashMap<String, String> usersMap) {
		this.usersMap = usersMap;
	}

	public List<String> getHashtagsList() {
		return hashtagsList;
	}

	public void setHashtagsList(List<String> hashtagsList) {
		this.hashtagsList = hashtagsList;
	}
}
