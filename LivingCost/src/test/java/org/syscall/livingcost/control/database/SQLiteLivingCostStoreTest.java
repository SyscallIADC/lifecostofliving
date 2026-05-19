package org.syscall.livingcost.control.database;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.syscall.livingcost.control.database.DatabaseHelper;
import org.syscall.livingcost.control.database.SQLiteLivingCostStore;
import org.syscall.livingcost.model.LivingCost;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SQLiteLivingCostStoreTest {
    @TempDir
    Path tempDir;

    private SQLiteLivingCostStore store;
    private String databasePath;

    @BeforeEach
    public void setUp(){
        databasePath = "jdbc:sqlite:" +tempDir.resolve("test.db");
        DatabaseHelper.initialize(databasePath);
        store = new SQLiteLivingCostStore(databasePath);
    }

    @Test
    void testInsertAndRetrieveByDate() {
        LocalDate fecha = LocalDate.of(2026, 4, 17);
        LivingCost costToInsert = crearLivingCostFalso("Spain", fecha, LocalTime.of(10, 0));

        store.insertData(costToInsert);
        List<LivingCost> results = store.retrieveDataByDate("2026-04-17");

        assertEquals(1, results.size(), "Debería haber exactamente 1 registro para esta fecha");

        LivingCost retrievedCost = results.get(0);
        assertEquals("Spain", retrievedCost.country());
        assertEquals("EUR", retrievedCost.currency());
        assertEquals(2.5, retrievedCost.cappuccino()); // Verificamos un precio para asegurar el mapeo
        assertEquals(fecha, retrievedCost.date());
    }

    @Test
    void testRetrieveLastData(){
        LivingCost costOld = crearLivingCostFalso("France", LocalDate.of(2026, 1, 1), LocalTime.of(10, 0));
        LivingCost costNewest = crearLivingCostFalso("Spain", LocalDate.of(2026, 4, 18), LocalTime.of(15, 30));
        LivingCost costMiddle = crearLivingCostFalso("Italy", LocalDate.of(2026, 3, 10), LocalTime.of(12, 0));

        store.insertData(costOld);
        store.insertData(costNewest);
        store.insertData(costMiddle);

        LivingCost lastData = store.retrieveLastData();

        assertNotNull(lastData, "No debería devolver null si hay datos");
        assertEquals("Spain", lastData.country(), "Debería haber recuperado el registro del 18 de Abril (el más reciente)");
        assertEquals(LocalDate.of(2026, 4, 18), lastData.date());
        assertEquals(LocalTime.of(15, 30), lastData.time());
    }

    @Test
    void testHandleEmptyDatabase() {
        List<LivingCost> listResult = store.retrieveDataByDate("2026-01-01");
        LivingCost lastDataResult = store.retrieveLastData();

        assertTrue(listResult.isEmpty(), "La lista debería estar vacía si no hay registros");
        assertNull(lastDataResult, "retrieveLastData debería devolver null si no hay registros");
    }

    private LivingCost crearLivingCostFalso(String country, LocalDate date, LocalTime time) {
        return new LivingCost(
                country, "EUR",
                2.5, 1.0, 1.5, 2.0, 3.0,
                10.0, 6.0, 15.0, 2.5, 2.0,
                0.5, 30.0, 1.6, 25000.0, 150.0,
                400.0, 40.0, 900.0, 1500.0, 3500.0,
                2000.0, 3.5,
                date, time);
    }
}
