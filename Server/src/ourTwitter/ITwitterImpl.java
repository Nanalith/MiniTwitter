package ourTwitter;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.advisory.DestinationSource;
import org.apache.activemq.command.ActiveMQTopic;


public class ITwitterImpl extends UnicastRemoteObject implements ITwitter {
	private static final long serialVersionUID = 1L;
	private HashMap<String, AbstractMap.SimpleEntry<String, String>> usersMap;
	private List<String> hashtagsList;

	private javax.jms.Session sendSession = null;
	private javax.jms.MessageProducer sender = null;
	private Context context;
	private static final long   MESSAGE_LIFESPAN = 86400000; //24h

	private Session session;

	protected ITwitterImpl() throws RemoteException {
		super();

		usersMap = new HashMap<>();
		hashtagsList = new ArrayList<>();
		connectToBroker();
	}

	private void connectToBroker() {
		Hashtable<String, String> properties = new Hashtable<String, String>();
		properties.put(Context.INITIAL_CONTEXT_FACTORY,
				"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
		properties.put(Context.PROVIDER_URL, "tcp://localhost:61616");

		// Create a Connection
		ActiveMQConnection connection = null;
		try {
			context = new InitialContext(properties);
			ActiveMQConnectionFactory factory = (ActiveMQConnectionFactory) context.lookup("ConnectionFactory");
			connection = (ActiveMQConnection)factory.createConnection();
			//connection.getClientID()
			connection.start();
			DestinationSource ds = connection.getDestinationSource();
			Set<ActiveMQTopic> topics = ds.getTopics();
			for (ActiveMQTopic t : topics) {
				hashtagsList.add(t.getTopicName());
			}
			// Create a Session
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			newHashtag("HashtagList");
			declareWriter(connection);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("couldn't connect to Apache");
		}
	}

	private void declareWriter(Connection connect) {
		try {
			sendSession = connect.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
		} catch (javax.jms.JMSException jmse) {
			jmse.printStackTrace();
		}
	}


	@Override
	public String connect(String pseudo, String pass) throws RemoteException {
		if (usersMap.get(pseudo).getKey().equals(pass))
		return usersMap.get(pseudo).getValue();
		return null;
	}

	@Override
	public boolean newHashtag(String hashtag) throws RemoteException {
		if(this.hashtagsList.contains(hashtag))
			return false;
		
		// Create the destination (topic)
		try {
			session.createTopic(hashtag);
			publishNewHashtag(hashtag);
		} catch (JMSException e) {
			System.out.println("couldn't create the hashtag ! :(");
		}
		hashtagsList.add(hashtag);
		
		return true;
	}

	private void publishNewHashtag(String hashtag) {
		Topic topic = null;
		try {
			topic = (Topic) context.lookup("dynamicTopics/hashtagList");
			sender = sendSession.createProducer(topic);
			MapMessage mess = sendSession.createMapMessage();
			mess.setString("content", "A new Hashtag was created ! \""+hashtag+"\"");
			sender.send(mess, javax.jms.DeliveryMode.PERSISTENT, //publish persistently
					9,//maximum priority
					MESSAGE_LIFESPAN); // equivaut à publier dans le topic
		} catch (NullPointerException e) {
			//problème uniquement dans le cas où on crée le tout premier topic hashtagList, alors qu'on n'est pas
			//encore écrivain dessus : on essaie d'écrire que ce topic a été créé, impossible !
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<String> retrieveTopics() throws RemoteException {
		return hashtagsList;
	}

	@Override
	public boolean createAccount(String pseudo, String pass) throws RemoteException {
		if(usersMap.get(pseudo) != null)
			return false;
		usersMap.put(pseudo, new AbstractMap.SimpleEntry<String, String>(pass, ""+usersMap.size()));
		return true;
	}

	public HashMap<String, AbstractMap.SimpleEntry<String, String>> getUsersMap() {
		return usersMap;
	}

}
