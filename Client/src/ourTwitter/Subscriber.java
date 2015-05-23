package ourTwitter;

import java.util.Hashtable;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Subscriber implements javax.jms.MessageListener {
    private javax.jms.Session receiveSession = null;

    public void configurer(Connection connect) throws JMSException {
        try {
            receiveSession = connect.createSession(false,
                    javax.jms.Session.AUTO_ACKNOWLEDGE);
        } catch (javax.jms.JMSException jmse) {
            jmse.printStackTrace();
        }
    }

	public Topic sabonner(String name, Context context) throws JMSException, NamingException {
		Topic topic = (Topic) context.lookup("dynamicTopics/" + name);

		System.out.println("Topic name " + topic.getTopicName());
		javax.jms.MessageConsumer topicReceiver = receiveSession
				.createConsumer(topic);

		topicReceiver.setMessageListener(this);

        return topic;
    }

    @Override
    public void onMessage(Message message) {
        try {
            System.out.print("Message received from "
					+ message.getJMSDestination() + " : "
					+ ((MapMessage) message).getString("content") + "\n");
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
