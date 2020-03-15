package com.ps.kor.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
@ToString
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
  @JoinColumn(name = "daily_budget_id")
  private DailyBudget dailyBudget;
}
