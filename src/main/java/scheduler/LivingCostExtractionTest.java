package scheduler;

import database.DatabaseHelper;

public class LivingCostExtractionTest {
    public static void main(String[] args) {
        System.out.println("Empezando test...");

        DatabaseHelper db = DatabaseHelper.getInstance();
        System.out.println("Conectado a la base de datos con éxito...");

        LivingCostExtraction test = new LivingCostExtraction();
        System.out.println("Simulando ciclo...");
        test.run();

        System.out.println("Ciclo finalizado");
        try{
            Thread.sleep(13568);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Simulando segundo ciclo...");
        test.run();
        System.out.println("Ciclo finalizado");

        System.out.println("Comprobando que los datos se guardan bien...");
        java.util.List<models.LivingCost> lastInserts = database.DatabaseHelper.getInstance().getLastInserts();

        if (lastInserts.isEmpty()) {
            System.out.println("❌ No se encontraron datos en la tabla cost_of_living.");
        } else {
            for (models.LivingCost cost : lastInserts) {
                System.out.println("\n-------------------------------------------------");
                System.out.println("PAÍS: " + cost.getCountry());
                System.out.println("FECHA/HORA: " + cost.getDate() + " " + cost.getTime());
                System.out.println("PRECIO CAPUCCINO: " + cost.getCapuccino() + " " + cost.getCurrency());
                System.out.println("SUELDO MEDIO: " + cost.getSalaryMonth());
                System.out.println("-------------------------------------------------");
            }
        }

    }

}
