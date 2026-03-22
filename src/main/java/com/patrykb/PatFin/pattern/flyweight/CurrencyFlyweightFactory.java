
package com.patrykb.PatFin.pattern.flyweight;

import java.util.HashMap;
import java.util.Map;



// FABRYKA PYŁKÓW - dpowiada za współdzielenie obiektów


public class CurrencyFlyweightFactory {
    private static final Map<String, CurrencyFlyweight> cache = new HashMap<>();

    public static CurrencyFlyweight getCurrency(String code) {
        return cache.computeIfAbsent(code, c -> switch (c) {
            case "PLN" -> new CurrencyFlyweight("PLN", "zł", "Polski Złoty");
            case "USD" -> new CurrencyFlyweight("USD", "$", "Dolar amerykański");
            case "EUR" -> new CurrencyFlyweight("EUR", "€", "Euro");
            default -> new CurrencyFlyweight(c, "", "Nieznana waluta");
        });
    }
}