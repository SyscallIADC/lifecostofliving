package org.syscall.dacd.eventstorebuilder;

import org.syscall.dacd.eventstorebuilder.control.EventStoreBuilder;

public class Main {
    public static void main(String[] args) {
        EventStoreBuilder builder = new EventStoreBuilder();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                builder.stop();
            } catch (Exception e) {
                System.out.println("Error al cerrar: " + e.getMessage());
            }
        }));

        try {
            builder.start();
            System.out.println("Esperando eventos... (Ctrl+C para parar)");
            Thread.currentThread().join();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
