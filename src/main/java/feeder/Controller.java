package feeder;

import models.ExchangeRate;

import javax.jms.JMSException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Controller {

    private final ExchangeRateFeeder feeder;
    private final ActiveMQExchangeRatePublisher publisher;
    private final ScheduledExecutorService executor;

    private static final String BASE_CURRENCY = "EUR";
    private static final String[] CURRENCIES = {
            "CHF", "GBP", "INR", "RUB",
            "JPY", "CNY", "AUD", "USD", "MXN", "ARS",
            "PEN", "BRL", "THB", "KRW", "AED",
            "KYD", "DOP", "ILS", "QAR", "ZAR"
    };

    public Controller(ExchangeRateFeeder feeder, ActiveMQExchangeRatePublisher publisher) {
        this.feeder = feeder;
        this.publisher = publisher;
        this.executor = Executors.newSingleThreadScheduledExecutor();
    }

    public void start() {
        try {
            publisher.start();
        } catch (JMSException e) {
            System.out.println("Error al conectar con ActiveMQ: " + e.getMessage());
            return;
        }
        System.out.println("Controller iniciado. Ejecutando cada 24 horas.");
        executor.scheduleAtFixedRate(this::fetchAndPublish, 0, 24, TimeUnit.HOURS);
    }

    public void stop() {
        System.out.println("Controller detenido.");
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) executor.shutdownNow();
            publisher.stop();
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        } catch (JMSException e) {
            System.out.println("Error al cerrar publisher: " + e.getMessage());
        }
    }

    private void fetchAndPublish() {
        System.out.println("Iniciando ciclo de consultas...");
        int success = 0, failed = 0;

        for (String currency : CURRENCIES) {
            try {
                Thread.sleep(1500);
                ExchangeRate rate = feeder.feed(BASE_CURRENCY, currency);
                publisher.publish(rate);
                System.out.println("EUR -> " + currency + " = " + rate.getExchangeRate());
                success++;
            } catch (Exception e) {
                System.out.println("Error EUR -> " + currency + ": " + e.getMessage());
                failed++;
            }
        }
        System.out.println("Ciclo completado. Éxitos: " + success + " | Fallos: " + failed);
    }
