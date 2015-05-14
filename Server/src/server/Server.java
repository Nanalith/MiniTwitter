package server;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class Server extends UnicastRemoteObject {
	private ITwitter twitter = new ITwitterImpl();
    private static final long serialVersionUID = 1L;
    private String url = "rmi://";

	protected Server() throws RemoteException, UnknownHostException {
		super();
		this.url += InetAddress.getLocalHost().getHostAddress() + "/twitter";
    }

	protected Server(String ip) throws RemoteException {
		super();
		this.url += ip + "/twitter";
    }
	
	public void launch() throws RemoteException, MalformedURLException {
		LocateRegistry.createRegistry(1099);
		Naming.rebind(this.url, this.twitter);
		System.out.println("Server launched at " + this.url + "...");
	}
	
	public static void main(String[] args) throws RemoteException, UnknownHostException, MalformedURLException {
		Server s = new Server();
		s.launch();
	}
}



