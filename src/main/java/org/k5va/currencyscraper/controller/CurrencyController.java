package org.k5va.currencyscraper.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.k5va.currencyscraper.service.CurrencyService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/currency", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping(path = "/nbp")
    public Mono<ResponseEntity<String>> getNbpEurUsdRate() {
        log.info("getNbpEurUsdRate");
        return currencyService.getNbpEurUsdRate().map(ResponseEntity::ok);
    }

    @GetMapping(path = "/nbs")
    public Mono<ResponseEntity<String>> getNbsMiddleRate(
            @RequestParam @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate date) {

        log.info("getNbsMiddleRate: {}", date);
        return currencyService
                .getNbsMiddleRate(Optional.ofNullable(date).orElse(LocalDate.now()))
                .map(ResponseEntity::ok);
    }
}
