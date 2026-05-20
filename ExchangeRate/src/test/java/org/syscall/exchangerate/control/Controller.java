package org.syscall.exchangerate.control;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.syscall.exchangerate.control.feeder.ExchangeRateFeeder;
import org.syscall.exchangerate.control.publisher.ExchangeRatePublisher;

import javax.jms.JMSException;
import java.lang.reflect.Field;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ControllerTest {

    private ExchangeRateFeeder mockFeeder;
    private ExchangeRatePublisher mockPublisher;
    private Controller controller;

    @BeforeEach
    void setUp() {
        mockFeeder = mock(ExchangeRateFeeder.class);
        mockPublisher = mock(ExchangeRatePublisher.class);
        controller = new Controller(mockFeeder, mockPublisher);
    }

    @Test
    void testStartInitializesPublisher() throws JMSException {
        controller.start();
        verify(mockPublisher, times(1)).start();
    }

    @Test
    void testStartHandlesJMSExceptionGracefully() throws JMSException {
        doThrow(new JMSException("Simulated Connection Error")).when(mockPublisher).start();

        controller.start();

        verify(mockPublisher, times(1)).start();
    }

    @Test
    void testStopShutsDownPublisher() throws JMSException {
        controller.stop();
        verify(mockPublisher, times(1)).stop();
    }

    @Test
    void testStartSchedulesFetchTask() throws Exception {
        ScheduledExecutorService mockExecutor = mock(ScheduledExecutorService.class);
        Field executorField = Controller.class.getDeclaredField("executor");
        executorField.setAccessible(true);
        executorField.set(controller, mockExecutor);

        controller.start();

        verify(mockExecutor, times(1)).scheduleAtFixedRate(
                any(Runnable.class),
                eq(0L),
                eq(24L),
                eq(TimeUnit.HOURS)
        );
    }
}