package org.k5va.currencyscraper.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.k5va.currencyscraper.error.ParserException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Optional;

@Component
@Slf4j
public class NbsMiddleRateParser implements Parser<BigDecimal> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public BigDecimal parse(String json) {
        log.info("Parsing NBS currency rates {}", json);

        try {
            Assert.hasText(json, "Currency json cannot be empty");

            return Optional.ofNullable(objectMapper.readTree(json))
                    .map(node -> node.get("KursnaLista"))
                    .map(node -> node.get("SrednjiKursEurNBS"))
                    .map(JsonNode::asText)
                    .map(BigDecimal::new)
                    .orElseThrow(() -> new ParserException("NBS middle rate not found"));
        } catch (Exception e) {
            throw new ParserException(e);
        }
    }
}
