package com.ps.kor.business.validation;

import com.ps.kor.business.alert.util.AlertDispatcher;
import com.ps.kor.business.util.message.BusinessMesageType;
import com.ps.kor.business.util.message.BusinessMessage;
import com.ps.kor.entity.Alert;
import com.ps.kor.entity.BudgetRole;
import com.ps.kor.entity.DailyBudget;
import com.ps.kor.entity.Expenditure;
import com.ps.kor.entity.enums.BudgetRoleType;
import com.ps.kor.repo.DailyBudgetRepo;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class ExpenditureLogicValidation extends AlertDispatcher {

  @Autowired
  private DailyBudgetRepo dailyBudgetRepo;

  public BusinessMessage<Boolean> createExpenditureValidation(
      BudgetRole initiator, Expenditure expenditure) {

    DailyBudget budget = expenditure.getDailyBudget();
    final int remaining = budget.getImposedLimit() - dailyBudgetRepo.findUsedBudget(budget);

    if (remaining < expenditure.getAmount()) {
      dispatchLowBudgetAlert(expenditure, remaining);
      return new BusinessMessage(false, BusinessMesageType.NO_REMAINING_BUDGET);
    }
    if (initiator.getRoleType().rank < BudgetRoleType.EDIT.rank) {
      return new BusinessMessage(false, BusinessMesageType.UNAUTHORIZED_OPERATION);
    }

    return new BusinessMessage(true, BusinessMesageType.OPERATION_COMPLETED);
  }

  private void dispatchLowBudgetAlert(@NotNull Expenditure expenditure, int remaining) {
    Alert alert = new Alert();

    alert.setDailyBudget(expenditure.getDailyBudget());
    alert.setPriority(Alert.AlertPriority.HIGH);
    alert.setMessage(String.format(
        "Remaining budget sum of %d not enough to cover expenditure of %d",
        remaining, expenditure.getAmount()));

    log.info("Dispatching low budget alert to all handlers");
    notifyAllManagers(alert);
  }
}
