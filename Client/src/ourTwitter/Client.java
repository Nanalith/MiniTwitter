package ourTwitter;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Client {
	private ArrayList<String> topics = new ArrayList<String>();
	private Publisher myPub = new Publisher();
	private Subscriber mySub = new Subscriber();
	private ITwitter twitter;
	private String name;

    private javax.jms.Connection connect = null;
    InitialContext context = null;

	public void config() throws JMSException, MalformedURLException,
			RemoteException, UnknownHostException, NotBoundException {
		twitter = (ITwitter) Naming.lookup("rmi://"
				+ InetAddress.getLocalHost().getHostAddress() + "/twitter");
	}

	public void createAccount(String login, String pass) throws RemoteException {
		if (twitter.createAccount(login, pass)) {
			System.out.println("Creation of the account " + login + " OK");
		} else {
			System.out.println("Account already exists");
		}
	}

	public void connect(String login, String pass) throws RemoteException, JMSException, NamingException {
            connectToApacheMQ(twitter.connect(login, pass));
		this.name = login;
		//resubscribeToEveryThing(login);
		System.out.println(login + " is connected");
	}

	private void resubscribeToEveryThing(String login) throws RemoteException {
		for(String aTopic: topics)
			try {
				mySub.sabonner(aTopic, context, connect.getClientID()).getTopicName();
			} catch (JMSException e) {
				e.printStackTrace();
			} catch (NamingException e) {
				e.printStackTrace();
			}

		List<String> mandatoryTopics = twitter.getMandatoryTopics(login);
		for(String mandTops: topics)
			try {
				mySub.sabonner(mandTops, context, connect.getClientID()).getTopicName();
			} catch (JMSException e) {
				e.printStackTrace();
			} catch (NamingException e) {
				e.printStackTrace();
			}
	}

	private void connectToApacheMQ(String clientId) throws JMSException {
            try {
                // Create a connection
                Hashtable<String, String> properties = new Hashtable<String, String>();
                properties.put(Context.INITIAL_CONTEXT_FACTORY,
                        "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
                properties.put(Context.PROVIDER_URL, "tcp://localhost:61616");

                context = new InitialContext(properties);

                javax.jms.ConnectionFactory factory = (ConnectionFactory) context
                        .lookup("ConnectionFactory");
                connect = factory.createConnection();
                connect.setClientID(clientId);
                connect.start();

                myPub.configurer(connect);
                mySub.configurer(connect);

            } catch (javax.jms.JMSException jmse) {
                jmse.printStackTrace();
            } catch (NamingException e) {
                e.printStackTrace();
            }
    }

	public void retrieveTopics() {	
		List<String> sujets = new ArrayList<String>();

		try {
			sujets = twitter.retrieveTopics();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		if(sujets.size() > 0) {
			System.out.println("List of the topics :");
			for(String topic : sujets) {
				System.out.println("\t-" + topic);
			} 
		} else {
			System.out.println("No topics");
		}
	}
	
	public void newTag(String tag) throws RemoteException {
		if(twitter.newHashtag(tag)) {
			System.out.println(this.name + " created the topic " + tag);
		} else {
			System.out.println(this.name + " can't create the topic " + tag + ", it already exists");
		}	
	}

	public void subscribe(String tag) throws JMSException, NamingException {
		addTopics(mySub.sabonner(tag, context, connect.getClientID()).getTopicName());
		System.out.println(this.name + " is abonned to " + tag);
	}

	public void publish(String tag, String message) throws JMSException {
		try {
			myPub.tweet(tag, message, context);
			System.out.println(this.name + " wrote on the tag \"" + tag + "\" the message : " + message);
		} catch (NamingException e) {
			e.printStackTrace();
			System.out.println("Couldn't write to the topic :(");
		}
	}

	public ArrayList<String> getTopics() {
		return topics;
	}

	public void addTopics(String t) {
		topics.add(t);
	}
	
	public Connection getConnect() {
		return connect;
	}
	
	public void disconnect() {
		try {
			this.connect.close();
			mySub.disconnect();
			myPub.disconnect();
			System.out.println(this.name + " is disconnected");
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
