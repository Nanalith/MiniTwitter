package ourTwitter;

import java.util.Hashtable;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Publisher {
    private javax.jms.Connection connect = null;
    private javax.jms.Session sendSession = null;
    private javax.jms.MessageProducer sender = null;
    InitialContext context = null;

    public void configurer() throws JMSException {
        try {  
            Hashtable<String, String> properties = new Hashtable<String, String>();
            properties.put(Context.INITIAL_CONTEXT_FACTORY,
                    "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            properties.put(Context.PROVIDER_URL, "tcp://localhost:61616");

            context = new InitialContext(properties);

            javax.jms.ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
            connect = factory.createConnection();

            sendSession = connect.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);

            connect.start(); 

        } catch (javax.jms.JMSException jmse) {
            jmse.printStackTrace();
        } catch (NamingException e) {
        	e.printStackTrace();
        }
    }
   
    public void tweet(String hashTag, String message) throws JMSException, NamingException {
        Topic topic = (Topic) context.lookup("dynamicTopics/"+hashTag);
        sender = sendSession.createProducer(topic);
        //TODO: on pourrait aussi conserver les objets sender sur chaque topic o� on a d�j� publi�, pour pas avoir � la recr�er
        //� chaque fois ^^
        MapMessage mess = sendSession.createMapMessage();
        mess.setString("content", message);
        sender.send(mess); // equivaut � publier dans le topic
    }
}