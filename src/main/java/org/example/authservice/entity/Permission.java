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
@Table(name = "permissions")
public class Permission {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private UUID id;

  @Size(max = 100)
  @NotNull
  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Column(name = "description", length = Integer.MAX_VALUE)
  private String description;

  @NotNull
  @ColumnDefault("now()")
  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt;
}
