package org.example.authservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private UUID id;

  @Size(max = 255)
  @NotNull
  @Column(name = "email", nullable = false)
  private String email;

  @Size(max = 255)
  @Column(name = "password_hash")
  private String passwordHash;

  @ColumnDefault("'ACTIVE'")
  @Column(name = "status", columnDefinition = "user_status not null")
  private Object status;

  @NotNull
  @ColumnDefault("false")
  @Column(name = "email_verified", nullable = false)
  private Boolean emailVerified;

  @NotNull
  @ColumnDefault("0")
  @Column(name = "failed_login_attempts", nullable = false)
  private Integer failedLoginAttempts;

  @Column(name = "locked_until")
  private OffsetDateTime lockedUntil;

  @Column(name = "last_login_at")
  private OffsetDateTime lastLoginAt;

  @NotNull
  @ColumnDefault("now()")
  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt;

  @NotNull
  @ColumnDefault("now()")
  @Column(name = "updated_at", nullable = false)
  private OffsetDateTime updatedAt;
}
