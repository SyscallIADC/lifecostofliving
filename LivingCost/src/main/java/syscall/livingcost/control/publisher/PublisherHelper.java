    package syscall.livingcost.control.publisher;

    import org.apache.activemq.ActiveMQConnectionFactory;
    import syscall.livingcost.control.feeder.Serializer;

    import javax.jms.*;

    public class PublisherHelper {
        private static final String brokerUrl = "tcp://localhost:61616";
        private static Connection connection = null;

        private static Connection connect() throws JMSException {
            if (connection == null) {
                ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
                connection = factory.createConnection();
                connection.start();
            }
            return connection;
        }

        public static void publishEvent(String topicName, Object event) throws JMSException {
            String jsonContent = Serializer.serialize(event);
            Connection connection = connect();
            Session session = null;
            MessageProducer producer = null;
            try {
                session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Topic topic = session.createTopic(topicName);
                producer = session.createProducer(topic);

                producer.setDeliveryMode(DeliveryMode.PERSISTENT);
                TextMessage message = session.createTextMessage(jsonContent);

                producer.send(message);

            } catch (JMSException e) {
                System.err.println("Error publicando el evento en ActiveMQ: " + e.getMessage());
            } finally {
                if (producer != null) {
                    try {
                        producer.close();
                    } catch (JMSException e) {
                        System.err.println("Error al cerrar el producer: " + e.getMessage());
                    }
                }
                if (session != null) {
                    try {
                        session.close();
                    } catch (JMSException e) {
                        System.err.println("Error al cerrar la sesión: " + e.getMessage());
                    }
                }
            }
        }
    }
