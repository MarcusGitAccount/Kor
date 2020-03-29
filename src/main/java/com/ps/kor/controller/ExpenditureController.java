package com.ps.kor.controller;

import com.ps.kor.business.ExpenditureLogic;
import com.ps.kor.business.util.message.BusinessMessage;
import com.ps.kor.controller.response.ResponseEntityFactory;
import com.ps.kor.entity.Expenditure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class ExpenditureController {

  @Autowired
  private ExpenditureLogic expenditureLogic;

  @PostMapping("/api/expenditure")
  public ResponseEntity create(
      @RequestHeader("authorization") String token,
      @RequestBody Expenditure expenditure,
      @RequestParam("budget") UUID budgetId
  ) {
    BusinessMessage message = expenditureLogic.createExpendtiure(token, budgetId, expenditure);

    return ResponseEntityFactory.createResponseFromBusinessMessage(message);
  }
}
