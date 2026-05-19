package syscall.livingcost.control;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.syscall.livingcost.control.ExtractionJob;
import org.syscall.livingcost.control.database.LivingCostStore;
import org.syscall.livingcost.control.database.countryQueue.CountryQueueStore;
import org.syscall.livingcost.control.feeder.LivingCostFeeder;
import org.syscall.livingcost.model.LivingCost;

import java.util.Random;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExtrationJobTest {
    @Mock
    private LivingCostStore storeMock;

    @Mock
    private LivingCostFeeder feederMock;

    @Mock
    private CountryQueueStore queueMock;

    @Mock
    private Random randomMock;

    @InjectMocks
    private ExtractionJob job;

    @AfterEach
    public void tearDown() {
        Thread.interrupted();
    }

    @Test
    void testAbortNoCountryFound() {
        when(queueMock.getNextCountry()).thenReturn(null);
        Thread.currentThread().interrupt();
        job.extraction();

        verify(queueMock).getNextCountry();
        verifyNoInteractions(feederMock);
        verifyNoInteractions(storeMock);
    }

    @Test
    void testExtractionFlow() {
        String country = "Japan";
        LivingCost mockCost = mock(LivingCost.class);

        when(mockCost.currency()).thenReturn("JPY");
        when(mockCost.cappuccino()).thenReturn(3.5);
        when(queueMock.getNextCountry()).thenReturn(country);
        when(feederMock.feed(country)).thenReturn(mockCost);
        when(storeMock.retrieveLastData()).thenReturn(mockCost);

        Thread.currentThread().interrupt();

        job.extraction();

        verify(queueMock).getNextCountry();
        verify(feederMock).feed(country);
        verify(storeMock).insertData(mockCost);
        verify(queueMock).setCountryAsScrapped(country);
        verify(storeMock).retrieveLastData();
    }
}
