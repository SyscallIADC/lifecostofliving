package org.syscall.control.feeder;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.syscall.control.api.APIConsumer;
import org.syscall.control.publisher.ActiveMQExchangeRatePublisher;
import org.syscall.control.publisher.Serializer;
import org.syscall.models.ExchangeRate;

import java.lang.reflect.Field;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class APIExchangeRateFeederTest {

    @Mock
    private APIConsumer apiConsumer;
    @Mock
    private ActiveMQExchangeRatePublisher publisher;
    @Mock
    private Serializer serializer;

    private APIExchangeRateFeeder buildFeeder() throws Exception {
        // Creamos el objeto sin pasar por el constructor normal
        APIExchangeRateFeeder feeder = org.mockito.internal.util.reflection.ReflectionMemberAccessor
                .class.getClassLoader()
                .loadClass("org.syscall.control.feeder.APIExchangeRateFeeder")
                .asSubclass(APIExchangeRateFeeder.class)
                .getDeclaredConstructor(ActiveMQExchangeRatePublisher.class)
                .newInstance(publisher);
        return feeder;
    }

    private APIExchangeRateFeeder buildFeederWithFields() throws Exception {
        // Usamos unsafe para crear la instancia sin llamar al constructor
        java.lang.reflect.Constructor<APIExchangeRateFeeder> constructor =
                APIExchangeRateFeeder.class.getDeclaredConstructor(ActiveMQExchangeRatePublisher.class);

        org.junit.jupiter.api.Assumptions.assumeTrue(
                System.getenv("ALPHAVANTAGE_API_KEY") != null,
                "ALPHAVANTAGE_API_KEY no definida, test omitido"
        );

        APIExchangeRateFeeder feeder = constructor.newInstance(publisher);
        setField(feeder, "apiConsumer", apiConsumer);
        setField(feeder, "serializer", serializer);
        return feeder;
    }

    @Test
    void testFeedCallsPublisher() throws Exception {
        APIExchangeRateFeeder feeder = buildFeederWithFields();

        JsonObject fakeResponse = buildFakeResponse();
        when(apiConsumer.fetch(anyString())).thenReturn(fakeResponse);
        when(serializer.serialize(any(ExchangeRate.class), anyString())).thenReturn("{\"fake\":\"json\"}");

        feeder.feed("EUR", "USD");

        verify(publisher, times(1)).send(anyString());
    }

    @Test
    void testFeedCallsApiConsumer() throws Exception {
        APIExchangeRateFeeder feeder = buildFeederWithFields();

        JsonObject fakeResponse = buildFakeResponse();
        when(apiConsumer.fetch(anyString())).thenReturn(fakeResponse);
        when(serializer.serialize(any(ExchangeRate.class), anyString())).thenReturn("{\"fake\":\"json\"}");

        feeder.feed("EUR", "USD");

        verify(apiConsumer, times(1)).fetch(anyString());
    }

    private JsonObject buildFakeResponse() {
        JsonObject data = new JsonObject();
        data.addProperty("1. From_Currency Code", "EUR");
        data.addProperty("3. To_Currency Code", "USD");
        data.addProperty("5. Exchange Rate", 1.16);
        data.addProperty("6. Last Refreshed", "2026-05-15 10:00:00");
        data.addProperty("7. Time Zone", "UTC");

        JsonObject response = new JsonObject();
        response.add("Realtime Currency Exchange Rate", data);
        return response;
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}