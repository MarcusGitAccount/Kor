package com.ps.kor.business;

import com.ps.kor.business.auth.AuthenticationUtils;
import com.ps.kor.business.report.Report;
import com.ps.kor.business.report.ReportData;
import com.ps.kor.business.report.ReportFactory;
import com.ps.kor.business.util.message.BusinessMesageType;
import com.ps.kor.business.util.message.BusinessMessage;
import com.ps.kor.business.validation.DailyBudgetLogicValidation;
import com.ps.kor.entity.BudgetRole;
import com.ps.kor.entity.DailyBudget;
import com.ps.kor.entity.User;
import com.ps.kor.repo.BudgetRoleRepo;
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

  @Autowired
  private DailyBudgetLogicValidation budgetLogicValidation;

  /**
   * @param token - auth token
   * @param budgetId - budget to which the expenditure is tied
   * @param reportType
   * @return
   */
  public BusinessMessage<ReportData> getReportDataForBudget(UUID budgetId, String token,
                                                            ReportFactory.ReportType reportType) {

    DailyBudget budget = dailyBudgetRepo.findById(budgetId).orElse(null);
    if (budget == null) {
      return new BusinessMessage<>(BUDGET_NOT_FOUND);
    }

    BusinessMessage<BudgetRole> budgetRoleValidation = budgetLogicValidation
        .validateUserHasBudgetRole(budget, token);

    if (budgetRoleValidation.getData() == null) {
      return new BusinessMessage<ReportData>(null, budgetRoleValidation.getType());
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
