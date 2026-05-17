package org.syscall.business.control.data_loader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.syscall.business.control.datamart.DatamartDAO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class HistorialDataLoaderTest {

    @TempDir
    Path tempDir;

    private DatamartDAO datamart;

    @BeforeEach
    void setUp() {
        datamart = new DatamartDAO();
    }

    @Test
    void testLoadFromNonExistentDirectory() {
        assertDoesNotThrow(() -> HistorialDataLoader.load(datamart),
                "No debería lanzar excepción si no existe el directorio eventstore");
    }

    @Test
    void testLoadLivingCostEvents() throws IOException {
        Path eventsDir = tempDir.resolve("eventstore/LivingCost/feeder-living-cost");
        Files.createDirectories(eventsDir);

        String json = "{\"ts\":\"2026-05-17T10:00:00Z\",\"ss\":\"feeder-living-cost\",\"data\":{\"country\":\"TestCountry\",\"currency\":\"EUR\",\"cappuccino\":2.5,\"milk\":1.0,\"rice\":1.5,\"bread\":1.2,\"eggs\":2.0,\"cheese\":5.0,\"chicken\":6.0,\"beef\":10.0,\"fruits\":2.0,\"vegetables\":1.5,\"water\":0.5,\"publicTransport\":40.0,\"gasoline\":1.6,\"car\":20000.0,\"utilities\":100.0,\"childCare\":400.0,\"gymMonthly\":35.0,\"bedroomMonth\":800.0,\"apartmentMonth\":1200.0,\"apartmentBuy\":3000.0,\"salaryMonth\":1800.0,\"interestRate\":3.5}}";

        Files.writeString(eventsDir.resolve("20260517.events"), json);

        assertDoesNotThrow(() -> HistorialDataLoader.load(datamart));
    }

    @Test
    void testLoadExchangeRateEvents() throws IOException {
        Path eventsDir = tempDir.resolve("eventstore/ExchangeRate/ExchangeRate-feeder");
        Files.createDirectories(eventsDir);

        String json = "{\"ts\":\"2026-05-17T10:00:00Z\",\"ss\":\"ExchangeRate-feeder\",\"data\":{\"fromCurrency\":\"EUR\",\"toCurrency\":\"USD\",\"exchangeRate\":1.08,\"lastRefreshed\":\"2026-05-17\",\"timeZone\":\"UTC\"}}";

        Files.writeString(eventsDir.resolve("20260517.events"), json);

        assertDoesNotThrow(() -> HistorialDataLoader.load(datamart));
    }

    @Test
    void testLoadIgnoresUnknownTopics() throws IOException {
        Path eventsDir = tempDir.resolve("eventstore/UnknownTopic/some-source");
        Files.createDirectories(eventsDir);
        Files.writeString(eventsDir.resolve("20260517.events"), "{\"ts\":\"2026-05-17T10:00:00Z\"}");

        assertDoesNotThrow(() -> HistorialDataLoader.load(datamart));
    }

    @Test
    void testLoadMultipleEventsFromFile() throws IOException {
        Path eventsDir = tempDir.resolve("eventstore/LivingCost/feeder-living-cost");
        Files.createDirectories(eventsDir);

        String json1 = "{\"ts\":\"2026-05-17T10:00:00Z\",\"ss\":\"feeder-living-cost\",\"data\":{\"country\":\"France\",\"currency\":\"EUR\",\"cappuccino\":2.5,\"milk\":1.0,\"rice\":1.5,\"bread\":1.2,\"eggs\":2.0,\"cheese\":5.0,\"chicken\":6.0,\"beef\":10.0,\"fruits\":2.0,\"vegetables\":1.5,\"water\":0.5,\"publicTransport\":40.0,\"gasoline\":1.6,\"car\":20000.0,\"utilities\":100.0,\"childCare\":400.0,\"gymMonthly\":35.0,\"bedroomMonth\":800.0,\"apartmentMonth\":1200.0,\"apartmentBuy\":3000.0,\"salaryMonth\":1800.0,\"interestRate\":3.5}}";
        String json2 = "{\"ts\":\"2026-05-17T10:00:00Z\",\"ss\":\"feeder-living-cost\",\"data\":{\"country\":\"Germany\",\"currency\":\"EUR\",\"cappuccino\":3.0,\"milk\":1.2,\"rice\":1.8,\"bread\":1.5,\"eggs\":2.5,\"cheese\":6.0,\"chicken\":7.0,\"beef\":12.0,\"fruits\":2.5,\"vegetables\":2.0,\"water\":0.6,\"publicTransport\":50.0,\"gasoline\":1.8,\"car\":22000.0,\"utilities\":120.0,\"childCare\":500.0,\"gymMonthly\":40.0,\"bedroomMonth\":900.0,\"apartmentMonth\":1400.0,\"apartmentBuy\":4000.0,\"salaryMonth\":2500.0,\"interestRate\":3.5}}";

        Files.writeString(eventsDir.resolve("20260517.events"), json1 + "\n" + json2);

        assertDoesNotThrow(() -> HistorialDataLoader.load(datamart));
    }

    @Test
    void testLoadEmptyFile() throws IOException {
        Path eventsDir = tempDir.resolve("eventstore/LivingCost/feeder-living-cost");
        Files.createDirectories(eventsDir);
        Files.writeString(eventsDir.resolve("20260517.events"), "");

        assertDoesNotThrow(() -> HistorialDataLoader.load(datamart));
    }
}