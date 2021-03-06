package com.ps.kor.controller;

import com.ps.kor.business.BudgetLogic;

import com.ps.kor.business.util.message.BusinessMessage;
import com.ps.kor.controller.response.ResponseEntityFactory;
import com.ps.kor.entity.DailyBudget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controller to manipulate and manage
 * budget operations
 */
@RestController
public class BudgetController {

  @Autowired
  private BudgetLogic budgetLogic;

  @PostMapping("/api/budget")
  public ResponseEntity create(
      @RequestHeader("authorization") String token,
      @RequestBody DailyBudget dailyBudget
      ) {

    BusinessMessage message = budgetLogic.create(token, dailyBudget);

    return ResponseEntityFactory.createResponseFromBusinessMessage(message);
  }

  @GetMapping("/api/budget")
  @CrossOrigin(origins = "*")
  public ResponseEntity getById(
      @RequestHeader("authorization") String token,
      @RequestParam("budget") UUID budgetId
  ) {

    BusinessMessage message = budgetLogic.retrieveById(token, budgetId);

    return ResponseEntityFactory.createResponseFromBusinessMessage(message);
  }
}
