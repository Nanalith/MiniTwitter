package ourTwitter;


import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

public class Client {

    public static void main(String[] args) throws RemoteException, MalformedURLException, UnknownHostException, NotBoundException {
        System.out.println("Bonjour, je me connecte � Twitter ! #Awesome");
        
        System.setProperty("java.security.policy","file:"+System.getProperty("user.dir")+"\\java.policy");

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
        
		ITwitter twitter = (ITwitter) Naming.lookup("rmi://" + InetAddress.getLocalHost().getHostAddress() + "/twitter");
        System.out.println("j'essaie de me connecter, et la réponse est : "+twitter.connect("Nana","jaimelescookies"));
    		
	}
}

