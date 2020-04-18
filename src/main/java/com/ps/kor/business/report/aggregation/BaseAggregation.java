package com.ps.kor.business.report.aggregation;

import java.util.List;

public interface BaseAggregation {

  /**
   * Return the name of columns.
   * @return
   */
  List<String> getHeader();

  /**
   * Returns the objects fields mapped as list of strings.
   * @return
   */
  List<String> getData();
}
