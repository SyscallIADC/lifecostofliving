package org.syscall.business.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CountryStatsTest {

    private CountryStats createTestStats() {
        return new CountryStats(
                "Spain",
                "2026-05-13T13:16:51.186952100Z",
                800.0,   // rent1Bed
                1200.0,  // rent3Bed
                300.0,   // foodBasket
                100.0,   // utilities
                40.0,    // transport
                35.0,    // gym
                400.0,   // childCare
                1800.0   // avgSalary
        );
    }

    @Test
    void testCostSingle() {
        CountryStats stats = createTestStats();
        double expected = 800.0 + 300.0 + 100.0 + 40.0;
        assertEquals(expected, stats.costSingle(), 0.01);
    }

    @Test
    void testCostGoodLiving() {
        CountryStats stats = createTestStats();
        double costSingle = stats.costSingle();
        double expected = costSingle + 35.0 + (costSingle * 0.40);
        assertEquals(expected, stats.costOptimum(), 0.01);
    }

    @Test
    void testCostFamily() {
        CountryStats stats = createTestStats();
        double expected = 1200.0 + (300.0 * 2.5) + 100.0 + (40.0 * 2) + 400.0;
        assertEquals(expected, stats.costFamily(), 0.01);
    }

    @Test
    void testCountryName() {
        CountryStats stats = createTestStats();
        assertEquals("Spain", stats.country());
    }

    @Test
    void testAvgSalary() {
        CountryStats stats = createTestStats();
        assertEquals(1800.0, stats.avgSalary(), 0.01);
    }
}