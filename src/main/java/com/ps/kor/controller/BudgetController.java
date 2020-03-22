package com.ps.kor.controller;

import com.ps.kor.auth.AuthenticationUtils;
import com.ps.kor.controller.response.ResponseEntityFactory;
import com.ps.kor.entity.BudgetRole;
import com.ps.kor.entity.DailyBudget;
import com.ps.kor.entity.User;
import com.ps.kor.entity.enums.BudgetRoleType;
import com.ps.kor.repo.BudgetRoleRepo;
import com.ps.kor.repo.DailyBudgetRepo;
import com.ps.kor.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.LinkedList;

/**
 * Controller to manipulate and manage
 * budget operations
 */
@RestController
public class BudgetController {

  @Autowired
  private UserRepo userRepo;

  @Autowired
  private DailyBudgetRepo dailyBudgetRepo;

  @Autowired
  private BudgetRoleRepo budgetRoleRepo;

  @Autowired
  private AuthenticationUtils authenticationUtils;

  /**
   * Persists a DailyBudget entity and a new CREATOR role
   * for the given (budget, user) pair.
   * @param token - jwt used to authenticate and identify the user
   *              making the request
   * @param dailyBudget - entity to be persisted
   * @return the persisted budget entity
   */
  @PostMapping("/api/budget")
  public ResponseEntity create(
      @RequestHeader("authorization") String token,
      @RequestBody DailyBudget dailyBudget
      ) {

    // TODO: validation(if supported currency too).
    User user = authenticationUtils.getTokenUser(token);

    if (user == null) {
      return ResponseEntityFactory.buildErrorResponse(
          null, "User not found.", HttpStatus.NOT_FOUND);
    }
    if (dailyBudget.getDate() == null) {
      dailyBudget.setDate(new Date());
    }
    if (dailyBudgetRepo.save(dailyBudget) == null) {
      return ResponseEntityFactory.buildErrorResponse(
          null, "Could not create daily budget.", HttpStatus.BAD_REQUEST);
    }

    BudgetRole role = new BudgetRole();

    role.setRoleType(BudgetRoleType.CREATOR);
    role.setCreator(null);
    role.setEnabled(true);
    role.setUser(user);

    role.setDailyBudgetList(new LinkedList<>());
    role.getDailyBudgetList().add(dailyBudget);

    user.getRoleList().add(role);
    userRepo.save(user);

    if (budgetRoleRepo.save(role) == null) {
      return ResponseEntityFactory.buildErrorResponse(
          null, "Could not create role.", HttpStatus.BAD_REQUEST);
    }

    dailyBudget.setBudgetRoleList(new LinkedList<>());
    dailyBudget.getBudgetRoleList().add(role);

    user.setRoleList(null);
    return ResponseEntityFactory.buildSuccesResponse(
        dailyBudget, "Daily budget and creator role created.", HttpStatus.CREATED);
  }
}
