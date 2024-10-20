package org.k5va.currencyscraper.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.k5va.currencyscraper.scraper.NbpEurToUsdScraper;
import org.k5va.currencyscraper.scraper.NbsMiddleRateScraper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyService {

    private final NbpEurToUsdScraper nbpEurToUsdScraper;
    private final NbsMiddleRateScraper nbsMiddleRateScraper;

    public Mono<String> getNbpEurUsdRate() {
        return nbpEurToUsdScraper.scrape();
    }

    public Mono<String> getNbsMiddleRate(LocalDate date) {
        return nbsMiddleRateScraper.scrape(date);
    }
}
