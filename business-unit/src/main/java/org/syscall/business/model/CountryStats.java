package org.syscall.business.model;

public record CountryStats(
        String country,
        double rent1Bed,
        double rent3Bed,
        double foodBasket,
        double utilities,
        double transport,
        double gym,
        double childCare,
        double avgSalary
) {
    public double costSingle() {
        return rent1Bed + foodBasket + utilities + transport;
    }

    public double costOptimum() {
        return costSingle() + gym + (costSingle() * 0.40);
    }

    public double costFamily() {
        double baseLivingCosts = foodBasket + utilities + transport;
        return rent3Bed + (baseLivingCosts * 2.5);
    }
}