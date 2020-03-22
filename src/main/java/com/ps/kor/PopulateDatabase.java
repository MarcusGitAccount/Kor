package com.ps.kor;

import com.ps.kor.entity.User;
import com.ps.kor.repo.UserRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Log4j2
public class PopulateDatabase {

  /**
   * Populates the application database with mockup users used
   * in manual testing the APIs.
   * @param userRepo
   * @return
   */
  @Bean(name = "populateWithUsers")
  public CommandLineRunner populateWithUsers(UserRepo userRepo) {
    User first = new User();
    User second = new User();

    first.setEmail("first@email.com");
    first.setFirstName("Vasile");
    first.setLastName("Popescu");

    second.setEmail("second@email.com");
    second.setFirstName("Ion");
    second.setLastName("Gheorghescu");

    return args -> {
      boolean firstFound  = userRepo.findByEmail(first.getEmail() ).orElse(null) != null;
      boolean secondFound = userRepo.findByEmail(second.getEmail()).orElse(null) != null;

      if (!firstFound && !secondFound) {
        log.info("Will populate database with default users");

        userRepo.save(first);
        userRepo.save(second);
      } else {
        log.info("Default users already exist in database.");
      }
    };
  }

}
