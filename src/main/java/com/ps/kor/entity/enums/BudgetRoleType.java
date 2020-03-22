package com.ps.kor.entity.enums;

public enum BudgetRoleType {

  CREATOR("Creator",       3, "Can nomitate administrators"),
  ADMIN  ("Administrator", 2, "Can edit any expenditure and manage users"),
  EDIT   ("Editor",        1, "Can add and edit own expenditures"),
  VIEW   ("Viewer",        0, "Can view budget");

  public final String label;

  public final Integer rank;

  public final String comment;

  BudgetRoleType(String label, Integer rank, String comment) {
    this.label = label;
    this.rank = rank;
    this.comment = comment;
  }
}
