package com.ps.kor.business;

import com.ps.kor.business.report.Report;
import com.ps.kor.business.report.ReportData;
import com.ps.kor.business.report.ReportFactory;
import com.ps.kor.business.util.message.BusinessMessage;
import com.ps.kor.entity.DailyBudget;
import com.ps.kor.repo.DailyBudgetRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.ps.kor.business.util.message.BusinessMesageType.*;

@Service
public class ReportLogic {

  @Autowired
  private ReportFactory reportFactory;

  @Autowired
  private DailyBudgetRepo dailyBudgetRepo;

  public BusinessMessage<ReportData> getReportDataForBudget(UUID budgetId,
                                                            ReportFactory.ReportType reportType) {

    DailyBudget budget = dailyBudgetRepo.findById(budgetId).orElse(null);

    if (budget == null) {
      return new BusinessMessage<>(BUDGET_NOT_FOUND);
    }

    Report report = reportFactory.getInstance(reportType);

    if (report == null) {
      return new BusinessMessage<>(REPORT_TYPE_NOT_FOUND);
    }

    ReportData data;

    report.generate(budget);
    data = report.getReportData();

    return new BusinessMessage(data, REPORT_CREATED);
  }
}
