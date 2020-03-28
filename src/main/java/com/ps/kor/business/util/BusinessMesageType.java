package com.ps.kor.business.util;

public enum BusinessMesageType {

  USER_NOT_FOUND_AUTH("User is not authenticated.", true),

  BUDGET_CREATION_FAIL("Could not create daily budget", true),

  ROLE_CREATION_FAIL("Could not create role", true),

  BUDGET_CREATION_SUCCESS("Daily budget and creator role created", false),

  USER_CREATION_FAIL("Failed creating user", true),

  SIGNUP_SUCCESS("Successful signup", false),

  USER_NOT_FOUND("User does not exist", false),

  INVALID_PASSWORD("Invalid password", true),

  ALREADY_LOGGED_IN("User already logged in.", false),

  LOG_IN_SUCCESS("Logged in", false),

  LOG_OUT_SUCESS("Logged out", false),

  TOKEN_EMPTY("Token is empty", false),

  TOKEN_INVALID("Token is invalid", false),

  BUDGET_NOT_FOUND("Budget not found", false),

  ROLE_CREATION_SUCCESS("Role created successfuly", false),

  USER_NOT_PART_OF_BUDGET("You are taking part in the planning of this budget", true),

  UNAUTHORIZED_ROLE_CREATION("You do not have the rights to create this role", true),

  ALREADY_HAS_ROLE("User already has a role in the given budget", true)

  ;

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
