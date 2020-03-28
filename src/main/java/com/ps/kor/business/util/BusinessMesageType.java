package com.ps.kor.business.util;

public enum BusinessMesageType {

  USER_NOT_FOUND_AUTH("User is not authenticated.", true),

  BUDGET_CREATION_FAIL("Could not create daily budget", true),

  ROLE_CREATION_FAIL("Could not create role", true),

  BUDGET_CREATION_SUCCESS("Daily budget and creator role created", false);

  private String details;

  private boolean isError;

  BusinessMesageType(String details, boolean isError) {
    this.details = details;
    this.isError = isError;
  }

  public String getDetails() {
    return details;
  }

  public void setDetails(String details) {
    this.details = details;
  }

  public boolean isError() {
    return isError;
  }

  public void setError(boolean error) {
    isError = error;
  }

}
