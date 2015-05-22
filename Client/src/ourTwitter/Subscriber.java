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


public class Subscriber implements javax.jms.MessageListener{

    private javax.jms.Connection connect = null;
    private javax.jms.Session receiveSession = null;
    InitialContext context = null;
    public void configurer() throws JMSException {

        try
        {	// Create a connection
            Hashtable properties = new Hashtable();
            properties.put(Context.INITIAL_CONTEXT_FACTORY,
                    "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            properties.put(Context.PROVIDER_URL, "tcp://localhost:61616");

            context = new InitialContext(properties);

            javax.jms.ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
            connect = factory.createConnection();

            this.configurerSouscripteur();
            connect.start(); // on peut activer la connection.
        } catch (javax.jms.JMSException jmse){
            jmse.printStackTrace();
        } catch (NamingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private void configurerSouscripteur() throws JMSException, NamingException{
        // Pour consommer, il faudra simplement ouvrir une session
        receiveSession = connect.createSession(false,javax.jms.Session.AUTO_ACKNOWLEDGE);
        // et dire dans cette session quelle queue(s) et topic(s) on accèdera et dans quel mode
        Topic topic = (Topic) context.lookup("dynamicTopics/topicExo2");
        System.out.println("Nom du topic " + topic.getTopicName());
        javax.jms.MessageConsumer topicReceiver = receiveSession.createConsumer(topic);//,"Conso");//,"typeMess = 'important'");
        //topicReceiver.setMessageListener(this);
        //ESSAI d'une reception synchrone
        connect.start(); // on peut activer la connection.
        while (true){
            Message m= topicReceiver.receive();
            System.out.print("recept synch: "); onMessage(m);
        }
    }


    @Override
    public void onMessage(Message message) {
        // Methode permettant au souscripteur de consommer effectivement chaque msg recu
        // via le topic auquel il a souscrit
        try {
            System.out.print("Recu un message du topic: "+((MapMessage)message).getString("nom"));
            System.out.println(((MapMessage)message).getString("num"));
        } catch (JMSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
