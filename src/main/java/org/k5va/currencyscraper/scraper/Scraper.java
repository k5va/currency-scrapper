package org.k5va.currencyscraper.scraper;

import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface Scraper<T> {
    Mono<T> scrape(LocalDate date);

    Mono<T> scrape();
}
