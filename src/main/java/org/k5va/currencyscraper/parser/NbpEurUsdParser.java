package org.k5va.currencyscraper.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.k5va.currencyscraper.error.ParserException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Map;

@Component
@Slf4j
public class NbpEurUsdParser implements Parser<Map<String, BigDecimal>> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Map<String, BigDecimal> parse(String currencyJson) {
        log.debug("Parsing NBP currency rates {}", currencyJson);

        try {
            Assert.hasText(currencyJson, "Currency json cannot be empty");

            BigDecimal usdRate = null;
            BigDecimal eurRate = null;
            var rates = objectMapper.readTree(currencyJson).get(0).get("rates");

            for (var rateNode: rates) {
                var code = rateNode.get("code").asText();
                if (code.equals("USD")) {
                    usdRate = new BigDecimal(rateNode.get("mid").asText());
                } else if (code.equals("EUR")) {
                    eurRate = new BigDecimal(rateNode.get("mid").asText());
                }
            }

            Assert.notNull(usdRate, "USD rate not found");
            Assert.notNull(eurRate, "EUR rate not found");

            return Map.of(
                    "USD", usdRate,
                    "EUR", eurRate
            );
        } catch (Exception e) {
            throw new ParserException(e);
        }
    }
}
