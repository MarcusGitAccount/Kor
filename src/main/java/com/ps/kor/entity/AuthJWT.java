package com.ps.kor.entity;

import lombok.*;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "AuthJWT")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
@ToString
public class AuthJWT {

  @Id
  private String token;

  @Column(unique = true)
  private String userEmail;

}
