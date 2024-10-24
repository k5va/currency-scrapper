package org.k5va.currencyscraper.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.k5va.currencyscraper.dto.CurrencyDto;
import org.k5va.currencyscraper.entity.Currency;
import org.k5va.currencyscraper.repository.CurrencyRepository;
import org.k5va.currencyscraper.scraper.NbpEurToUsdScraper;
import org.k5va.currencyscraper.scraper.NbsMiddleRateScraper;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyService {
    private final CurrencyRepository currencyRepository;
    private final NbpEurToUsdScraper nbpEurToUsdScraper;
    private final NbsMiddleRateScraper nbsMiddleRateScraper;

    public Flux<CurrencyDto> getNbpEurUsdRate() {
        return currencyRepository
                .findByType(Currency.Type.NBP_EUR_USD)
                .map(currency -> new CurrencyDto(currency.getValue(),
                        currency.getDate(),
                        currency.getType().name()));
    }

    public Flux<CurrencyDto> getNbsMiddleRate(LocalDate date) {
        return currencyRepository
                .findByType(Currency.Type.NBS_MIDDLE_RATE)
                .map(currency -> new CurrencyDto(currency.getValue(),
                        currency.getDate(),
                        currency.getType().name()));
    }

    public Mono<CurrencyDto> scrapeNbpEurUsdRate() {
        return nbpEurToUsdScraper
                .scrape()
                .flatMap(currencyDto -> currencyRepository.save(Currency.builder()
                        .id(UUID.randomUUID())
                        .date(currencyDto.date())
                        .value(currencyDto.value())
                        .type(Currency.Type.NBP_EUR_USD)
                        .build()))
                .map(currency -> new CurrencyDto(currency.getValue(),
                        currency.getDate(),
                        currency.getType().name()));
    }

    public Mono<CurrencyDto> scrapeNbsMiddleRate() {
        return nbsMiddleRateScraper
                .scrape()
                .flatMap(currencyDto -> currencyRepository.save(Currency.builder()
                        .id(UUID.randomUUID())
                        .date(currencyDto.date())
                        .value(currencyDto.value())
                        .type(Currency.Type.NBS_MIDDLE_RATE)
                        .build()))
                .map(currency -> new CurrencyDto(currency.getValue(),
                        currency.getDate(),
                        currency.getType().name()));
    }
}
