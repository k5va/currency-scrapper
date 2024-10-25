package org.k5va.currencyscraper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CurrencyscraperApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurrencyscraperApplication.class, args);
	}

}
