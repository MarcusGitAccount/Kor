package com.ps.kor.repo;

import com.ps.kor.entity.Expenditure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExpenditureRepo extends JpaRepository<Expenditure, UUID> {
}
