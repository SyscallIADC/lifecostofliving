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

    public double costGoodLiving() {
        return costSingle() + gym + (costSingle() * 0.40);
    }

    public double costFamily() {
        return rent3Bed + (foodBasket * 2.5) + utilities + (transport * 2) + childCare;
    }
}
