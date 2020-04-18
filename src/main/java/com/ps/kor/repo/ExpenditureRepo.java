package com.ps.kor.repo;

import com.ps.kor.business.report.aggregation.CountSumByTypeAggregation;
import com.ps.kor.business.report.aggregation.CountSumByTypeRoleAggregation;
import com.ps.kor.entity.DailyBudget;
import com.ps.kor.entity.Expenditure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ExpenditureRepo extends JpaRepository<Expenditure, UUID> {

  @Query(value =
    "select new com.ps.kor.business.report.aggregation.CountSumByTypeAggregation(" +
    "e.type, sum(e.amount), count(e.amount)) " +
    "from Expenditure e " +
    "where e.dailyBudget = :budget " +
    "group by e.type "
  )
  List<CountSumByTypeAggregation> countAndSumByType(@Param("budget") DailyBudget budget);

  @Query(value =
      "select new com.ps.kor.business.report.aggregation.CountSumByTypeRoleAggregation(" +
          "concat(e.budgetRole.user.firstName, ' ', e.budgetRole.user.lastName), " +
          "e.type, sum(e.amount), count(e.amount)) " +
          "from Expenditure e " +
          "where e.dailyBudget = :budget " +
          "group by e.type, e.budgetRole"
  )
  List<CountSumByTypeRoleAggregation> countAndSumByTypeAndRole(@Param("budget") DailyBudget budget);

}
