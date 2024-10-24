package org.k5va.currencyscraper.repository;

import org.k5va.currencyscraper.entity.Currency;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface CurrencyRepository extends ReactiveMongoRepository<Currency, UUID> {

    Flux<Currency> findByType(Currency.Type type);
}
