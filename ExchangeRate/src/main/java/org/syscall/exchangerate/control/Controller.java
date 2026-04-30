package org.syscall.exchangerate.control;


import org.syscall.exchangerate.control.database.ExchangeRateStore;
import org.syscall.exchangerate.control.feeder.ExchangeRateFeeder;
import org.syscall.exchangerate.models.ExchangeRate;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Controller {

    private final ExchangeRateFeeder feeder;
    private final ExchangeRateStore store;
    private final ScheduledExecutorService executor;

    private static final String BASE_CURRENCY = "EUR";

    private static final String[] CURRENCIES = {
            "CHF", "GBP", "INR", "RUB",
            "JPY", "CNY", "AUD", "USD", "MXN", "ARS",
            "PEN", "BRL", "THB", "KRW", "AED",
            "KYD", "DOP", "ILS", "QAR", "ZAR"
    };

    public Controller(ExchangeRateFeeder feeder, ExchangeRateStore store) {
        this.feeder = feeder;
        this.store = store;
        this.executor = Executors.newSingleThreadScheduledExecutor();
    }


    public void start() {
        System.out.println("Controller iniciado. Ejecutando cada 24 horas.");
        executor.scheduleAtFixedRate(this::fetchAndStore, 0, 24, TimeUnit.HOURS);
    }

    public void stop() {
        System.out.println("Controller detenido.");
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) executor.shutdownNow();
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private void fetchAndStore() {
        System.out.println("Iniciando ciclo de consultas...");
        int success = 0, failed = 0;

        for (String currency : CURRENCIES) {
            try {
                Thread.sleep(1500);
                ExchangeRate rate = feeder.feed(BASE_CURRENCY, currency);
                store.insertData(rate);
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
