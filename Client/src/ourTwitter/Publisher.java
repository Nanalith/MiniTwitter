package ourTwitter;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.NamingException;

public class Publisher {
    private javax.jms.Session sendSession = null;
    private javax.jms.MessageProducer sender = null;
    private static final long   MESSAGE_LIFESPAN = 86400000; //30 minutes


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
        sender.send(mess, javax.jms.DeliveryMode.PERSISTENT, //publish persistently
                javax.jms.Message.DEFAULT_PRIORITY,//priority
                MESSAGE_LIFESPAN); // equivaut � publier dans le topic


    }
}