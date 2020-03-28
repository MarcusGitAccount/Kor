package com.ps.kor.repo;

import com.ps.kor.entity.BudgetRole;
import com.ps.kor.entity.DailyBudget;
import com.ps.kor.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface BudgetRoleRepo extends JpaRepository<BudgetRole, UUID> {

  @Query(value =
    "select b from BudgetRole b " +
    "where b.dailyBudget = :dailyBudget and " +
    "b.user = :user"
  )
  Optional<BudgetRole> findByUserAndBudget(@Param("dailyBudget") DailyBudget dailyBudget,
                                           @Param("user")        User user);
}
