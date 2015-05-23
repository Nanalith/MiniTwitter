package ourTwitter;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Topic;
import javax.naming.Context;
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

	public void disconnect() {
		try {
			receiveSession.close();
		} catch (JMSException e) {
			e.printStackTrace();
			System.out.println("couldn't disconnect from receiving");
		}
	}

	public Topic sabonner(String name, Context context, String clientId) throws JMSException, NamingException {
		Topic topic = (Topic) context.lookup("dynamicTopics/" + name);

		javax.jms.MessageConsumer topicReceiver = receiveSession
				.createDurableSubscriber(topic, clientId+topic);

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
