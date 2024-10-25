package org.k5va.currencyscraper.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.k5va.currencyscraper.dto.CurrencyDto;
import org.k5va.currencyscraper.entity.Currency;
import org.k5va.currencyscraper.repository.CurrencyRepository;
import org.k5va.currencyscraper.scraper.BtcUsdRateScraper;
import org.k5va.currencyscraper.scraper.NbpEurToUsdScraper;
import org.k5va.currencyscraper.scraper.NbsMiddleRateScraper;
import org.k5va.currencyscraper.scraper.Scraper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyService {
    private final CurrencyRepository currencyRepository;
    private final NbpEurToUsdScraper nbpEurToUsdScraper;
    private final NbsMiddleRateScraper nbsMiddleRateScraper;
    private final BtcUsdRateScraper btcUsdRateScraper;

    public Flux<CurrencyDto> getCurrencyRates(Currency.Type type) {
        return currencyRepository
                .findByType(type)
                .map(currency -> new CurrencyDto(currency.getValue(),
                        currency.getDate(),
                        currency.getType().name()));
    }

    public Mono<CurrencyDto> scrapeNbpEurUsdRate() {
        return scrape(nbpEurToUsdScraper, Currency.Type.NBP_EUR_USD);
    }

    public Mono<CurrencyDto> scrapeNbsMiddleRate() {
        return scrape(nbsMiddleRateScraper, Currency.Type.NBS_MIDDLE_RATE);
    }

    public Mono<CurrencyDto> scrapeBtcUsdRate() {
        return scrape(btcUsdRateScraper, Currency.Type.BTC_USD);
    }

    private Mono<CurrencyDto> scrape(Scraper<CurrencyDto> scraper, Currency.Type type) {
        return scraper.scrape()
                .flatMap(currencyDto -> currencyRepository.save(Currency.builder()
                        .id(UUID.randomUUID())
                        .date(currencyDto.date())
                        .value(currencyDto.value())
                        .type(type)
                        .build()))
                .map(currency -> new CurrencyDto(currency.getValue(),
                        currency.getDate(),
                        currency.getType().name()));
    }
}
