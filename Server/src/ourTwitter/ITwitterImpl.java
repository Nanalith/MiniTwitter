package ourTwitter;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ITwitterImpl extends UnicastRemoteObject implements ITwitter {

	protected ITwitterImpl() throws RemoteException {
		super();
	}

	@Override
	public boolean connect(String pseudo, String pass) throws RemoteException {
		// TODO Auto-generated method stub
		return true;
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
		// TODO Auto-generated method stub
		return null;
	}

}
