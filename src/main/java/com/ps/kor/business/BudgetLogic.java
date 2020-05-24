package com.ps.kor.business;

import com.ps.kor.business.auth.AuthenticationUtils;
import com.ps.kor.business.util.message.BusinessMesageType;
import com.ps.kor.business.util.message.BusinessMessage;
import com.ps.kor.business.validation.DailyBudgetLogicValidation;
import com.ps.kor.entity.*;
import com.ps.kor.entity.enums.BudgetRoleType;
import com.ps.kor.repo.BudgetRoleRepo;
import com.ps.kor.repo.DailyBudgetRepo;
import com.ps.kor.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.ps.kor.business.util.message.BusinessMesageType.*;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

@Service
public class BudgetLogic {

  @Autowired
  private UserRepo userRepo;

  @Autowired
  private DailyBudgetRepo dailyBudgetRepo;

  @Autowired
  private BudgetRoleRepo budgetRoleRepo;

  @Autowired
  private AuthenticationUtils authenticationUtils;

  @Autowired
  private DailyBudgetLogicValidation budgetLogicValidation;

  /**
   * Persists a DailyBudget entity and a new CREATOR role
   * for the given (budget, user) pair.
   * @param token - jwt used to authenticate and identify the user
   *              making the request
   * @param dailyBudget - entity to be persisted
   * @return the persisted budget entity
   */
  public BusinessMessage create(String token, DailyBudget dailyBudget) {
    User user = authenticationUtils.getTokenUser(token);

    if (user == null) {
      return new BusinessMessage(USER_NOT_FOUND_AUTH);
    }

    BudgetRole role = new BudgetRole();

    role.setRoleType(BudgetRoleType.CREATOR);
    role.setEnabled(true);
    role.setUser(user);
    role.setDailyBudget(dailyBudget);

    dailyBudget.setBudgetRoleList(Collections.singletonList(role));
    if (dailyBudget.getDate() == null) {
      dailyBudget.setDate(new Date());
    }
    dailyBudget = dailyBudgetRepo.save(dailyBudget);
    if (dailyBudget == null) {
      return new BusinessMessage(BUDGET_CREATION_FAIL);
    }

    role = budgetRoleRepo.save(role);
    if (role == null) {
      return new BusinessMessage(ROLE_CREATION_FAIL);
    }

    user.getRoleList().add(role);
    user = userRepo.save(user);

    // Prevent infinite loop
    user.setRoleList(null);
    role.setDailyBudget(null);
    return new BusinessMessage(dailyBudget, BUDGET_CREATION_SUCCESS);
  }

  public BusinessMessage<DailyBudget> retrieveById(String token, UUID budgetId) {
    if (budgetId == null) {
      return new BusinessMessage(BusinessMesageType.BUDGET_NOT_FOUND);
    }

    DailyBudget budget = dailyBudgetRepo.findById(budgetId).orElse(null);
    if (budget == null) {
      return new BusinessMessage(BusinessMesageType.BUDGET_NOT_FOUND);
    }

    BusinessMessage<BudgetRole> budgetRoleValidation = budgetLogicValidation
        .validateUserHasBudgetRole(budget, token);

    if (budgetRoleValidation.getData() == null) {
      return new BusinessMessage<>(null, budgetRoleValidation.getType());
    }

    for (Expenditure expenditure: budget.getExpenditureList()) {
      expenditure.getBudgetRole().setDailyBudget(null);
    }
    for (BudgetRole role: budget.getBudgetRoleList()) {
      role.setDailyBudget(null);
    }
    for (Alert alert: budget.getAlertList()) {
      alert.setDailyBudget(null);
    }

    return new BusinessMessage<>(budget, DATA_QUERIED);
  }

}
