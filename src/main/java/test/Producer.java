package test;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;
 
 
public class Producer {
 
    private static final Logger log = Logger.getLogger(Producer.class);
     
    public static void main(String[] args) throws Exception {
        // Create a ConnectionFactory
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                "failover:(tcp://localhost:61616,tcp://localhost:61626)");
 
        for (int i = 0; i < 10000; i++) {
             
            log.info("Establishing connection");
            // Create a Connection
            Connection connection = connectionFactory.createConnection();
            connection.start();
             
            log.info("Connection established");
             
            // Create a Session
            Session session = connection.createSession(false,
                    Session.AUTO_ACKNOWLEDGE);
 
            // Create the destination (Topic or Queue)
            Destination destination = session.createQueue("TEST.FOO");
 
            // Create a MessageProducer from the Session to the Topic or Queue
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
 
            // Create a messages
            String text = "Message Counter : " + i;
            TextMessage message = session.createTextMessage(text);
 
            log.info("Sending message : " + text);
            producer.send(message);
            log.info("Sent message : " + text);
            // Clean up
            session.close();
            connection.close();
 
            Thread.sleep(1000);
        }
    }
}