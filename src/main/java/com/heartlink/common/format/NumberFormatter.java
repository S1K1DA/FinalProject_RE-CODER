package com.heartlink.common.format;


import org.springframework.format.Formatter;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class NumberFormatter implements Formatter<Number> {
    private final NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());

    @Override
    public Number parse(String text, Locale locale) throws ParseException {
        return numberFormat.parse(text);
    }

    @Override
    public String print(Number number, Locale locale) {
        return numberFormat.format(number);
    }
}