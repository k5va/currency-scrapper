package org.k5va.currencyscraper.scraper;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.util.ResourceUtils;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.nio.file.Files;

@SpringBootTest
class NbpEurToUsdScraperIT {

    private static MockWebServer mockNbpServer;

    @Autowired
    private NbpEurToUsdScraper scraper;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry r) {
        r.add("app.currency.nbp.url", () -> mockNbpServer.url("/").toString());
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
        var jsonCurrencyData = Files.readString(
                ResourceUtils.getFile("classpath:data/currency.json").toPath());

        mockNbpServer.enqueue(new MockResponse()
                .setBody(jsonCurrencyData)
                .addHeader("Content-Type", "application/json; charset=utf-8"));

        // when
        var result = scraper.scrape();

        // then
        StepVerifier.create(result)
                .expectNextMatches("1.08"::equals)
                .verifyComplete();
    }

    // TODO test errors (invalid json, 404, etc.)

}