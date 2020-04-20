package com.ps.kor.controller;

import com.ps.kor.business.RoleLogic;
import com.ps.kor.business.util.message.BusinessMessage;
import com.ps.kor.controller.response.ResponseEntityFactory;
import com.ps.kor.entity.BudgetRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class RoleController {

  @Autowired
  private RoleLogic roleLogic;

  @PostMapping("/api/role")
  public ResponseEntity create(
      @RequestHeader("authorization") String token,
      @RequestBody BudgetRole budgetRole,
      @RequestParam("budget") UUID budgetId
      ) {

    BusinessMessage message = roleLogic.createRole(token, budgetId, budgetRole);

    return ResponseEntityFactory.createResponseFromBusinessMessage(message);
  }

  @GetMapping("/api/role")
  public ResponseEntity getAllUserRoles(
      @RequestHeader("authorization") String token) {

    BusinessMessage message = roleLogic.retrieveRolesForUser(token);

    return ResponseEntityFactory.createResponseFromBusinessMessage(message);
  }
}
