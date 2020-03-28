package com.ps.kor;

import com.ps.kor.business.auth.AuthenticationUtils;
import com.ps.kor.repo.BudgetRoleRepo;
import com.ps.kor.repo.DailyBudgetRepo;
import com.ps.kor.repo.UserRepo;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("testing")
@Configuration
public class TestingConfiguration {

  @Bean
  public UserRepo userRepo() {
    return Mockito.mock(UserRepo.class);
  }

  @Bean
  public DailyBudgetRepo dailyBudgetRepo() {
    return Mockito.mock(DailyBudgetRepo.class);
  }

  @Bean
  public BudgetRoleRepo budgetRoleRepo() {
    return Mockito.mock(BudgetRoleRepo.class);
  }

  @Bean
  public AuthenticationUtils authUtils() {
    return Mockito.mock(AuthenticationUtils.class);
  }

}
