package ourTwitter;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

interface ITwitter extends Remote {
	String connect(String pseudo, String pass) throws RemoteException;

	boolean newHashtag(String hashtag) throws RemoteException;

	boolean createAccount(String pseudo, String pass) throws RemoteException;

	List<String> retrieveTopics() throws RemoteException;
}
