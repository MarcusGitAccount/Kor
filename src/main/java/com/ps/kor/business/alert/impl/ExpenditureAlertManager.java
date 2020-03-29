package com.ps.kor.business.alert.impl;

import com.ps.kor.business.alert.util.AlertManager;
import com.ps.kor.entity.Alert;
import com.ps.kor.repo.AlertRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class ExpenditureAlertManager implements AlertManager {

  @Autowired
  private AlertRepo alertRepo;

  @Override
  public void reiceiveAlert(Alert alert) {
    log.info("Received expenditure alert.");
    log.info("Will persist alert");
    log.warn(alert.getMessage());

    alert.setIsActive(true);
    alertRepo.save(alert);
  }

}
