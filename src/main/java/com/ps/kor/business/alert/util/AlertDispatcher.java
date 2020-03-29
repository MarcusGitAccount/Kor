package com.ps.kor.business.alert.util;

import com.ps.kor.entity.Alert;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlertDispatcher {

  protected List<AlertManager> managerList;

  public AlertDispatcher() {
    this.managerList = new ArrayList<>();
  }

  public void addAlertManager(AlertManager manager) {
    managerList.add(manager);
  }

  public void removeAlertManager(AlertManager manager) {
    managerList.remove(manager);
  }

  public void notifyAllManagers(Alert alert) {
    for (AlertManager manager: managerList) {
      manager.reiceiveAlert(alert);
    }
  }
}
