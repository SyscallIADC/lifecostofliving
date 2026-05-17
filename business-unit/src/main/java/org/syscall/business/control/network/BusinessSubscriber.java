package org.syscall.business.control.network;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.syscall.business.control.datamart.DatamartRepository;
import org.syscall.business.control.datamart.ParsedEvent;

import javax.jms.*;
import java.util.List;

public class BusinessSubscriber implements Subscriber{
    private final String brokerUrl = "tcp://localhost:61616";
    private final DatamartRepository datamart;
    private Connection connection;
    private Session session;

    public BusinessSubscriber(DatamartRepository datamart) {
        this.datamart = datamart;
    }
    @Override
    public void start(List<String> topicNames) {
        try {
            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
            connection = factory.createConnection();
            connection.setClientID("BusinessUnit_Datamart");
            connection.start();

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            for (String topicName : topicNames) {
                Topic topic = session.createTopic(topicName);
                String subName = "BusinessSub_" + topicName;
                MessageConsumer consumer = session.createDurableSubscriber(topic, subName);

                consumer.setMessageListener(message -> {
                    if (message instanceof TextMessage) {
                        try {
                            String json = ((TextMessage) message).getText();
                            ParsedEvent event = new ParsedEvent(topicName, "live-stream", "", json);

                            if (topicName.equalsIgnoreCase("LivingCost")) {
                                datamart.upsertCountryData(event);
                            } else if (topicName.equalsIgnoreCase("ExchangeRate")) {
                                datamart.upsertExchangeRateData(event);
                            }

                        } catch (JMSException e) {
                            System.err.println("Error procesando mensaje en tiempo real: " + e.getMessage());
                        }
                    }
                });
                System.out.println("Business Unit suscrita a: " + topicName);
            }
        } catch (JMSException e) {
            System.err.println("Error de conexión con ActiveMQ: " + e.getMessage());
        }
    }
    @Override
    public void stop() {
        try {
            if (session != null) session.close();
            if (connection != null) connection.close();
        } catch (JMSException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}