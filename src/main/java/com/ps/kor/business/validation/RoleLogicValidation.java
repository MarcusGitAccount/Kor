package com.ps.kor.business.validation;

import com.ps.kor.business.util.BusinessMesageType;
import com.ps.kor.business.util.BusinessMessage;
import com.ps.kor.entity.BudgetRole;
import com.ps.kor.entity.enums.BudgetRoleType;
import org.springframework.stereotype.Service;

@Service
public class RoleLogicValidation {

  public BusinessMessage<Boolean> createRoleValidation(BudgetRole initiator, BudgetRole toBeCreated) {
    if (initiator == null) {
      return new BusinessMessage(false, BusinessMesageType.USER_NOT_PART_OF_BUDGET);
    }
    if (!checkIfCanPerformActionType(initiator, BudgetRoleType.ADMIN)) {
      return new BusinessMessage(false, BusinessMesageType.UNAUTHORIZED_ROLE_CREATION);
    }
    if (initiator.getRoleType().rank <= toBeCreated.getRoleType().rank) {
      return new BusinessMessage(false, BusinessMesageType.UNAUTHORIZED_ROLE_CREATION);
    }

    return new BusinessMessage(true, null);
  }

  public boolean checkIfCanPerformActionType(BudgetRole initiator, BudgetRoleType actionType) {
    return initiator.getRoleType().rank >= actionType.rank;
  }
}
