package feeder;

import models.ExchangeRate;
import models.ExchangeRateParser;

public class TestApi {
    public static void main(String[] args) {
        try {
            APIExchangeRateFeeder feeder = new APIExchangeRateFeeder();

            String from = "USD";
            String to = "EUR";

            ExchangeRate rate = feeder.extractData(from, to);

            System.out.println("De " + rate.getFromCurrency() + " a " + rate.getToCurrency());
            System.out.println("Tasa: " + rate.getExchangeRate());
            System.out.println("Última actualización: " + rate.getLastRefreshed());

            ExchangeRateParser parser = new ExchangeRateParser();
            System.out.println(parser.toMap(rate));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}