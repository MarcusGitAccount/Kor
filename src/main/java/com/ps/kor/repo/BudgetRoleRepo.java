package com.ps.kor.repo;

import com.ps.kor.entity.BudgetRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BudgetRoleRepo extends JpaRepository<BudgetRole, UUID> {
}
