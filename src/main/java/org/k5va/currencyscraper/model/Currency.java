package org.k5va.currencyscraper.model;

import java.math.BigDecimal;

public record Currency(
        BigDecimal usd,
        BigDecimal eur
) {
}
