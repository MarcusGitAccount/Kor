package com.ps.kor.controller;

import com.ps.kor.controller.response.ResponseEntityFactory;
import com.ps.kor.entity.BudgetRole;
import com.ps.kor.entity.DailyBudget;
import com.ps.kor.entity.User;
import com.ps.kor.repo.BudgetRoleRepo;
import com.ps.kor.repo.DailyBudgetRepo;
import com.ps.kor.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.UUID;

@RestController
public class BudgetController {

  @Autowired
  private UserRepo userRepo;

  @Autowired
  private DailyBudgetRepo dailyBudgetRepo;

  @Autowired
  private BudgetRoleRepo budgetRoleRepo;

  @PostMapping("/api/budget")
  public ResponseEntity create(
      @RequestHeader("authorization") UUID creatorId,
      @RequestBody DailyBudget dailyBudget
      ) {

    // TODO: validation(if supported currency too).
    User user = userRepo.findById(creatorId).orElse(null);

    if (user == null) {
      return ResponseEntityFactory.buildErrorResponse(
          null, "User not found.", HttpStatus.NOT_FOUND);
    }

    BudgetRole role = new BudgetRole();

    role.setRoleType(BudgetRole.BudgetRoleType.CREATOR);
    role.setCreator(null);
    role.setEnabled(true);
    role.setUser(user);
    role.setDailyBudgetList(new LinkedList<>());
    role.getDailyBudgetList().add(dailyBudget);

    // prevent user setting those
    dailyBudget.setBudgetRoleList(null);
    dailyBudget.setExpenditureList(null);
    if (dailyBudget.getDate() == null) {
      dailyBudget.setDate(new Date());
    }

    if (dailyBudgetRepo.save(dailyBudget) == null) {
      return ResponseEntityFactory.buildErrorResponse(
          null, "Could not create daily budget.", HttpStatus.BAD_REQUEST);
    }
    if (budgetRoleRepo.save(role) == null) {
      return ResponseEntityFactory.buildErrorResponse(
          null, "Could not create role.", HttpStatus.BAD_REQUEST);
    }

    return ResponseEntityFactory.buildSuccesResponse(
        dailyBudget, "Daily budget and creator role created.", HttpStatus.CREATED);
  }
}
