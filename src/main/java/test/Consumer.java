package test;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
 
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;
 
public class Consumer implements MessageListener {
 
    private static final Logger log = Logger.getLogger(Consumer.class);
 
    public static void main(String[] args) throws Exception {
 
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                "failover:(tcp://localhost:61616,tcp://localhost:61626)");
 
        // Create a Connection
        Connection connection = connectionFactory.createConnection();
        connection.start();
 
        // Create a Session
        Session session = connection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);
 
        // Create the destination (Topic or Queue)
        Destination destination = session.createQueue("TEST.FOO");
 
        // Create a MessageConsumer from the Session to the Queue
        MessageConsumer consumer = session.createConsumer(destination);
        consumer.setMessageListener(new Consumer()); // asynchronous listener
        Thread.sleep(12000000); // long wait to keep program running
        consumer.close();
        session.close();
        connection.close();
 
    }
 
    /**
     * asynchronous message listener
     */
    public void onMessage(Message message) {
        try {
            log.info(((TextMessage) message).getText());
            Thread.sleep(500);
        } catch (JMSException e) {
            log.error(e);
        } catch (InterruptedException e) {
            log.error(e);
        }
    }
 
}