package org.k5va.currencyscraper.parser;

import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class NbpEurUsdParserTest {

    private final NbpEurUsdParser parser = new NbpEurUsdParser();

    @Test
    void parserShouldReturnCorrectValueOnValidJson() throws IOException {
        // given
        var currencies = Map.of(
                "USD", new BigDecimal("3.9718"),
                "EUR", new BigDecimal("4.3048")
        );
        var jsonCurrencyData = Files.readString(
                ResourceUtils.getFile("classpath:data/nbp.json").toPath());

        // when
        var result = parser.parse(jsonCurrencyData);

        // then
        assertEquals(currencies, result);
    }
}