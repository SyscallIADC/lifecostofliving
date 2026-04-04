package consumer;
import models.LivingCost;

public class TestPeru {
    public static void main(String[] args) {
        System.out.println("=== TEST DIRECTO: PERÚ ===");

        try (WebScrapper scraper = new WebScrapper()) {
            System.out.println("Extrayendo datos (con el nuevo parsePrice)...");

            LivingCost peru = scraper.extractDataForCountry("Peru");

            System.out.println("\n--- RESULTADOS ---");
            System.out.println("Moneda: " + peru.getCurrency());
            System.out.println("Capuccino: " + peru.getCapuccino());
            System.out.println("Sueldo Medio: " + peru.getSalaryMonth());
            System.out.println("Agua: " + peru.getWater());
            System.out.println("==================");

            if (peru.getCapuccino() > 0.0) {
                System.out.println("✅ ¡ÉXITO! La expresión regular funcionó.");
            } else {
                System.out.println("❌ Sigue fallando. Devuelve 0.0.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}