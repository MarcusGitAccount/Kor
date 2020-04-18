package com.ps.kor.controller;

import com.ps.kor.business.ReportLogic;
import com.ps.kor.business.report.ReportFactory;
import com.ps.kor.business.util.message.BusinessMessage;
import com.ps.kor.controller.response.ResponseEntityFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class ReportController {

  @Autowired
  private ReportLogic reportLogic;

  @GetMapping("/api/report")
  public ResponseEntity create(
      @RequestParam("type") ReportFactory.ReportType type,
      @RequestParam("budget") UUID budgetId
  ) {
    BusinessMessage message = reportLogic.getReportDataForBudget(budgetId, type);

    return ResponseEntityFactory.createResponseFromBusinessMessage(message);
  }
}
