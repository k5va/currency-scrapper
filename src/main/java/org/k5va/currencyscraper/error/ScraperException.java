package org.k5va.currencyscraper.error;

public class ScraperException extends RuntimeException {
    public ScraperException(String message) {
        super(message);
    }

    public ScraperException(Throwable cause) {
        super(cause);
    }
}
