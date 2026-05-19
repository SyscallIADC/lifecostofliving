package org.syscall.livingcost.control.feeder;

import org.junit.jupiter.api.Test;
import org.syscall.livingcost.control.feeder.LivingCostFactory;
import org.syscall.livingcost.control.webScraper.NumbeoLivingCostData;
import org.syscall.livingcost.model.LivingCost;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class LivingCostFactoryTest {
    @Test
    public void testNumbeoDataToLivingCost() {
        NumbeoLivingCostData data = mock(NumbeoLivingCostData.class);
        when(data.currency()).thenReturn("EUR");
        when(data.cappuccino()).thenReturn(3.5);
        when(data.salaryMonth()).thenReturn(4025.80);
        when(data.interestRate()).thenReturn(5.2);

        String country = "Spain";

        LivingCost result = LivingCostFactory.fromNumbeoData(data, country);

        assertEquals("Spain", result.country());
        assertEquals("EUR", result.currency());
        assertEquals(3.50, result.cappuccino());
        assertEquals(4025.80, result.salaryMonth());
        assertEquals(5.2, result.interestRate());
        assertEquals(LocalDate.now(), result.date(), "Data should be today");
        assertNotNull(result.time(), "Hour should not be null");
    }
}
