package com.ps.kor.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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

  public enum BudgetRoleType {
    CREATOR, ADMIN, EDIT, VIEW
  }

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
  private BudgetRoleType roleType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "creator_id")
  private BudgetRole creator;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = SELF_REF_COL_NAME)
  private List<BudgetRole> createdRoles;

  @ManyToMany(mappedBy = "budgetRoleList", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<DailyBudget> dailyBudgetList;
}
