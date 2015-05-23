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
   
    public void configurer() throws JMSException {
        try {	
        	// Create a connection
            Hashtable<String, String> properties = new Hashtable<String, String>();
            properties.put(Context.INITIAL_CONTEXT_FACTORY,
                    "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            properties.put(Context.PROVIDER_URL, "tcp://localhost:61616");

            context = new InitialContext(properties);

            javax.jms.ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
            connect = factory.createConnection();

            receiveSession = connect.createSession(false,javax.jms.Session.AUTO_ACKNOWLEDGE);
            connect.start();

        } catch (javax.jms.JMSException jmse){
            jmse.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }  
    
    public Topic sabonner(String name) throws JMSException, NamingException{
        Topic topic = (Topic) context.lookup("dynamicTopics/" + name);
          
        System.out.println("Topic name " + topic.getTopicName());
        javax.jms.MessageConsumer topicReceiver = receiveSession.createConsumer(topic);
        
        topicReceiver.setMessageListener(this);
        
        return topic;        
    }

    @Override
    public void onMessage(Message message) {
        try {
            System.out.print("Message received from " + message.getJMSDestination() + " : ");
            System.out.println(((MapMessage)message).getString("content"));
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
