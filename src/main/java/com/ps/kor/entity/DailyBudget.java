package com.ps.kor.entity;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.Date;

@Entity
@Table(name = "DailyBudget")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
@ToString
public class DailyBudget {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Type(type = "uuid-char")
  private UUID id;

  @Column(nullable = false)
  @Temporal(TemporalType.DATE)
  private Date date;

  @Column
  private String name;

  @Column(length = 255)
  private String comment;

  @Column(name = "imposed_limit", nullable = false, columnDefinition = "int default 0")
  private Integer imposedLimit;

  @NotNull
  @Column(length = 3, name = "currency_code")
  private String currencyCode;

  @Column
  @OneToMany(mappedBy = "dailyBudget")
  private List<Expenditure> expenditureList;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "DailyBudget_BudgetRole",
      joinColumns = @JoinColumn(name = "budget_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  private List<BudgetRole> budgetRoleList;

}
