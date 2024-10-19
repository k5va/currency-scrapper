package org.k5va.currencyscraper.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.k5va.currencyscraper.scraper.NbpEurToUsdScraper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyService {

    private final NbpEurToUsdScraper scraper;

    public Mono<String> getCurrency() {
        return scraper.scrape();
    }
}
