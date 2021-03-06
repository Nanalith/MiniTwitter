package ourTwitter;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

interface ITwitter extends Remote {
	String connect(String pseudo, String pass) throws RemoteException;

	boolean newHashtag(String hashtag) throws RemoteException;

	boolean createAccount(String pseudo, String pass) throws RemoteException;

	List<String> retrieveTopics() throws RemoteException;

	List<String> getMandatoryTopics(String pseudo) throws RemoteException;
}
