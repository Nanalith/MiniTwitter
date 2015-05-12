package server;
import java.util.*;

interface ITwitter {
	void connect(String pseudo, String pass);
	void tweet(String hashtag, String message);
	void newHashtag(String hashtag);
	void subscribe(String hashtag);
	List<String> receive(String hashtag);
	List<String> retrieveTopics();
}
