package com.ps.kor.controller;

import com.ps.kor.business.UserLogic;
import com.ps.kor.business.util.message.BusinessMessage;
import com.ps.kor.controller.response.ResponseEntityFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {

  @Autowired
  private UserLogic userLogic;

  @GetMapping("/api/user")
  @CrossOrigin(origins = "*")
  public ResponseEntity getAllNotPartOfBudget(
      @RequestHeader("authorization") String token,
      @RequestParam("budget") UUID budgetId) {

    BusinessMessage message = userLogic.getAllUsersNotPartOfBudget(token, budgetId);

    return ResponseEntityFactory.createResponseFromBusinessMessage(message);
  }
}
