package ourTwitter;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Topic;
import javax.naming.NamingException;

public class Client {
	private ArrayList<Topic> topics = new ArrayList<Topic>();
	private Publisher myPub = new Publisher();
	private Subscriber mySub = new Subscriber();
	private ITwitter twitter;
	private String name;

	public void config() throws JMSException, MalformedURLException,
			RemoteException, UnknownHostException, NotBoundException {
		twitter = (ITwitter) Naming.lookup("rmi://"
				+ InetAddress.getLocalHost().getHostAddress() + "/twitter");
		myPub.configurer();
		mySub.configurer();
	}

	public void createAccount(String login, String pass) throws RemoteException {
		if (twitter.createAccount(login, pass)) {
			System.out.println("Creation of the account " + login + " OK");
			this.name = login;
		} else {
			System.out.println("Account already exists");
		}
	}

	public void connect(String login, String pass) throws RemoteException {
		if (twitter.connect(login, pass)) {
			System.out.println(login + " is connected");
		} else {
			System.out.println("Connection failed : login or pass invalid");
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
		addTopics(mySub.sabonner(tag));
		System.out.println(this.name + " is abonned to " + tag);
	}

	public void publish(String tag, String message) throws JMSException {
		try {
			myPub.tweet(tag, message);
			System.out.println(this.name + " wrote on the tag " + tag + "the message " + message);
		} catch (NamingException e) {
			e.printStackTrace();
			System.out.println("Couldn't write to the topic :(");
		}
	}

	public ArrayList<Topic> getTopics() {
		return topics;
	}

	public void addTopics(Topic t) {
		topics.add(t);
	}
	
	public static void main(String[] args) throws RemoteException,
			MalformedURLException, UnknownHostException, NotBoundException,
			JMSException, NamingException {
		System.out.println("Bienvenue sur Twitter ! #Awesome");

		System.setProperty("java.security.policy",
				"file:" + System.getProperty("user.dir") + "/java.policy");
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager());
		}

		// Création et configuration du client
		Client c = new Client();
		c.config();
		c.createAccount("Nana", "jaimelescookies");
		c.connect("Nana", "jaimelescookies"); // TODO securiser !
		c.retrieveTopics();
		c.newTag("cookies");
		c.subscribe("cookies");
		c.publish("cookies", "les cookies c'est chouette");

		System.out.println("\n--------\n");
		
		// Deuxieme client
		Client c2 = new Client();
		c2.config();
		c2.createAccount("Garance", "jaimeaussilesbrownies");
		c2.connect("Garance", "jaimeaussilesbrownies");
		c2.retrieveTopics();
		c2.subscribe("cookies");
		c2.publish("cookies", "oui mais les browkies c'est encore mieux");

	}
}
