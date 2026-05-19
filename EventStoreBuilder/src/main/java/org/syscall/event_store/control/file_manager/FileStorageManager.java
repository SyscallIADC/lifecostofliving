package org.syscall.event_store.control.file_manager;

import org.syscall.event_store.control.parser.ParsedEvent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileStorageManager {
    private static final String BASE_DIR = "eventstore";

    public static void save(ParsedEvent event) {
        try{
            Path directoryPath = Paths.get(BASE_DIR, event.topic(), event.ss());

            Files.createDirectories(directoryPath);

            Path filePath = directoryPath.resolve(event.folderDate() + ".events");

            String lineToAppend = event.rawJson() + System.lineSeparator();

            Files.writeString(filePath, lineToAppend,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Error al guardar el evento " + e.getMessage());
        }
    }
}
