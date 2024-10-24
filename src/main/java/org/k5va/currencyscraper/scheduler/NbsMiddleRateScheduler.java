package org.k5va.currencyscraper.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.k5va.currencyscraper.service.CurrencyService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class NbsMiddleRateScheduler {
    private final CurrencyService currencyService;

//    @Scheduled(cron = "0 0 * * * *")
    @Scheduled(cron = "0 * * * * *")
    public void run() {
        log.info("Running NBS middle rate scraper");
        currencyService
                .scrapeNbsMiddleRate()
                .log()
                .subscribe();
    }
}
