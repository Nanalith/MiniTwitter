package ourTwitter;

import java.util.Hashtable;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Publisher {
    private javax.jms.Session sendSession = null;
    private javax.jms.MessageProducer sender = null;

    public void configurer(Connection connect) throws JMSException {
        try {  
          sendSession = connect.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);

            connect.start(); 

        } catch (javax.jms.JMSException jmse) {
            jmse.printStackTrace();
        }
    }
   
    public void tweet(String hashTag, String message, Context context) throws JMSException, NamingException {
        Topic topic = (Topic) context.lookup("dynamicTopics/"+hashTag);
        sender = sendSession.createProducer(topic);
        MapMessage mess = sendSession.createMapMessage();
        mess.setString("content", message);
        sender.send(mess); // equivaut � publier dans le topic
    }
}