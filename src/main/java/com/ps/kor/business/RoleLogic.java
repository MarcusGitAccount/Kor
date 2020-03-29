package com.ps.kor.business;

import com.ps.kor.business.auth.AuthenticationUtils;
import com.ps.kor.business.util.message.BusinessMesageType;
import com.ps.kor.business.util.message.BusinessMessage;
import com.ps.kor.business.validation.RoleLogicValidation;
import com.ps.kor.entity.BudgetRole;
import com.ps.kor.entity.DailyBudget;
import com.ps.kor.entity.User;
import com.ps.kor.repo.BudgetRoleRepo;
import com.ps.kor.repo.DailyBudgetRepo;
import com.ps.kor.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RoleLogic {

  @Autowired
  private UserRepo userRepo;

  @Autowired
  private DailyBudgetRepo dailyBudgetRepo;

  @Autowired
  private BudgetRoleRepo budgetRoleRepo;

  @Autowired
  private AuthenticationUtils authUtils;

  @Autowired
  private RoleLogicValidation roleLogicValidation;

  /**
   * Logic to create a role by an authenticated user with a given token,
   * for a given budget.
   * @param token
   * @param budgetId
   * @param role
   * @return
   */
  public BusinessMessage createRole(String token, UUID budgetId, BudgetRole role) {
    User user = authUtils.getTokenUser(token);

    if (user == null) {
      return new BusinessMessage(BusinessMesageType.USER_NOT_FOUND);
    }
    if (budgetId == null) {
      return new BusinessMessage(BusinessMesageType.BUDGET_NOT_FOUND);
    }

    DailyBudget budget = dailyBudgetRepo.findById(budgetId).orElse(null);
    if (budget == null) {
      return new BusinessMessage(BusinessMesageType.BUDGET_NOT_FOUND);
    }
    if (budgetRoleRepo.findByUserAndBudget(budget, role.getUser()).orElse(null) != null) {
      return new BusinessMessage(BusinessMesageType.ALREADY_HAS_ROLE);
    }

    BudgetRole initiatorRole = budgetRoleRepo.findByUserAndBudget(budget, user).orElse(null);
    BusinessMessage<Boolean> validationMessage = roleLogicValidation.createRoleValidation(initiatorRole, role);

    if (!validationMessage.getData()) {
      validationMessage.setData(null);
      return validationMessage;
    }

//    User beneficiary = userRepo.findById(role.getUser().getId()).orElse(null);

    role.setEnabled(true);
    role.setDailyBudget(budget);
    role.setCreator(initiatorRole);

//    budget.getBudgetRoleList().add(role);
//    if (beneficiary != null) {
//      beneficiary.getRoleList().add(role);
//    }
//
//    userRepo.save(beneficiary);
//    dailyBudgetRepo.save(budget);



    role = budgetRoleRepo.save(role);
    if (role == null) {
      return new BusinessMessage(BusinessMesageType.ROLE_CREATION_FAIL);
    }

    role.setCreator(null);
    role.setDailyBudget(null);
    return new BusinessMessage(role, BusinessMesageType.ROLE_CREATION_SUCCESS);
  }
}
