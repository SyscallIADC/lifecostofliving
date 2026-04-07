package models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LivingCostTest {
    private LivingCost testModel;
    private final LocalTime testTime = LocalTime.of(12,0);
    private final LocalDate testDate = LocalDate.of(2026,3,12);

    @BeforeEach
    void setUp() {
        testModel = new LivingCost("Spain", "EUR", 2.5, 1.2, 1.0, 1.1, 2.0, 10.0, 7.0, 15.0,
                3.0, 2.5, 1.0, 1.5, 1.8, 25000.0, 150.0, 400.0, 40.0,
                800.0, 1200.0, 3000.0, 2000.0, 3.5, testDate, testTime
        );
    }

    @Test
    void testGetters() {
        assertEquals("Spain", testModel.getCountry());
        assertEquals("EUR", testModel.getCurrency());
        assertEquals(1.0, testModel.getWater());
        assertEquals(2.5, testModel.getCapuccino());
        assertEquals(testDate, testModel.getDate());
    }

    @Test
    void testToMap() {
        Map<String, Object> map = testModel.toMap();

        assertNotNull(map);
        assertTrue(map.containsKey("public_transport"));
        assertTrue(map.containsKey("eggs"));
        assertTrue(map.containsKey("salary_month"));
        assertEquals(1.5, map.get("public_transport"));
    }
    @Test
    void testAllGetters() {
        assertAll("Propiedades de LivingCost",
                () -> assertEquals("Spain", testModel.getCountry()),
                () -> assertEquals("EUR", testModel.getCurrency()),
                () -> assertEquals(2.5, testModel.getCapuccino()),
                () -> assertEquals(1.2, testModel.getMilk()),
                () -> assertEquals(1.1, testModel.getBread()),
                () -> assertEquals(1.0, testModel.getRice()),
                () -> assertEquals(2.0, testModel.getEggs()),
                () -> assertEquals(10.0, testModel.getCheese()),
                () -> assertEquals(7.0, testModel.getChicken()),
                () -> assertEquals(15.0, testModel.getBeef()),
                () -> assertEquals(3.0, testModel.getFruits()),
                () -> assertEquals(2.5, testModel.getVegetables()),
                () -> assertEquals(1.0, testModel.getWater()),
                () -> assertEquals(1.5, testModel.getPublicTransport()),
                () -> assertEquals(1.8, testModel.getGasoline()),
                () -> assertEquals(25000.0, testModel.getCar()),
                () -> assertEquals(150.0, testModel.getUtilities()),
                () -> assertEquals(400.0, testModel.getChildCare()),
                () -> assertEquals(40.0, testModel.getGymMonthly()),
                () -> assertEquals(800.0, testModel.getBedroomMonth()),
                () -> assertEquals(1200.0, testModel.getAppartmentMonth()),
                () -> assertEquals(3000.0, testModel.getAppartmentBuy()),
                () -> assertEquals(2000.0, testModel.getSalaryMonth()),
                () -> assertEquals(3.5, testModel.getInterestRateTwentyYears()),
                () -> assertEquals(testDate, testModel.getDate()),
                () -> assertEquals(testTime, testModel.getTime())
        );
    }

    @Test
    void testFromMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("country", "Peru");
        map.put("currency", "PEN");
        map.put("capuccino", 3.2);
        map.put("date", testDate);
        map.put("time", testTime);

        LivingCost testResult = LivingCost.fromMap(map);

        assertEquals("Peru",  testResult.getCountry());
        assertEquals("PEN", testResult.getCurrency());
        assertEquals(3.2,  testResult.getCapuccino());
        assertEquals(testDate, testResult.getDate());
        assertEquals(0.0, testResult.getMilk());
    }

    @Test
    void testExtractDouble() {
        assertEquals(5.5, LivingCost.extractDouble(5.5));
        assertEquals(10.0, LivingCost.extractDouble(10));
        assertEquals(0.0, LivingCost.extractDouble("String de ejemplo"));
        assertEquals(0.0, LivingCost.extractDouble(null));
    }
}
