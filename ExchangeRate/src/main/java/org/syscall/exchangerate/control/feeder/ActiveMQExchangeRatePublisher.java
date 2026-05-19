package org.syscall.exchangerate.control.feeder;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.syscall.exchangerate.models.ExchangeRate;
import java.time.Instant;
import javax.jms.*;

public class ActiveMQExchangeRatePublisher {

    private static final String BROKER_URL = "failover:(tcp://localhost:61616)?randomize=false";
    private static final String TOPIC_NAME = "ExchangeRate";
    private static final String SOURCE_ID = "ExchangeRate-feeder";

    private Connection connection;
    private Session session;
    private MessageProducer producer;

    public void start() throws JMSException {
        ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
        connection = factory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic(TOPIC_NAME);
        producer = session.createProducer(topic);
        System.out.println("Publisher conectado a ActiveMQ.");
    }

    public void publish(ExchangeRate rate) throws JMSException {
        JsonObject data = new JsonObject();
        data.addProperty("fromCurrency", rate.getFromCurrency());
        data.addProperty("toCurrency", rate.getToCurrency());
        data.addProperty("exchangeRate", rate.getExchangeRate());
        data.addProperty("lastRefreshed", rate.getLastRefreshed());
        data.addProperty("timeZone", rate.getTimeZone());

        JsonObject event = new JsonObject();
        event.addProperty("ts", Instant.now().toString());
        event.addProperty("ss", SOURCE_ID);
        event.add("data", data);

        String jsonStr = new Gson().toJson(event);
        TextMessage message = session.createTextMessage(jsonStr);
        producer.send(message);
        System.out.println("Evento publicado: " + jsonStr);
    }

    public void stop() throws JMSException {
        if (producer != null) producer.close();
        if (session != null) session.close();
        if (connection != null) connection.close();
        System.out.println("Publisher desconectado.");
    }
}