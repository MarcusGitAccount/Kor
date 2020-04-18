package com.ps.kor.business.report;

import com.ps.kor.entity.DailyBudget;

public interface Report {

  void generate(DailyBudget budget);

  ReportData getReportData();
}
