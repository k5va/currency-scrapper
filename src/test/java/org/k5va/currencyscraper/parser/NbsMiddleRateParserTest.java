package org.k5va.currencyscraper.parser;

import org.junit.jupiter.api.Test;
import org.k5va.currencyscraper.error.ParserException;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NbsMiddleRateParserTest {

    private final NbsMiddleRateParser parser = new NbsMiddleRateParser();

    @Test
    void parserShouldReturnCorrectValueOnValidJson() throws IOException {
        // given
        var middleRate = new BigDecimal("117.0199");
        var jsonData = Files.readString(
                ResourceUtils.getFile("classpath:data/nbs.json").toPath());

        // when
        var result = parser.parse(jsonData);

        // then
        assertEquals(middleRate, result);
    }

    @Test
    void parserShouldThrowOnInvalidJson() {
        // given
        var jsonData = "invalid json";

        // then
        assertThrows(ParserException.class, () -> parser.parse(jsonData));
    }
}