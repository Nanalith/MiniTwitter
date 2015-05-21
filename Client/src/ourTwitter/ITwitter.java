package ourTwitter;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

interface ITwitter extends Remote {
	boolean connect(String pseudo, String pass) throws RemoteException;
	void tweet(String hashtag, String message) throws RemoteException;
	void newHashtag(String hashtag) throws RemoteException;
	void subscribe(String hashtag) throws RemoteException;
	List<String> receive(String hashtag) throws RemoteException;
	List<String> retrieveTopics() throws RemoteException;
}
