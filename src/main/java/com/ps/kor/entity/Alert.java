package com.ps.kor.entity;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "Alert")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
@ToString
public class Alert {

  public enum AlertPriority {
    LOW, HIGH
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Type(type = "uuid-char")
  private UUID id;

  @Column
  private String message;

  @Enumerated(EnumType.STRING)
  private AlertPriority priority;

  @Column(name = "is_active")
  private Boolean isActive;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "budget_id")
  private DailyBudget dailyBudget;

}
