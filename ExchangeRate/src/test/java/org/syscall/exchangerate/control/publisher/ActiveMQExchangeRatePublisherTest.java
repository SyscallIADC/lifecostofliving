package org.syscall.exchangerate.control.publisher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.syscall.exchangerate.models.ExchangeRate;

import javax.jms.Connection;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ActiveMQExchangeRatePublisherTest {

    private ActiveMQExchangeRatePublisher publisher;
    private Connection mockConnection;
    private Session mockSession;
    private MessageProducer mockProducer;

    @BeforeEach
    void setUp() throws Exception {
        publisher = new ActiveMQExchangeRatePublisher();

        mockConnection = mock(Connection.class);
        mockSession = mock(Session.class);
        mockProducer = mock(MessageProducer.class);

        setPrivateField(publisher, "connection", mockConnection);
        setPrivateField(publisher, "session", mockSession);
        setPrivateField(publisher, "producer", mockProducer);
    }

    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void testPublish() throws Exception {
        ExchangeRate rate = new ExchangeRate("EUR", "JPY", 160.0, "2026-05-18", "UTC");
        TextMessage mockTextMessage = mock(TextMessage.class);

        when(mockSession.createTextMessage(anyString())).thenReturn(mockTextMessage);

        publisher.publish(rate);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mockSession).createTextMessage(captor.capture());
        verify(mockProducer).send(mockTextMessage);

        String jsonPayload = captor.getValue();

        assertTrue(jsonPayload.contains("\"fromCurrency\":\"EUR\""));
        assertTrue(jsonPayload.contains("\"toCurrency\":\"JPY\""));
        assertTrue(jsonPayload.contains("\"exchangeRate\":160.0"));
        assertTrue(jsonPayload.contains("\"ss\":\"ExchangeRate-feeder\""));
        assertTrue(jsonPayload.contains("\"ts\":"));
    }

    @Test
    void testStop() throws Exception {
        publisher.stop();

        verify(mockProducer).close();
        verify(mockSession).close();
        verify(mockConnection).close();
    }
}