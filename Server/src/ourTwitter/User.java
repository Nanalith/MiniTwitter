package ourTwitter;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Garance on 23/05/2015.
 */
public class User {

    private String password;
    private String userId;
    private List<String> topicsList;

    public User(String password, String userId) {
        this.password = password;
        this.userId = userId;
        topicsList = new ArrayList<>();
        topicsList.add("hashtagList");
    }

    public String getPassword() {
        return password;
    }

    public String getUserId() {
        return userId;
    }

    public List<String> getTopicsList() {
        return topicsList;
    }
}
