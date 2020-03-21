package com.ps.kor.entity.util;

import java.util.Arrays;
import java.util.Currency;
import java.util.Set;
import java.util.TreeSet;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class CurrencyFactory {

  private static final String defaultCurrencyCode = "RON";

  private static final Set<String> supportedCurrencyCodes = new TreeSet<>();

  /**
   * Init the set of supported currencies by the system.
   */
  static {
    supportedCurrencyCodes.addAll(Arrays.asList(
       "RON", "EUR", "USD"
    ));
  }

  /**
   * Given the 3 letter representation for a currency
   * return the afferent Java Currency implementation.
   *
   * @param code
   * @return the Currency object for the given code
   *         or the default Currency if the requested one
   *         is not supported
   */
  public static Currency getCurrency(String code) {
    if (!supportedCurrencyCodes.contains(code)) {
      log.info(String.format("Currency %s not supported by the system. Will default to %s.",
          code, defaultCurrencyCode));
      code = defaultCurrencyCode;
    }

    return Currency.getInstance(code);
  }
}
