package ourTwitter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class Server extends UnicastRemoteObject {
	private ITwitter twitter = new ITwitterImpl();
    private static final long serialVersionUID = 1L;
    private String url;

	protected Server() throws RemoteException, UnknownHostException {
		super();
		this.url = "rmi://" + InetAddress.getLocalHost().getHostAddress() + "/twitter";
    }

	protected Server(String ip) throws RemoteException {
		super();
		this.url += ip + "/twitter";
    }
	
	public void launch(int port) {
		try {
			LocateRegistry.createRegistry(port);
			Naming.rebind(this.url, this.twitter);
			System.out.println("Server launched at " + this.url + "...");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {
	    // Pour le chargement dynamique des class
		System.setProperty("java.security.policy","file:./java.policy");
		System.setProperty("java.rmi.ourTwitter.codebase", "file:./bin");
		if (System.getSecurityManager() == null) {
	      System.setSecurityManager(new RMISecurityManager());
	    }

		Server s = new Server();
		s.launch(1099);
	}
}



