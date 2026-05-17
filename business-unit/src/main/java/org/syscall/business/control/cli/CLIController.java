package org.syscall.business.control.cli;

import org.syscall.business.control.datamart.DatamartRepository;
import org.syscall.business.model.CountryStats;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class CLIController {
    private final DatamartRepository datamart;
    private final Scanner scanner;

    public CLIController(DatamartRepository datamart) {
        this.datamart = datamart;
        this.scanner = new Scanner(System.in);
    }

    public void startMenu() {
        boolean running = true;
        System.out.println("\n=========================================");
        System.out.println("   DATAMART BUSINESS UNIT - SPRINT 3");
        System.out.println("=========================================");

        while (running) {
            System.out.println("\n--- CALCULADORA DE COSTE REAL ---");
            System.out.println("1. Analizar viabilidad de sueldo en un país");
            System.out.println("2. Buscar destinos recomendados (Top 'Buen Vivir')");
            System.out.println("3. Comparar calidad de vida entre dos países");
            System.out.println("4. Salir");
            System.out.print("Elige una opción (1-4): ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> analyzeCountry();
                case "2" -> recommendCountries();
                case "3" -> compareCountries();
                case "4" -> running = false;
                default -> System.out.println("❌ Opción no válida. Inténtalo de nuevo.");
            }
        }
        System.out.println("Cerrando el sistema...");
    }

    private void analyzeCountry() {
        System.out.print("Introduce tu moneda local (Ej. EUR, USD, GBP, MXN): ");
        String userCurrency = scanner.nextLine().toUpperCase();
        if (userCurrency.isBlank()) userCurrency = "EUR";

        System.out.print("Introduce tu sueldo mensual en " + userCurrency + ": ");
        double rawSalary = readDouble();

        double rateToEur = datamart.getExchangeRate(userCurrency, "EUR");
        double salaryInEur = rawSalary * rateToEur;

        System.out.print("Introduce el país a consultar (Ej. Spain, Japan): ");
        String countryName = scanner.nextLine();

        CountryStats stats = datamart.getCountryStats(countryName);
        if (stats == null) {
            System.out.println("❌ No hay datos disponibles para el país: " + countryName);
            return;
        }

        System.out.println("\n--- RESULTADOS PARA " + stats.country().toUpperCase() + " (Todo en Euros) ---");
        System.out.printf("Salario medio local neto: %.2f€\n\n", stats.avgSalary());

        checkCategory("Soltero", salaryInEur, stats.costSingle());
        checkCategory("Buen Vivir", salaryInEur, stats.costGoodLiving());
        checkCategory("Familia", salaryInEur, stats.costFamily());
    }

    private void recommendCountries() {
        System.out.print("Introduce tu moneda local (Ej. EUR, USD, GBP): ");
        String userCurrency = scanner.nextLine().toUpperCase();
        if (userCurrency.isBlank()) userCurrency = "EUR";

        System.out.print("Introduce tu sueldo mensual en " + userCurrency + ": ");
        double rawSalary = readDouble();

        double rateToEur = datamart.getExchangeRate(userCurrency, "EUR");
        double salaryInEur = rawSalary * rateToEur;

        System.out.printf("\n--- TOP 10 PAÍSES PARA 'BUEN VIVIR' CON %.2f€ (Convertido) ---\n", salaryInEur);
        List<CountryStats> all = datamart.getAllCountries();

        List<CountryStats> viableCountries = all.stream()
                .filter(s -> salaryInEur > s.costGoodLiving())
                .sorted(Comparator.comparingDouble((CountryStats s) -> salaryInEur - s.costGoodLiving()).reversed())
                .limit(10)
                .toList();

        if (viableCountries.isEmpty()) {
            System.out.println("❌ Con ese sueldo está difícil alcanzar el nivel de 'Buen Vivir'.");
        } else {
            for (CountryStats s : viableCountries) {
                System.out.printf("✅ %-15s | Coste Vida: %7.2f€ | Ahorro mensual: %7.2f€\n",
                        s.country(), s.costGoodLiving(), (salaryInEur - s.costGoodLiving()));
            }
        }
    }

    private void compareCountries() {
        System.out.print("País de origen: ");
        String origin = scanner.nextLine();
        System.out.print("País de destino: ");
        String destination = scanner.nextLine();

        CountryStats statsOrg = datamart.getCountryStats(origin);
        CountryStats statsDest = datamart.getCountryStats(destination);

        if (statsOrg == null || statsDest == null) {
            System.out.println("❌ Faltan datos de alguno de los países en la base de datos.");
            return;
        }

        System.out.println("\n--- COMPARATIVA: " + statsOrg.country() + " vs " + statsDest.country() + " ---");

        double diffSingle = statsDest.costSingle() - statsOrg.costSingle();
        double diffFamily = statsDest.costFamily() - statsOrg.costFamily();
        double diffSalary = statsDest.avgSalary() - statsOrg.avgSalary();

        System.out.printf("- Coste Soltero:  %s (Diferencia de %.2f€)\n",
                diffSingle > 0 ? "Más caro" : "Más barato", Math.abs(diffSingle));
        System.out.printf("- Coste Familia:  %s (Diferencia de %.2f€)\n",
                diffFamily > 0 ? "Más caro" : "Más barato", Math.abs(diffFamily));
        System.out.printf("- Salario Medio:  %s (Diferencia de %.2f€)\n",
                diffSalary > 0 ? "Más alto" : "Más bajo", Math.abs(diffSalary));
    }

    private void checkCategory(String category, double salary, double cost) {
        if (salary >= cost) {
            System.out.printf("✅ %-12s: VIABLE   (Coste aprox: %7.2f€ | Te sobran: %7.2f€)\n", category, cost, salary - cost);
        } else {
            System.out.printf("❌ %-12s: INVIABLE (Coste aprox: %7.2f€ | Te faltan: %7.2f€)\n", category, cost, cost - salary);
        }
    }

    private double readDouble() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.print("❌ Por favor, introduce un número válido: ");
            }
        }
    }
}