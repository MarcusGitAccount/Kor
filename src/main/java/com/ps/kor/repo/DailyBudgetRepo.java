package com.ps.kor.repo;

import com.ps.kor.entity.DailyBudget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface DailyBudgetRepo extends JpaRepository<DailyBudget, UUID> {

  @Query(value =
      "select coalesce(sum(e.amount), 0) " +
      "from Expenditure e " +
      "where e.dailyBudget = :budget"
  )
  Integer findUsedBudget(@Param("budget") DailyBudget budget);

}
