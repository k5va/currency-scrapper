package org.k5va.currencyscraper.scraper;

import lombok.extern.slf4j.Slf4j;
import org.k5va.currencyscraper.dto.CurrencyDto;
import org.k5va.currencyscraper.entity.Currency;
import org.k5va.currencyscraper.error.ScraperException;
import org.k5va.currencyscraper.parser.BtcUsdRateParser;
import org.k5va.currencyscraper.parser.NbsMiddleRateParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class BtcUsdRateScraper implements Scraper<CurrencyDto> {

    public static final int TIMEOUT_SECONDS = 5;

    private final BtcUsdRateParser parser;

    private final WebClient webClient;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public BtcUsdRateScraper(BtcUsdRateParser parser,
                             WebClient.Builder webClientBuilder,
                             @Value("${app.currency.btc.url}") String nbsUrl) {

        Assert.hasText(nbsUrl, "BTC URL cannot be empty");

        this.parser = parser;
        this.webClient = webClientBuilder.baseUrl(nbsUrl).build();
    }

    @Override
    public Mono<CurrencyDto> scrape() {
        return scrape(LocalDate.now());
    }

    @Override
    public Mono<CurrencyDto> scrape(LocalDate date) {
        log.info("Scraping BTC currency middle rates on: {}", date);

        return webClient.get()
                .retrieve()
                .bodyToMono(String.class)
                .map(parser::parse)
                .log()
                .map(value -> new CurrencyDto(value, date, Currency.Type.BTC_USD.name()))
                .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                .switchIfEmpty(Mono.error(new ScraperException("Empty response from BTC")))
                .onErrorMap(Exception.class, ScraperException::new);
    }
}
