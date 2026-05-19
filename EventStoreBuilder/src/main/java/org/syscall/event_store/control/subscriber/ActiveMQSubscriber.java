package org.syscall.event_store.control.subscriber;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class ActiveMQSubscriber implements Subscriber{
    private final String brokerUrl = "tcp://localhost:61616";
    private Connection connection;
    private Session session;
    private final List<MessageConsumer> consumers = new ArrayList<>();
    private final BlockingQueue<RawEvent> queue;

    public ActiveMQSubscriber(BlockingQueue<RawEvent> queue) {
        this.queue = queue;
    }

    @Override
    public void start(List<String> topics) {
        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
            connection = connectionFactory.createConnection();

            connection.setClientID("EventStoreBuilder");
            connection.start();

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            for (String topicName : topics) {
                Topic topic = session.createTopic(topicName);
                String subscriptionName = "Durable_" + topicName;
                MessageConsumer consumer = session.createDurableSubscriber(topic, subscriptionName);
                consumers.add(consumer);

                consumer.setMessageListener(message -> {
                    if (message instanceof TextMessage) {
                        try {
                            String json = ((TextMessage) message).getText();
                            queue.put(new RawEvent(topicName, json));
                        } catch (Exception e) {
                            System.out.println("Error al procesar el evento " + e.getMessage());
                        }
                    }
                });

                System.out.println("Se ha suscrito a " + topicName);
            }

        } catch (JMSException e) {
            System.out.println("Error al crear la conexión " + e.getMessage());
        }
    }

    @Override
    public void stop() {
        try {
            for (MessageConsumer consumer : consumers) {
                if (consumer != null) consumer.close();
            }

            if (session != null) {
                session.close();
            }

            if (connection != null) {
                connection.close();
                System.out.println("Conexión con ActiveMQ cerrada correctamente.");
            }
        } catch (JMSException e) {
            System.err.println("Error al cerrar los recursos de ActiveMQ: " + e.getMessage());
        }
    }

}
