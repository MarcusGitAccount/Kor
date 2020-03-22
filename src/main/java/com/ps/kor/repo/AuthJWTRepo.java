package com.ps.kor.repo;

import com.ps.kor.entity.AuthJWT;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthJWTRepo extends JpaRepository<AuthJWT, String> {

  Optional<AuthJWT> findByUserEmail(String userEmail);

}
