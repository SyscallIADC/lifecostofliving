package org.syscall.livingcost.control.database.countryQueue;

import java.util.List;

public class CountryQueueInitializer {
    public static void initialize(String databasePath) {
        List<String> initialCountries = List.of(
                "Spain",
                "France",
                "Switzerland",
                "Germany",
                "United Kingdom",
                "Italy",
                "Greece",
                "Finland",
                "Sweden",
                "Norway",
                "Austria",
                "Russia",
                "Andorra",
                "China",
                "Japan",
                "Australia",
                "United States",
                "Canada",
                "Mexico",
                "Argentina",
                "Peru",
                "Brazil",
                "Ireland",
                "Netherlands",
                "Thailand",
                "India",
                "South Korea",
                "United Arab Emirates",
                "Portugal",
                "Cayman Islands",
                "Dominican Republic",
                "Panama",
                "Qatar",
                "Israel",
                "South Africa"
        );
        SQLiteCountryQueueStore.createTable(databasePath);
        for (String country : initialCountries){
            SQLiteCountryQueueStore.insertCountryToQueue(country, databasePath);
        }
    }
}
