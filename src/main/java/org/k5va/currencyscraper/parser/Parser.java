package org.k5va.currencyscraper.parser;

public interface Parser<T> {

    T parse(String rawData);
}
