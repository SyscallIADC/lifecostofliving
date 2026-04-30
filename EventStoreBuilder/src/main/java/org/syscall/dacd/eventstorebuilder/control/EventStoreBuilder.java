package org.syscall.dacd.eventstorebuilder.control;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventStoreBuilder implements MessageListener {

    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String TOPIC_NAME = "ExchangeRate";
    private static final String CLIENT_ID = "EventStoreBuilder";
    private static final String SUBSCRIPTION_NAME = "EventStoreSubscription";
    private static final String EVENTSTORE_PATH = "eventstore";

    private Connection connection;
    private Session session;

    public void start() throws JMSException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
        connection = factory.createConnection();
        connection.setClientID(CLIENT_ID);
        connection.start();

        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic(TOPIC_NAME);

        TopicSubscriber subscriber = session.createDurableSubscriber(topic, SUBSCRIPTION_NAME);
        subscriber.setMessageListener(this);

        System.out.println("EventStoreBuilder suscrito al topic: " + TOPIC_NAME);
    }

    public void stop() throws JMSException {
        if (session != null) session.close();
        if (connection != null) connection.close();
        System.out.println("EventStoreBuilder desconectado.");
    }

    @Override
    public void onMessage(Message message) {
        if (!(message instanceof TextMessage textMessage)) return;
        try {
            String json = textMessage.getText();
            JsonObject event = JsonParser.parseString(json).getAsJsonObject();

            String ts = event.get("ts").getAsString();
            String ss = event.get("ss").getAsString();
            String date = extractDate(ts);

            Path filePath = Path.of(EVENTSTORE_PATH, TOPIC_NAME, ss, date + ".events");
            Files.createDirectories(filePath.getParent());

            try (BufferedWriter writer = Files.newBufferedWriter(filePath,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                writer.write(json);
                writer.newLine();
            }

            System.out.println("Evento guardado en: " + filePath);

        } catch (Exception e) {
            System.out.println("Error procesando mensaje: " + e.getMessage());
        }
    }

    private String extractDate(String ts) {
        LocalDateTime dateTime = LocalDateTime.parse(ts,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return dateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
}
