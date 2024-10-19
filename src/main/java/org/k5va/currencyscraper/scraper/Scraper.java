package org.k5va.currencyscraper.scraper;

import reactor.core.publisher.Mono;

public interface Scraper<T> {
    Mono<T> scrape();
}
