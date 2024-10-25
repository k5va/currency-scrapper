package org.k5va.currencyscraper.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CurrencyDto(
        BigDecimal value,
        LocalDate date,
        String type
) {
}
