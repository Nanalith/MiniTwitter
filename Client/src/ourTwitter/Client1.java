package ourTwitter;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

import javax.jms.JMSException;
import javax.naming.NamingException;

public class Client1 {
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

		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		c.publish("cookies", "j'aurai le dernier mot !");
		c.disconnect();
	}
}
