package com.ps.kor.entity.util;

import java.util.Arrays;
import java.util.Currency;
import java.util.Set;
import java.util.TreeSet;

import lombok.extern.java.Log;

@Log
public class CurrencyFactory {

  private static final String defaultCurrencyCode = "RON";

  private static final Set<String> supportedCurrencyCodes = new TreeSet<>();

  static {
    supportedCurrencyCodes.addAll(Arrays.asList(
       "RON", "EUR", "USD"
    ));
  }

  public static Currency getCurrency(String code) {
    if (!supportedCurrencyCodes.contains(code)) {
      log.info(String.format("Currency %s not supported by the system. Will default to %s.",
          code, defaultCurrencyCode));
      code = defaultCurrencyCode;
    }

    return Currency.getInstance(code);
  }
}
