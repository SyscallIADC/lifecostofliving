package models;
import java.time.LocalDate;
import java.time.LocalTime;

public class LivingCost {
    private final String country;
    private final String currency;
    private final double capuccino;
    private final double milk;
    private final double bread;
    private final double rice;
    private final double eggs;
    private final double cheese;
    private final double chicken;
    private final double beef;
    private final double fruits;
    private final double vegetables;
    private final double water;
    private final double publicTransport;
    private final double gasoline;
    private final double car;
    private final double utilities;
    private final double childCare;
    private final double gymMonthly;
    private final double bedroomMonth;
    private final double appartmentMonth;
    private final double appartmentBuy;
    private final double salaryMonth;
    private final double interestRateTwentyYears;
    private final LocalDate date;
    private final LocalTime time;

    public LivingCost(String country, String currency, double capuccino, double milk, double rice, double bread, double eggs, double cheese, double chicken, double beef, double fruits, double vegetables, double water, double publicTransport, double gasoline, double car, double utilities, double childCare, double gymMonthly, double bedroomMonth, double appartmentMonth, double appartmentBuy, double salaryMonth, double interestRateTwentyYears, LocalDate date, LocalTime time) {
        this.country = country;
        this.currency = currency;
        this.capuccino = capuccino;
        this.milk = milk;
        this.rice = rice;
        this.bread = bread;
        this.eggs = eggs;
        this.cheese = cheese;
        this.chicken = chicken;
        this.beef = beef;
        this.fruits = fruits;
        this.vegetables = vegetables;
        this.water = water;
        this.publicTransport = publicTransport;
        this.gasoline = gasoline;
        this.car = car;
        this.utilities = utilities;
        this.childCare = childCare;
        this.gymMonthly = gymMonthly;
        this.bedroomMonth = bedroomMonth;
        this.appartmentMonth = appartmentMonth;
        this.appartmentBuy = appartmentBuy;
        this.salaryMonth = salaryMonth;
        this.interestRateTwentyYears = interestRateTwentyYears;
        this.date = date;
        this.time = time;
    }

    public String getCountry() {
        return country;
    }

    public String getCurrency() {
        return currency;
    }

    public double getCapuccino() {
        return capuccino;
    }

    public double getMilk() {
        return milk;
    }

    public double getBread() {
        return bread;
    }

    public double getRice() {
        return rice;
    }

    public double getEggs() {
        return eggs;
    }

    public double getCheese() {
        return cheese;
    }

    public double getChicken() {
        return chicken;
    }

    public double getBeef() {
        return beef;
    }

    public double getFruits() {
        return fruits;
    }

    public double getVegetables() {
        return vegetables;
    }

    public double getWater() {
        return water;
    }

    public double getPublicTransport() {
        return publicTransport;
    }

    public double getGasoline() {
        return gasoline;
    }

    public double getCar() {
        return car;
    }

    public double getUtilities() {
        return utilities;
    }

    public double getChildCare() {
        return childCare;
    }

    public double getGymMonthly() {
        return gymMonthly;
    }

    public double getBedroomMonth() {
        return bedroomMonth;
    }

    public double getAppartmentMonth() {
        return appartmentMonth;
    }

    public double getAppartmentBuy() {
        return appartmentBuy;
    }

    public double getSalaryMonth() {
        return salaryMonth;
    }

    public double getInterestRateTwentyYears() {
        return interestRateTwentyYears;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }
}
