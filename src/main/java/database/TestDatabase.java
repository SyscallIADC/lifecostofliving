package database;

public class TestDatabase {
    public static void main(String[] args) {
        System.out.println("Iniciando test...");
        DatabaseHelper db = DatabaseHelper.getInstance();
        System.out.println("Base de datos iniciada");
        for (int paso=1; paso<=10; paso++) {
            System.out.println("Ciclo del scheduler" + paso);
            String paisActual = db.getNextCountry();
            if (paisActual == null) {
                System.out.println("Error, la base de datos devuelve null.");
                break;
            }
            System.out.println("La base de datos devuelve: " + paisActual);
            System.out.println("Simulando scrapeo...");
            simularScrapeo();

            db.setCountryAsScrapped(paisActual);
            System.out.println("Scrapeo correcto para " + paisActual + " fecha actualizada");
        }
        System.out.println("Finalizando test...");
    }

    private static void simularScrapeo() {
        try{
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
