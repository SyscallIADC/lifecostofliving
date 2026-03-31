package consumer;

import models.LivingCost;
import java.util.Map;

public class Test {

    public static void main(String[] args) {
        String targetCountry = "Finland";

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