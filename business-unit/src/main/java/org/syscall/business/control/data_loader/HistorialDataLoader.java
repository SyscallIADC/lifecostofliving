package org.syscall.business.control.data_loader;

import org.syscall.business.control.datamart.DatamartRepository;
import org.syscall.business.control.datamart.ParsedEvent;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class HistorialDataLoader {
    private static final String BASE_DIR = "eventstore";

    public static void load(DatamartRepository datamart) {
        Path startPath = Paths.get(BASE_DIR);

        if (!Files.exists(startPath)) {
            System.out.println("No se encontró la carpeta eventstore. El Datamart iniciará vacío.");
            return;
        }

        System.out.println("Iniciando carga histórica desde el Event Store...");

        try (Stream<Path> paths = Files.walk(startPath)) {
            paths.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".events"))
                    .forEach(file -> processFile(file, datamart));

            System.out.println("Carga histórica completada con éxito.");
        } catch (Exception e) {
            System.err.println("Error explorando el directorio histórico: " + e.getMessage());
        }
    }



    private static void processFile(Path file, DatamartRepository datamart) {
        String pathStr = file.toString();
        String topic = pathStr.contains("LivingCost") ? "LivingCost" :
                (pathStr.contains("ExchangeRate") ? "ExchangeRate" : "Unknown");

        if (topic.equals("Unknown")) return;

        try (Stream<String> lines = Files.lines(file)) {
            lines.forEach(jsonLine -> {
                ParsedEvent event = new ParsedEvent(topic, "historical-load", "", jsonLine);
                if (topic.equals("LivingCost")) {
                    datamart.upsertCountryData(event);
                } else {
                    datamart.upsertExchangeRateData(event);
                }
            });
        } catch (Exception e) {
            System.err.println("Error leyendo el archivo " + file.getFileName() + ": " + e.getMessage());
        }
    }
}
