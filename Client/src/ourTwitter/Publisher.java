package ourTwitter;

import java.util.Hashtable;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;



public class Publisher {

    private javax.jms.Connection connect = null;
    private javax.jms.Session sendSession = null;
    private javax.jms.MessageProducer sender = null;
    InitialContext context = null;

    public void configurer() throws JMSException {

        try {    // Create a connection
            // Si le producteur et le consommateur étaient codés séparément, ils auraient eu ce même bout de code

            Hashtable properties = new Hashtable();
            properties.put(Context.INITIAL_CONTEXT_FACTORY,
                    "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            properties.put(Context.PROVIDER_URL, "tcp://localhost:61616");

            context = new InitialContext(properties);

            javax.jms.ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
            connect = factory.createConnection();
            sendSession = connect.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
            connect.start(); // on peut activer la connection.
        } catch (javax.jms.JMSException jmse) {
            jmse.printStackTrace();
        } catch (NamingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public void tweet(String hashTag, String message) throws JMSException, NamingException {
        Topic topic = (Topic) context.lookup("dynamicTopics/"+hashTag);
        sender = sendSession.createProducer(topic);
        //TODO: on pourrait aussi conserver les objets sender sur chaque topic où on a déjà publié, pour pas avoir à la recréer
        //à chaque fois ^^
        MapMessage mess = sendSession.createMapMessage();
        mess.setString(hashTag, message);
        sender.send(mess); // equivaut à publier dans le topic
    }
}