package com.ps.kor.business.report;

import com.ps.kor.business.report.impl.CountSumByTypeReport;
import com.ps.kor.business.report.impl.CountSumByTypeRoleReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportFactory {

  /**
   * We have to let Spring manage the instances of the services.
   */
  @Autowired
  private CountSumByTypeReport countSumByTypeReport;

  @Autowired
  private CountSumByTypeRoleReport countSumByTypeRoleReport;

  public enum ReportType {
    CountAndSumByType,
    CountAndSumByTypeRole
  }

  public Report getInstance(ReportType type) {
    if (type.equals(ReportType.CountAndSumByType)) {
      return countSumByTypeReport;
    }
    if (type.equals(ReportType.CountAndSumByTypeRole)) {
      return countSumByTypeRoleReport;
    }

    return null;
  }

}
