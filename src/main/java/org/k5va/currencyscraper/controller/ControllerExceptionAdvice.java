package org.k5va.currencyscraper.controller;

import lombok.extern.slf4j.Slf4j;
import org.k5va.currencyscraper.error.ScraperException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ControllerExceptionAdvice {

    @ExceptionHandler(ScraperException.class)
    public ResponseEntity<String> handle(ScraperException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.internalServerError().body("Error while trying to scrape currency");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handle(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.internalServerError().body("Internal server error");
    }
}
