package scheduler;

import database.ExchangeRateStore;
import feeder.Feeder;
import models.ExchangeRate;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ControllerER implements ExchangeRateController {

    private final Feeder<ExchangeRate> feeder;
    private final ExchangeRateStore<ExchangeRate> database;
    private final ScheduledExecutorService executor;

    private static final String BASE_CURRENCY = "EUR";

    private static final String[] CURRENCIES = {
            "CHF", "GBP", "INR", "RUB",
            "JPY", "CNY", "AUD", "USD", "MXN", "ARS",
            "PEN", "BRL", "THB", "KRW", "AED",
            "KYD", "DOP", "ILS", "QAR", "ZAR"
    };

    public ControllerER(Feeder<ExchangeRate> feeder, ExchangeRateStore<ExchangeRate> database) {
        this.feeder = feeder;
        this.database = database;
        this.executor = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void start() {
        System.out.println("Scheduler iniciado. Ejecutando cada 24 horas.");
        executor.scheduleAtFixedRate(
                this::fetchAndStore,
                0,
                24,
                TimeUnit.HOURS
        );
    }

    @Override
    public void stop() {
        System.out.println("Scheduler detenido.");
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private void fetchAndStore() {
        System.out.println("Iniciando ciclo de consultas...");
        int success = 0;
        int failed = 0;

        for (String currency : CURRENCIES) {
            try {
                Thread.sleep(1500);
                ExchangeRate rate = feeder.extractData(BASE_CURRENCY, currency);
                database.insertData(rate);
                System.out.println("EUR -> " + currency + " = " + rate.getExchangeRate());
                success++;
            } catch (Exception e) {
                System.out.println("Error EUR -> " + currency + ": " + e.getMessage());
                failed++;
            }
        }

        System.out.println("Ciclo completado. Éxitos: " + success + " | Fallos: " + failed);
    }
}