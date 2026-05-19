package org.syscall.business.control.cli;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.syscall.business.control.datamart.DatamartDAO;
import org.syscall.business.control.datamart.ParsedEvent;
import org.syscall.business.view.CLIView;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;

public class CLIViewTest {

    private DatamartDAO datamart;

    private static final String LIVING_COST_JSON = "{\"ts\":\"2026-05-17T10:00:00Z\",\"ss\":\"feeder-living-cost\",\"data\":{\"country\":\"Spain\",\"currency\":\"EUR\",\"cappuccino\":2.5,\"milk\":1.0,\"rice\":1.5,\"bread\":1.2,\"eggs\":2.0,\"cheese\":5.0,\"chicken\":6.0,\"beef\":10.0,\"fruits\":2.0,\"vegetables\":1.5,\"water\":0.5,\"publicTransport\":40.0,\"gasoline\":1.6,\"car\":20000.0,\"utilities\":100.0,\"childCare\":400.0,\"gymMonthly\":35.0,\"bedroomMonth\":800.0,\"apartmentMonth\":1200.0,\"apartmentBuy\":3000.0,\"salaryMonth\":1800.0,\"interestRate\":3.5}}";
    private static final String EXCHANGE_RATE_JSON = "{\"ts\":\"2026-05-17T10:00:00Z\",\"ss\":\"ExchangeRate-feeder\",\"data\":{\"fromCurrency\":\"EUR\",\"toCurrency\":\"USD\",\"exchangeRate\":1.08,\"lastRefreshed\":\"2026-05-17\",\"timeZone\":\"UTC\"}}";

    @BeforeEach
    void setUp() {
        datamart = new DatamartDAO();
        datamart.upsertCountryData(new ParsedEvent("LivingCost", "feeder-living-cost", "20260517", LIVING_COST_JSON));
        datamart.upsertExchangeRateData(new ParsedEvent("ExchangeRate", "ExchangeRate-feeder", "20260517", EXCHANGE_RATE_JSON));
    }

    private CLIView createCLIWithInput(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        return new CLIView(datamart);
    }

    @Test
    void testMenuExitOption() {
        CLIView cli = createCLIWithInput("4\n");
        assertDoesNotThrow(cli::startMenu);
    }

    @Test
    void testAnalyzeCountryValidInput() {
        String input = "1\nEUR\n2000\nSpain\n4\n";
        CLIView cli = createCLIWithInput(input);
        assertDoesNotThrow(cli::startMenu);
    }

    @Test
    void testAnalyzeCountryNotFound() {
        String input = "1\nEUR\n2000\nUnknownCountry\n4\n";
        CLIView cli = createCLIWithInput(input);
        assertDoesNotThrow(cli::startMenu);
    }

    @Test
    void testRecommendCountriesViable() {
        String input = "2\nEUR\n5000\n4\n";
        CLIView cli = createCLIWithInput(input);
        assertDoesNotThrow(cli::startMenu);
    }

    @Test
    void testRecommendCountriesNotViable() {
        String input = "2\nEUR\n1\n4\n";
        CLIView cli = createCLIWithInput(input);
        assertDoesNotThrow(cli::startMenu);
    }

    @Test
    void testCompareCountriesValid() {
        String input = "3\nSpain\nSpain\n4\n";
        CLIView cli = createCLIWithInput(input);
        assertDoesNotThrow(cli::startMenu);
    }

    @Test
    void testCompareCountriesNotFound() {
        String input = "3\nUnknown\nAlsoUnknown\n4\n";
        CLIView cli = createCLIWithInput(input);
        assertDoesNotThrow(cli::startMenu);
    }

    @Test
    void testInvalidMenuOption() {
        String input = "9\n4\n";
        CLIView cli = createCLIWithInput(input);
        assertDoesNotThrow(cli::startMenu);
    }

    @Test
    void testAnalyzeCountryWithBlankCurrency() {
        String input = "1\n\n2000\nSpain\n4\n";
        CLIView cli = createCLIWithInput(input);
        assertDoesNotThrow(cli::startMenu);
    }

    @Test
    void testRecommendWithBlankCurrency() {
        String input = "2\n\n5000\n4\n";
        CLIView cli = createCLIWithInput(input);
        assertDoesNotThrow(cli::startMenu);
    }

    @Test
    void testInvalidSalaryInput() {
        String input = "1\nEUR\nabc\n2000\nSpain\n4\n";
        CLIView cli = createCLIWithInput(input);
        assertDoesNotThrow(cli::startMenu);
    }
}