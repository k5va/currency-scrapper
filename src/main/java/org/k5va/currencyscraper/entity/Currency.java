package org.k5va.currencyscraper.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Builder
@Data
@Document(collection = "currencies")
public class Currency {
    @Id
    private UUID id;

    private BigDecimal value;

    private LocalDate date;

    private Type type;

    public enum Type {
        NBP_EUR_USD,
        NBS_MIDDLE_RATE,
        BTC_USD
    }
}
