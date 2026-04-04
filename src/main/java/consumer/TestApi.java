package consumer;

import models.ExchangeRate;

public class TestApi {
    public static void main(String[] args) {
        try {
            // Crear consumidor usando variable de entorno
            ApiConsumer consumer = new ApiConsumer(); // Toma la API key de ALPHAVANTAGE_API_KEY

            // Aquí defines las monedas (después esto vendrá de la UI)
            String from = "USD";
            String to = "EUR";

            // Llamada a la API
            ExchangeRate rate = consumer.getExchangeRate(from, to);

            // Mostrar resultados
            System.out.println("De " + rate.getFromCurrency() + " a " + rate.getToCurrency());
            System.out.println("Tasa: " + rate.getExchangeRate());
            System.out.println("Última actualización: " + rate.getLastRefreshed());
            System.out.println(rate.toMap());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
