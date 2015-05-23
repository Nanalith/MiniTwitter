package ourTwitter;

import java.util.Hashtable;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public class Subscriber implements javax.jms.MessageListener {
    private javax.jms.Connection connect = null;
    private javax.jms.Session receiveSession = null;
    InitialContext context = null;
   
    public Topic configurer() throws JMSException {
        try {	
        	// Create a connection
            Hashtable properties = new Hashtable();
            properties.put(Context.INITIAL_CONTEXT_FACTORY,
                    "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            properties.put(Context.PROVIDER_URL, "tcp://localhost:61616");

            context = new InitialContext(properties);

            javax.jms.ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
            connect = factory.createConnection();

            Topic t = this.configurerSouscripteur();
            connect.start();
            return t;
        } catch (javax.jms.JMSException jmse){
            jmse.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }
		return null;      
    }
    
    private Topic configurerSouscripteur() throws JMSException, NamingException{
    	// Open a session
        receiveSession = connect.createSession(false,javax.jms.Session.AUTO_ACKNOWLEDGE);
        
        Topic topic = (Topic) context.lookup("dynamicTopics/topicExo2");
          
        System.out.println("Topic name " + topic.getTopicName());
        javax.jms.MessageConsumer topicReceiver = receiveSession.createConsumer(topic);
        
        topicReceiver.setMessageListener(this);
        
        return topic;
          
//        //ESSAI d'une reception synchrone
//        connect.start(); // on peut activer la connection.
//        while (true){
//            Message m= topicReceiver.receive();
//            System.out.print("recept synch: "); onMessage(m);
//        }
    }

    @Override
    public void onMessage(Message message) {
        try {
            System.out.print("Receive from topic : " + ((MapMessage)message).getString("nom"));
            System.out.println(((MapMessage)message).getString("num"));
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
