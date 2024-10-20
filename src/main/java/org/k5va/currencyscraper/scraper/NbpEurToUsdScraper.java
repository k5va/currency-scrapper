package org.k5va.currencyscraper.scraper;

import lombok.extern.slf4j.Slf4j;
import org.k5va.currencyscraper.error.ScraperException;
import org.k5va.currencyscraper.parser.NbpEurUsdParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;

@Component
@Slf4j
public class NbpEurToUsdScraper implements Scraper<String> {

    public static final int TIMEOUT_SECONDS = 5;
    public static final int RESULT_SCALE = 2;

    private final NbpEurUsdParser parser;

    private final WebClient webClient;

    public NbpEurToUsdScraper(NbpEurUsdParser parser,
                              WebClient.Builder webClientBuilder,
                              @Value("${app.currency.nbp.url}") String npbUrl) {

        Assert.hasText(npbUrl, "NBP URL cannot be empty");

        this.parser = parser;
        this.webClient = webClientBuilder.baseUrl(npbUrl).build();
    }

    @Override
    public Mono<String> scrape() {
        return scrape(LocalDate.now());
    }

    @Override
    public Mono<String> scrape(LocalDate date) {
        log.info("Scraping NBP currency rates");
        return webClient.get()
                .retrieve()
                .bodyToMono(String.class)
                .map(parser::parse)
                .log()
                .map(currencyDto -> currencyDto.eur().divide(currencyDto.usd(), RoundingMode.HALF_UP))
                .log()
                .map(result -> result.setScale(RESULT_SCALE, RoundingMode.HALF_UP).toPlainString())
                .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                .switchIfEmpty(Mono.error(new ScraperException("Empty response from NBP")))
                .onErrorMap(Exception.class, ScraperException::new);
    }
}
