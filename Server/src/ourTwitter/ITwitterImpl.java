package ourTwitter;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ITwitterImpl extends UnicastRemoteObject implements ITwitter {

	private HashMap<String, String> usersMap;
	private List<String> hashtagsList;

	protected ITwitterImpl() throws RemoteException {
		super();
		usersMap = new HashMap<>();
		hashtagsList = new ArrayList<>();
	}

	@Override
	public boolean connect(String pseudo, String pass) throws RemoteException {
		return usersMap.get(pseudo).equals(pass);
	}

	@Override
	public void tweet(String hashtag, String message) throws RemoteException {
		// TODO Auto-generated method stub
	}

	@Override
	public void newHashtag(String hashtag) throws RemoteException {
		// TODO Auto-generated method stub
	}

	@Override
	public void subscribe(String hashtag) throws RemoteException {
		// TODO Auto-generated method stub
	}

	@Override
	public List<String> receive(String hashtag) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> retrieveTopics() throws RemoteException {
		return hashtagsList;
	}

	@Override
	public void createAccount(String pseudo, String pass) throws RemoteException {
		usersMap.put(pseudo, pass);
	}

	public HashMap<String, String> getUsersMap() {
		return usersMap;
	}

	public void setUsersMap(HashMap<String, String> usersMap) {
		this.usersMap = usersMap;
	}

	public List<String> getHashtagsList() {
		return hashtagsList;
	}

	public void setHashtagsList(List<String> hashtagsList) {
		this.hashtagsList = hashtagsList;
	}
}
