package com.ps.kor.repo;

import com.ps.kor.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepo extends JpaRepository<User, UUID> {

  Optional<User> findByEmail(@Param("email") String email);

}
