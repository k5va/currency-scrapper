package org.k5va.currencyscraper.scraper;

import lombok.extern.slf4j.Slf4j;
import org.k5va.currencyscraper.entity.Currency;
import org.k5va.currencyscraper.error.ScraperException;
import org.k5va.currencyscraper.dto.CurrencyDto;
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
public class NbsMiddleRateScraper implements Scraper<CurrencyDto> {

    public static final int TIMEOUT_SECONDS = 5;

    private final NbsMiddleRateParser parser;

    private final WebClient webClient;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public NbsMiddleRateScraper(NbsMiddleRateParser parser,
                                WebClient.Builder webClientBuilder,
                                @Value("${app.currency.nbs.url}") String nbsUrl) {

        Assert.hasText(nbsUrl, "NBS URL cannot be empty");

        this.parser = parser;
        this.webClient = webClientBuilder.baseUrl(nbsUrl).build();
    }

    @Override
    public Mono<CurrencyDto> scrape() {
        return scrape(LocalDate.now());
    }

    @Override
    public Mono<CurrencyDto> scrape(LocalDate date) {
        log.info("Scraping NBS currency middle rates on: {}", date);

        return webClient.get()
                .uri("?datum=%s.".formatted(date.format(dateFormatter)))
                .retrieve()
                .bodyToMono(String.class)
                .map(parser::parse)
                .log()
                .map(value -> new CurrencyDto(value, date, Currency.Type.NBS_MIDDLE_RATE.name()))
                .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                .switchIfEmpty(Mono.error(new ScraperException("Empty response from NBS")))
                .onErrorMap(Exception.class, ScraperException::new);
    }
}
