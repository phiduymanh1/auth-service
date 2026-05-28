package org.example.authservice.repository;

import java.util.UUID;
import org.example.authservice.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, UUID> {

  Application findByClientId(String clientId);
}
