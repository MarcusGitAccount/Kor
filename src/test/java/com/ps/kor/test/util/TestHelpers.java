package com.ps.kor.test.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Nullable;


@Log4j2
public class TestHelpers {

  public static final String EMPTY_JSON_OBJ = "{}";

  /**
   * @param str
   * @return the string argument enclosed by "
   */
  public static String getEnclosedString(String str) {
    return String.format("\"%s\"", str);
  }

  public static String convert2JsonString(Object obj) {

    try {
      ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

      return ow.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      log.error("Error ocurred while trying to convert object to json. ",
          obj.toString(), e.getMessage());
      return EMPTY_JSON_OBJ;
    }
  }
}
