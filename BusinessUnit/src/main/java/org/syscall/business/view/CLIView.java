package org.syscall.business.view;

import org.syscall.business.control.BusinessController;
import org.syscall.business.model.CountryStats;
import org.syscall.business.model.MarketTrend;

import java.util.List;
import java.util.Scanner;

public class CLIView {
    private final BusinessController controller;
    private final Scanner scanner;

    public CLIView(BusinessController controller) {
        this.controller = controller;
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
            System.out.println("2. Buscar destinos recomendados (Top 'Nivel Óptimo')");
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

        double rateToEur = controller.convertToEuros(userCurrency);
        double salaryInEur = rawSalary * rateToEur;

        System.out.print("Introduce el país a consultar (Ej. Spain, Japan): ");
        String countryName = scanner.nextLine();

        CountryStats stats = controller.getCountryDetails(countryName);
        if (stats == null) {
            System.out.println("❌ No hay datos disponibles para el país: " + countryName);
            return;
        }

        MarketTrend trend = controller.getMarketTrend(countryName, userCurrency);

        System.out.println("\n--- RESULTADOS PARA " + stats.country().toUpperCase() + " ---");
        System.out.printf("Estado del Mercado: %s %s\n", trend.getEmoji(), trend.getDescription());
        System.out.printf("Tu sueldo actual: %.2f %s (Equivale a %.2f €)\n", rawSalary, userCurrency, salaryInEur);
        System.out.printf("Salario medio local neto: %.2f €\n\n", stats.avgSalary());

        checkCategory("Soltero", salaryInEur, stats.costSingle());
        checkCategory("Nivel Óptimo", salaryInEur, stats.costOptimum());
        checkCategory("Familia", salaryInEur, stats.costFamily());
    }

    private void recommendCountries() {
        System.out.print("Introduce tu moneda local (Ej. EUR, USD, GBP): ");
        String userCurrency = scanner.nextLine().toUpperCase();
        if (userCurrency.isBlank()) userCurrency = "EUR";

        System.out.print("Introduce tu sueldo mensual en " + userCurrency + ": ");
        double rawSalary = readDouble();

        double rateToEur = controller.convertToEuros(userCurrency);
        double salaryInEur = rawSalary * rateToEur;

        System.out.printf("\n--- TOP 10 PAÍSES PARA 'NIVEL ÓPTIMO' CON %.2f€ (Convertido) ---\n", salaryInEur);
        List<CountryStats> viableCountries = controller.getTopViableCountries(salaryInEur);

        if (viableCountries.isEmpty()) {
            System.out.println("❌ Con ese sueldo está difícil alcanzar un 'Nivel Óptimo' de vida en los registros actuales.");
        } else {
            for (CountryStats s : viableCountries) {
                MarketTrend trend = controller.getMarketTrend(s.country(), userCurrency);
                System.out.printf("%s %-15s | Coste Vida: %7.2f€ | Ahorro mensual: %7.2f€\n",
                        trend.getEmoji(), s.country(), s.costOptimum(), (salaryInEur - s.costOptimum()));
            }
        }
    }

    private void compareCountries() {
        System.out.print("País de origen: ");
        String origin = scanner.nextLine();
        System.out.print("País de destino: ");
        String destination = scanner.nextLine();

        CountryStats statsOrg = controller.getCountryDetails(origin);
        CountryStats statsDest = controller.getCountryDetails(destination);

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