package org.syscall.business.control.network;

import org.junit.jupiter.api.Test;
import org.syscall.business.control.datamart.DatamartDAO;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BusinessSubscriberTest {

    @Test
    void testStartWithInvalidBrokerDoesNotCrash() {
        DatamartDAO datamart = new DatamartDAO();
        BusinessSubscriber subscriber = new BusinessSubscriber(datamart);
        assertDoesNotThrow(() -> subscriber.start(List.of("ExchangeRate", "LivingCost")));
    }

    @Test
    void testStopWithoutStartDoesNotCrash() {
        DatamartDAO datamart = new DatamartDAO();
        BusinessSubscriber subscriber = new BusinessSubscriber(datamart);
        assertDoesNotThrow(subscriber::stop);
    }

    @Test
    void testStartAndStopDoesNotCrash() {
        DatamartDAO datamart = new DatamartDAO();
        BusinessSubscriber subscriber = new BusinessSubscriber(datamart);
        assertDoesNotThrow(() -> {
            subscriber.start(List.of("ExchangeRate"));
            subscriber.stop();
        });
    }
}