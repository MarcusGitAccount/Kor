package com.ps.kor.repo;

import com.ps.kor.entity.DailyBudget;
import com.ps.kor.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepo extends JpaRepository<User, UUID> {

  Optional<User> findByEmail(@Param("email") String email);

  @Query(value =
    "select u from User u " +
    "where u not in ( " +
      "select r.user from BudgetRole r " +
      "where r.dailyBudget = :dailyBudget " +
      ")"
  )
  List<User> findAllNotPartOfBudget(@Param("dailyBudget") DailyBudget dailyBudget);
}
