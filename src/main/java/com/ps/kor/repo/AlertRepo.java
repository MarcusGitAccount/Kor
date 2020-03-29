package com.ps.kor.repo;

import com.ps.kor.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AlertRepo extends JpaRepository<Alert, UUID> {
}
