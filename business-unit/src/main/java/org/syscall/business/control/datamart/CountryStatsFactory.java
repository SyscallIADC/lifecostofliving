package org.syscall.business.control.datamart;

import org.syscall.business.model.CountryStats;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryStatsFactory {
    public static CountryStats create(ResultSet rs, double rateToEur) throws SQLException {
        double foodBasketLocal = (rs.getDouble("milk") * 10.0) +
                (rs.getDouble("rice") * 3.0) +
                (rs.getDouble("bread") * 15.0) +
                (rs.getDouble("eggs") * 3.0) +
                (rs.getDouble("cheese") * 1.5) +
                (rs.getDouble("chicken") * 5.0) +
                (rs.getDouble("beef") * 3.0) +
                (rs.getDouble("fruits") * 8.0) +
                (rs.getDouble("vegetables") * 8.0);

        return new CountryStats(
                rs.getString("country"),
                rs.getDouble("bedroomMonth") * rateToEur,
                rs.getDouble("apartmentMonth") * rateToEur,
                foodBasketLocal * rateToEur,
                rs.getDouble("utilities") * rateToEur,
                rs.getDouble("publicTransport") * rateToEur,
                rs.getDouble("gymMonthly") * rateToEur,
                rs.getDouble("childCare") * rateToEur,
                rs.getDouble("salaryMonth") * rateToEur
        );
    }
}