package com.ps.kor.business;

import com.ps.kor.business.util.message.BusinessMesageType;
import com.ps.kor.business.util.message.BusinessMessage;
import com.ps.kor.business.validation.DailyBudgetLogicValidation;
import com.ps.kor.entity.BudgetRole;
import com.ps.kor.entity.DailyBudget;
import com.ps.kor.entity.User;
import com.ps.kor.repo.DailyBudgetRepo;
import com.ps.kor.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.ps.kor.business.util.message.BusinessMesageType.DATA_QUERIED;

@Service
public class UserLogic {

  @Autowired
  private UserRepo userRepo;

  @Autowired
  private DailyBudgetRepo dailyBudgetRepo;

  @Autowired
  private DailyBudgetLogicValidation budgetLogicValidation;


  /**
   * @param token - auth
   * @param budgetId
   * @return
   */
  public BusinessMessage getAllUsersNotPartOfBudget(String token, UUID budgetId) {
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
      return budgetRoleValidation;
    }

    List<User> users = userRepo.findAllNotPartOfBudget(budget);

    return new BusinessMessage<>(users, DATA_QUERIED);
  }
}
