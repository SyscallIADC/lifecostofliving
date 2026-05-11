package syscall.livingcost.control.database;

import org.junit.jupiter.api.Test;
import syscall.livingcost.model.LivingCost;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PersistenceMapperTest {
    @Test
    void testExtractDouble() {
        assertEquals(2.5, PersistenceMapper.extractDouble(2.5));
        assertEquals(10.0, PersistenceMapper.extractDouble(10));
        assertEquals(0.0, PersistenceMapper.extractDouble(null));
        assertEquals(0.0, PersistenceMapper.extractDouble("String"));
    }

    @Test
    void testFromMapWithStrings() {
        Map<String, Object> data = new HashMap<>();
        data.put("country", "Spain");
        data.put("currency", "EUR");
        data.put("cappuccino", 2.0);
        data.put("milk", 1.0);
        data.put("date", "2026-04-17");
        data.put("time", "13:00:00");

        LivingCost result = PersistenceMapper.fromMap(data);

        assertEquals("Spain", result.country());
        assertEquals(2.0, result.cappuccino());
        assertEquals(LocalDate.of(2026, 4, 17), result.date());
        assertEquals(LocalTime.of(13, 0, 0), result.time());
    }

    @Test
    void testFromMapWithObjects() {
        LocalDate hoy = LocalDate.now();
        LocalTime ahora = LocalTime.now();

        Map<String, Object> data = new HashMap<>();
        data.put("country", "France");
        data.put("currency", "EUR");
        data.put("date", hoy);
        data.put("time", ahora);

        LivingCost result = PersistenceMapper.fromMap(data);

        assertEquals(hoy, result.date());
        assertEquals(ahora, result.time());
    }

    @Test
    void testFromMapMissingData() {
        Map<String, Object> data = new HashMap<>();
        data.put("country", "Japan");
        data.put("currency", "JPY");
        data.put("date", "2026-01-01");
        data.put("time", "10:00:00");

        LivingCost result = PersistenceMapper.fromMap(data);

        assertEquals(0.0, result.cappuccino(), "Si falta el dato, debe ser 0.0");
        assertNotNull(result.country());
    }
}
