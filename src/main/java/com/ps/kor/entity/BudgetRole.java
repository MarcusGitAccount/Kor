package com.ps.kor.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.ps.kor.entity.enums.BudgetRoleType;

@Entity
@Table(name = "BudgetRole")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
@ToString
public class BudgetRole {

  public static final String USER_JOIN_COL_NAME = "user";

  public static final String SELF_REF_COL_NAME = "creator";

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Type(type = "uuid-char")
  private UUID id;

  @Column(name = "creation_time", nullable = false, updatable = false)
  @CreationTimestamp
  private Date creationTime;

  @Column
  private Boolean enabled;

  @Column(name = "role_type")
  @Enumerated(EnumType.STRING)
  private BudgetRoleType roleType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "creator_id")
  private BudgetRole creator;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = SELF_REF_COL_NAME)
  private List<BudgetRole> createdRoles;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "daily_budget_id")
  private DailyBudget dailyBudget;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "budgetRole")
  private List<Expenditure> createdExpenditures;
}
