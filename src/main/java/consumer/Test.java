package consumer;

import models.LivingCost;

import java.sql.Array;
import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Test {
    public static void main(String[] args) {
        List<String> countries = Arrays.asList("Spain", "United Kingdom", "France");

        for(String targetCountry : countries) {
            int sleepTime = ThreadLocalRandom.current().nextInt(10000, 30000+1);
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Iniciando prueba de extracción para: " + targetCountry);
            System.out.println("Levantando Playwright (modo headless)...");

            try (WebScrapper scraper = new WebScrapper()) {

                LivingCost costData = scraper.extractDataForCountry(targetCountry);
                imprimirReporte(costData);

            } catch (Exception exception) {
                System.err.println("La prueba ha fallado debido a un error crítico:");
                exception.printStackTrace();
            }
        }
    }
    private static void imprimirReporte(LivingCost costData) {
        System.out.println("\n=================================================");
        System.out.println("RESULTADOS DE LA PRUEBA: " + costData.getCountry().toUpperCase());
        System.out.println("=================================================");

        Map<String, Object> dataMap = costData.toMap();

        dataMap.forEach((key, value) ->
                System.out.printf("%-25s : %s%n", key, value)
        );

        System.out.println("=================================================\n");
    }
}