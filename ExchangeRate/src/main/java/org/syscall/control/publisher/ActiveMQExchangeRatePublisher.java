package org.syscall.control.publisher;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class ActiveMQExchangeRatePublisher {

    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String TOPIC_NAME = "ExchangeRate";

    private javax.jms.Connection connection;
    private Session session;
    public MessageProducer producer;

    public void start() throws JMSException {
        ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
        connection = factory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic(TOPIC_NAME);
        producer = session.createProducer(topic);
        System.out.println("Publisher conectado a ActiveMQ.");
    }

    public void send(String jsonStr) throws JMSException {
        TextMessage message = session.createTextMessage(jsonStr);
        producer.send(message);
        System.out.println("Evento publicado: " + jsonStr);
    }

    public Session getSession() {
        return session;
    }

    public void stop() throws JMSException {
        if (producer != null) producer.close();
        if (session != null) session.close();
        if (connection != null) connection.close();
        System.out.println("Publisher desconectado.");
    }
}