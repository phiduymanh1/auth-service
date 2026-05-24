package org.example.authservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "login_histories")
public class LoginHistory {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.SET_NULL)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.SET_NULL)
  @JoinColumn(name = "application_id")
  private Application application;

  @Size(max = 255)
  @NotNull
  @Column(name = "email", nullable = false)
  private String email;

  @Size(max = 100)
  @Column(name = "ip_address", length = 100)
  private String ipAddress;

  @Column(name = "user_agent", length = Integer.MAX_VALUE)
  private String userAgent;

  @Column(name = "login_status", columnDefinition = "login_status_type not null")
  private Object loginStatus;

  @Size(max = 255)
  @Column(name = "failure_reason")
  private String failureReason;

  @NotNull
  @ColumnDefault("now()")
  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt;
}
