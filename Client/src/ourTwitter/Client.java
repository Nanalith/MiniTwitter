package ourTwitter;


import javax.jms.JMSException;
import javax.jms.Topic;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Client {
	private ArrayList<Topic> topics = new ArrayList<Topic>();
	
    public ArrayList<Topic> getTopics() {
		return topics;
	}
    
    public void addTopics(Topic t) {
		topics.add(t);
	}

	public static void main(String[] args) throws RemoteException, MalformedURLException, UnknownHostException, NotBoundException, JMSException {
        Client c = new Client();
    	
    	System.out.println("Bonjour, je me connecte � Twitter ! #Awesome");
        
        System.setProperty("java.security.policy","file:"+System.getProperty("user.dir")+"/java.policy");

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
        
		ITwitter twitter = (ITwitter) Naming.lookup("rmi://" + InetAddress.getLocalHost().getHostAddress() + "/twitter");
        if(twitter.createAccount("Nana", "jaimelescookies"))
        	System.out.println("Création du compte Nana OK");
        else {
        	System.out.println("Compte déjà existant");
        }
        System.out.println("j'essaie de me connecter, et la réponse est : "+twitter.connect("Nana","jaimelescookies"));
    

        Publisher myPub = new Publisher();
        myPub.configurer();
        
        Subscriber mySub = new Subscriber();
        c.addTopics(mySub.configurer());
        System.out.println("My topics:" + c.getTopics());
              
    }
}

