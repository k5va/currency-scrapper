package org.k5va.currencyscraper.scraper;

import org.junit.jupiter.api.Test;
import org.k5va.currencyscraper.model.Currency;
import org.k5va.currencyscraper.parser.NbpEurUsdParser;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class NbpEurUsdParserTest {

    private final NbpEurUsdParser parser = new NbpEurUsdParser();

    @Test
    void parserShouldReturnCorrectValueOnValidJson() throws IOException {
        // given
        var currency = new Currency(
                new BigDecimal("3.9718"),
                new BigDecimal("4.3048"));
        var jsonCurrencyData = Files.readString(
                ResourceUtils.getFile("classpath:data/currency.json").toPath());

        // when
        var result = parser.parse(jsonCurrencyData);

        // then
        assertEquals(currency, result);
    }
}