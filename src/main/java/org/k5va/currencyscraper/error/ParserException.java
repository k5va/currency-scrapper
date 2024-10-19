package org.k5va.currencyscraper.error;

public class ParserException extends RuntimeException {

    public ParserException(String message) {
        super(message);
    }

    public ParserException(Throwable cause) {
        super(cause);
    }
}
