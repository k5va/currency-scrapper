package org.k5va.currencyscraper.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.k5va.currencyscraper.dto.CurrencyDto;
import org.k5va.currencyscraper.entity.Currency;
import org.k5va.currencyscraper.service.CurrencyService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(path = "/api/v1/currency", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping(path = "/nbp")
    public Flux<CurrencyDto> getNbpEurUsdRate() {
        log.info("getNbpEurUsdRate");
        return currencyService.getCurrencyRates(Currency.Type.NBP_EUR_USD);
    }

    @GetMapping(path = "/nbs")
    public Flux<CurrencyDto> getNbsMiddleRate() {
        log.info("getNbsMiddleRate");
        return currencyService.getCurrencyRates(Currency.Type.NBS_MIDDLE_RATE);
    }

    @GetMapping(path = "/btc")
    public Flux<CurrencyDto> getBtcUsdRate() {
        log.info("getBtcUsdRate");
        return currencyService.getCurrencyRates(Currency.Type.BTC_USD);
    }
}
