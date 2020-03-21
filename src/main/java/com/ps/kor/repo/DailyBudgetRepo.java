package com.ps.kor.repo;

import com.ps.kor.entity.DailyBudget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DailyBudgetRepo extends JpaRepository<DailyBudget, UUID> {
}
