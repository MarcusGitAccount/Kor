package com.ps.kor.business.validation;

import com.ps.kor.business.auth.AuthenticationUtils;
import com.ps.kor.business.util.message.BusinessMesageType;
import com.ps.kor.business.util.message.BusinessMessage;
import com.ps.kor.entity.BudgetRole;
import com.ps.kor.entity.DailyBudget;
import com.ps.kor.entity.User;
import com.ps.kor.repo.BudgetRoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DailyBudgetLogicValidation {


  @Autowired
  private BudgetRoleRepo budgetRoleRepo;

  @Autowired
  private AuthenticationUtils authUtils;

  /**
   * Given a user identified by an auth token, check whether it is authorised
   * to perform actions on it.
   *
   * @param budget
   * @param token
   * @return null if operation is not authorised, otherwise the Role afferent to the
   *         current user inside the budget
   */
  public BusinessMessage<BudgetRole> validateUserHasBudgetRole(DailyBudget budget, String token) {
    User user = authUtils.getTokenUser(token);

    if (user == null) {
      return new BusinessMessage(BusinessMesageType.USER_NOT_FOUND);
    }
    BudgetRole initiatorRole = budgetRoleRepo.findByUserAndBudget(budget, user).orElse(null);

    if (initiatorRole == null) {
      return new BusinessMessage(BusinessMesageType.UNAUTHORIZED_OPERATION);
    }

    return new BusinessMessage(initiatorRole, null);
  }

}
