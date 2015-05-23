package ourTwitter;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.advisory.DestinationSource;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;

import javax.jms.*;
import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXPrincipal;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

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


		Hashtable properties = new Hashtable();
		properties.put(Context.INITIAL_CONTEXT_FACTORY,
				"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
		properties.put(Context.PROVIDER_URL, "tcp://localhost:61616");

		// Create a Connection
		ActiveMQConnection connection = null;
		try {
			Context context = new InitialContext(properties);
			ActiveMQConnectionFactory factory = (ActiveMQConnectionFactory) context.lookup("ConnectionFactory");
			connection = (ActiveMQConnection)factory.createConnection();
			
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
	public void tweet(String hashtag, String message) throws RemoteException {
		// TODO Auto-generated method stub
	}

	@Override
	public void newHashtag(String hashtag) throws RemoteException {
		// Create the destination (Topic or Queue)
		try {
			Destination destination = session.createTopic(hashtag);
		} catch (JMSException e) {
			System.out.println("couldn't create the hashtag ! :(");
		}

		hashtagsList.add(hashtag);

	}

	@Override
	public void subscribe(String hashtag) throws RemoteException {
		// TODO Auto-generated method stub
	}

	@Override
	public List<String> receive(String hashtag) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
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
