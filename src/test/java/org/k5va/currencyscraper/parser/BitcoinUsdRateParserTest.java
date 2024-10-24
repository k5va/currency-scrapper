package org.k5va.currencyscraper.parser;

import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class BitcoinUsdRateParserTest {
    private final BtcUsdRateParser parser = new BtcUsdRateParser();

    @Test
    void parserShouldReturnCorrectValueOnValidJson() throws IOException {
        // given
        var rate = new BigDecimal("67674.733");
        var jsonCurrencyData = Files.readString(
                ResourceUtils.getFile("classpath:data/btc_coindesk.json").toPath());

        // when
        var result = parser.parse(jsonCurrencyData);

        // then
        assertEquals(rate, result);
    }

}