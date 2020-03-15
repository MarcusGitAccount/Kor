package com.ps.kor.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;
import java.util.Date;

@Entity
public class Expenditure {

  // TODO: define rules for each at validation level
  public enum ExpenditureType {
    BILL, AMENITY, MISC, FOOD, LARGE
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Type(type = "uuid-char")
  private UUID id;

  @Column(nullable = false, updatable = false, name = "creation_time")
  @CreationTimestamp
  private Date creationTime;

  @Column(nullable = false, columnDefinition = "int default 0")
  private Integer amount;

  @Column(length = 3, name = "currency_code")
  @NotNull
  private String currencyCode;

  @Column
  private String comment;

  @Column
  @Enumerated(EnumType.STRING)
  @NotNull
  private ExpenditureType type;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "plan_id")
  private SpendingPlan plan;

  public Expenditure() {
  }


  public Expenditure(UUID id, Date creationTime, Integer amount,
                     @NotNull String currencyCode,
                     String comment, @NotNull ExpenditureType type,
                     User user, SpendingPlan plan) {
    this.id = id;
    this.creationTime = creationTime;
    this.amount = amount;
    this.currencyCode = currencyCode;
    this.comment = comment;
    this.type = type;
    this.user = user;
    this.plan = plan;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Date getCreationTime() {
    return creationTime;
  }

  public void setCreationTime(Date creationTime) {
    this.creationTime = creationTime;
  }

  public Integer getAmount() {
    return amount;
  }

  public void setAmount(Integer amount) {
    this.amount = amount;
  }

  public String getCurrencyCode() {
    return currencyCode;
  }

  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public SpendingPlan getPlan() {
    return plan;
  }

  public void setPlan(SpendingPlan plan) {
    this.plan = plan;
  }

  public ExpenditureType getType() {
    return type;
  }

  public void setType(ExpenditureType type) {
    this.type = type;
  }
}
