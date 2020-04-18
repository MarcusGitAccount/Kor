package com.ps.kor.business;

import com.ps.kor.business.auth.AuthenticationUtils;
import com.ps.kor.business.util.message.BusinessMesageType;
import com.ps.kor.business.util.message.BusinessMessage;
import com.ps.kor.business.validation.ExpenditureLogicValidation;
import com.ps.kor.entity.BudgetRole;
import com.ps.kor.entity.DailyBudget;
import com.ps.kor.entity.Expenditure;
import com.ps.kor.entity.User;
import com.ps.kor.repo.BudgetRoleRepo;
import com.ps.kor.repo.DailyBudgetRepo;
import com.ps.kor.repo.ExpenditureRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ExpenditureLogic {

  @Autowired
  private ExpenditureRepo expenditureRepo;

  @Autowired
  private DailyBudgetRepo dailyBudgetRepo;

  @Autowired
  private AuthenticationUtils authUtils;

  @Autowired
  private BudgetRoleRepo budgetRoleRepo;

  @Autowired
  private ExpenditureLogicValidation expenditureLogicValidation;

  /**
   * @param token - auth token
   * @param budgetId - budget to which the expenditure is tied
   * @param expenditure
   * @return
   */
  public BusinessMessage createExpendtiure(String token, UUID budgetId,
                                           Expenditure expenditure) {
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

    expenditure.setDailyBudget(budget);

    BudgetRole initiatorRole = budgetRoleRepo.findByUserAndBudget(budget, user).orElse(null);
    BusinessMessage<Boolean> validationMessage = expenditureLogicValidation
        .createExpenditureValidation(initiatorRole, expenditure);

    if (!validationMessage.getData()) {
      validationMessage.setData(null);
      return validationMessage;
    }

    expenditure.setBudgetRole(initiatorRole);
    expenditure = expenditureRepo.save(expenditure);
    if (expenditure == null) {
      return new BusinessMessage(BusinessMesageType.EXPENDITURE_CREATION_FAIL);
    }

    expenditure.setBudgetRole(null);
    expenditure.setDailyBudget(null);
    return new BusinessMessage(expenditure, BusinessMesageType.EXPENDITURE_CREATION_SUCCESS);
  }
}
