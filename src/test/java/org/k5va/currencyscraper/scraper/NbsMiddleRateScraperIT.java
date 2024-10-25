package org.k5va.currencyscraper.scraper;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.k5va.currencyscraper.entity.Currency;
import org.k5va.currencyscraper.error.ScraperException;
import org.k5va.currencyscraper.dto.CurrencyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.util.ResourceUtils;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDate;

@SpringBootTest
class NbsMiddleRateScraperIT {

    private static MockWebServer mockNbpServer;

    @Autowired
    private NbsMiddleRateScraper scraper;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry r) {
        r.add("app.currency.nbs.url", () -> mockNbpServer.url("/").toString());
    }

    @BeforeAll
    static void setUp() throws IOException {
        mockNbpServer = new MockWebServer();
        mockNbpServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockNbpServer.shutdown();
    }

    @Test
    void scrapeShouldReturnCorrectValue() throws IOException {
        // given
        var currency = new CurrencyDto(new BigDecimal("117.0199"),
                LocalDate.now(),
                Currency.Type.NBS_MIDDLE_RATE.name());
        var jsonCurrencyData = Files.readString(
                ResourceUtils.getFile("classpath:data/nbs.json").toPath());

        mockNbpServer.enqueue(new MockResponse()
                .setBody(jsonCurrencyData)
                .addHeader("Content-Type", "application/json; charset=utf-8"));

        // when
        var result = scraper.scrape();

        // then
        StepVerifier.create(result)
                .expectNextMatches(currency::equals)
                .verifyComplete();
    }

    @Test
    void scrapeShouldThrowOnInvalidJson() {
        // given
        var jsonCurrencyData = "invalid json";

        mockNbpServer.enqueue(new MockResponse()
                .setBody(jsonCurrencyData)
                .addHeader("Content-Type", "application/json; charset=utf-8"));

        // when
        var result = scraper.scrape();

        // then
        StepVerifier.create(result)
                .expectError(ScraperException.class)
                .verify();
    }

    @Test
    void scrapeShouldThrowOnEmptyJson() {
        // given
        var jsonCurrencyData = "";

        mockNbpServer.enqueue(new MockResponse()
                .setBody(jsonCurrencyData)
                .addHeader("Content-Type", "application/json; charset=utf-8"));

        // when
        var result = scraper.scrape();

        // then
        StepVerifier.create(result)
                .expectError(ScraperException.class)
                .verify();
    }

    @Test
    void scrapeShouldThrowOnNotFound() {
        // given
        mockNbpServer.enqueue(new MockResponse()
                .setStatus("HTTP/1.1 404 NOT_FOUND")
                .addHeader("Content-Type", "text/plain; charset=UTF-8"));

        // when
        var result = scraper.scrape();

        // then
        StepVerifier.create(result)
                .expectError(ScraperException.class)
                .verify();
    }

    @Test
    void scrapeShouldThrowOnCurrencyNotFound() {
        // given
        var jsonCurrencyData = """
                {
                   "KursnaLista": {
                     "Id": 2748,
                     "Datum": "/Date(1729202400000)/",
                     "ProdajniKursEurErste": 117.9,
                     "ProdajniKursEurNBS": 117.371,
                     "Valid": true,
                     "LastModify": null
                   },
                   "Dan": 18,
                   "Mesec": 10,
                   "Godina": 2024
                 }
                """;

        mockNbpServer.enqueue(new MockResponse()
                .setBody(jsonCurrencyData)
                .addHeader("Content-Type", "application/json; charset=utf-8"));

        // when
        var result = scraper.scrape();

        // then
        StepVerifier.create(result)
                .expectError(ScraperException.class)
                .verify();
    }
}