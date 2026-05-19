package org.syscall.exchangerate.control.publisher;

import org.syscall.exchangerate.models.ExchangeRate;

import javax.jms.JMSException;

public interface ExchangeRatePublisher {
    void start() throws JMSException;
    void publish(ExchangeRate rate) throws JMSException;
    void stop() throws JMSException;
}
