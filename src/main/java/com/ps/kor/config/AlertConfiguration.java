package com.ps.kor.config;

import com.ps.kor.business.alert.impl.ExpenditureAlertManager;
import com.ps.kor.business.validation.ExpenditureLogicValidation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Log4j2
@Configuration
public class AlertConfiguration {

  @Autowired
  private ExpenditureAlertManager expenditureAlertManager;

  @Autowired
  private ExpenditureLogicValidation expenditureLogicValidation;

  @Bean("configureExpenditureAlert")
  public void configureExpenditureAlert() {
    expenditureLogicValidation.addAlertManager(expenditureAlertManager);
    log.info("Expenditure alert setup and running.");
  }
}
