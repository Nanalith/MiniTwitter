package ourTwitter;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.jms.JMSException;
import javax.jms.Topic;
import javax.naming.NamingException;

public class Client {
	private ArrayList<Topic> topics = new ArrayList<Topic>();
	
    public ArrayList<Topic> getTopics() {
		return topics;
	}
    
    public void addTopics(Topic t) {
		topics.add(t);
	}

	public static void main(String[] args) throws RemoteException, MalformedURLException, UnknownHostException, NotBoundException, JMSException, NamingException {
        Client c = new Client();
    	
    	System.out.println("Bonjour, je me connecte � Twitter ! #Awesome");
        
        System.setProperty("java.security.policy", "file:" + System.getProperty("user.dir") + "/java.policy");

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
        
		ITwitter twitter = (ITwitter) Naming.lookup("rmi://" + InetAddress.getLocalHost().getHostAddress() + "/twitter");
        if(twitter.createAccount("Nana", "jaimelescookies"))
        	System.out.println("Création du compte Nana OK");
        else {
        	System.out.println("Compte déjà existant");
        }
        
        System.out.println("j'essaie de me connecter, et la réponse est : " + twitter.connect("Nana","jaimelescookies"));

        Publisher myPub = new Publisher();
        myPub.configurer();
        
        Subscriber mySub = new Subscriber();
        mySub.configurer(); 
        
        // Au début on est abonné à rien 
        System.out.println("My topics:" + c.getTopics()); // vide
 
        // Création du tag cookie
        System.out.println("Création d'un topic \"cookies\"");
        twitter.newHashtag("cookies");
        
        // Abonnement au tag cookies
        c.addTopics(mySub.souscripteur("cookies"));
        System.out.println("My topics:" + c.getTopics());
        
        // Publier sur le tag cookie
        try {
            System.out.println("Ecriture sur le hashtag cookies");
            myPub.tweet("cookies", "les cookies c'est chouette ");
        } catch (NamingException e) {
            e.printStackTrace();
            System.out.println("couldn't write to the topic :(");
        }
        
    }
}

