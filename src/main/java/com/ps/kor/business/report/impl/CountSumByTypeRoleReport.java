package com.ps.kor.business.report.impl;

import com.ps.kor.business.report.Report;
import com.ps.kor.business.report.ReportData;
import com.ps.kor.business.report.aggregation.BaseAggregation;
import com.ps.kor.business.report.aggregation.CountSumByTypeRoleAggregation;
import com.ps.kor.entity.DailyBudget;
import com.ps.kor.repo.ExpenditureRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CountSumByTypeRoleReport implements Report {

  @Autowired
  private ExpenditureRepo expenditureRepo;

  private ReportData data;

  public CountSumByTypeRoleReport() {
    data = null;
  }

  @Override
  public void generate(DailyBudget budget) {
    List<CountSumByTypeRoleAggregation> dataFromRepo = expenditureRepo.countAndSumByTypeAndRole(budget);

    if (dataFromRepo.size() == 0) {
      data = new ReportData();
      return;
    }

    BaseAggregation first = dataFromRepo.get(0);

    data = new ReportData(first.getHeader(),
        dataFromRepo
            .stream()
            .map(BaseAggregation::getData)
            .collect(Collectors.toList()));
  }

  @Override
  public ReportData getReportData() {
    return data;
  }
}