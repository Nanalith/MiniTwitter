package ourTwitter;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

import javax.jms.JMSException;
import javax.naming.NamingException;

public class Client2 {
	public static void main(String[] args) throws RemoteException,
			MalformedURLException, UnknownHostException, NotBoundException,
			JMSException, NamingException {
		System.out.println("Bienvenue sur Twitter ! #Awesome");

		System.setProperty("java.security.policy",
				"file:" + System.getProperty("user.dir") + "/java.policy");
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager());
		}

		Client c2 = new Client();
		c2.config();
		c2.createAccount("Garance", "jaimeaussilesbrownies");
		c2.connect("Garance", "jaimeaussilesbrownies");
		c2.retrieveTopics();
		c2.subscribe("cookies");
		c2.publish("cookies", "oui mais les browkies c'est encore mieux");
		c2.disconnect();	
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		c2.connect("Garance", "jaimeaussilesbrownies");
		c2.disconnect();
	}

}
