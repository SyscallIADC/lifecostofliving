package syscall.livingcost.model;

import java.time.LocalDate;
import java.time.LocalTime;

public record LivingCost(
        String country,
        String currency,
        double cappuccino,
        double milk,
        double rice,
        double bread,
        double eggs,
        double cheese,
        double chicken,
        double beef,
        double fruits,
        double vegetables,
        double water,
        double publicTransport,
        double gasoline,
        double car,
        double utilities,
        double childCare,
        double gymMonthly,
        double bedroomMonth,
        double apartmentMonth,
        double apartmentBuy,
        double salaryMonth,
        double interestRate
) {}