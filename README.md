# auth-service

## 1. Introduction
- The Auth Service is designed as a centralized authentication and authorization hub for multiple applications within the ecosystem. The service supports user account management, application-based authorization, login authentication, OAuth2, MFA, secure token management, login history tracking, and audit logging to ensure security, scalability, and easy integration with future satellite systems.

## 2. Goals
- Build a centralized authentication and authorization system shared across multiple applications.
- Ensure user account security with mechanisms such as JWT, MFA, OAuth2, and secure token management.
- Support flexible Role and Permission management per application.
- Provide the ability to track and monitor login activities and data changes through login history and audit logs.
- Design for scalability and easy integration with microservices and future systems.
- Optimize query performance and database scalability for production environments.

## 3. Features
- Centralized user account management.
- Email/password authentication and OAuth2 login (Google, Facxebook, ...).
- Role and Permission-based authorization per application.
- Manage multiple applications/clients within the same auth system.
- Support MFA (2FA) via OTP and backup codes.
- Security token management for email verification, password reset, and MFA verification.
- Login history tracking and login status monitoring.
- Audit logging for critical system actions.
- Temporary account lockout mechanism for multiple failed login attempts.
- Support for integration with API Gateway and other microservices.
- Normalized database design with optimized indexes and production scalability support.

## 4. High-Level Architecture
The system is built following a Centralized Authentication Service model, serving as the authentication and authorization hub for the entire application ecosystem and microservices.

Client applications communicate with Auth Service through API or API Gateway to perform functions such as login, token authentication, authorization, and user management. After successful authentication, the system issues JWT Access Token/Refresh Token for other services to use during the authorization process.

Auth Service centrally manages:

- Users
- Roles & Permissions
- Application-based Authorization
- OAuth2 Login
- MFA/OTP Security
- Security Tokens
- Login History & Audit Logs

The architecture is designed with the following principles:

- Stateless authentication using JWT.
- Scalable following microservice model.
- Clear separation of Authentication and Authorization.
- Support for API Gateway, Kafka/Event-Driven, or Internal Service Communication integration.
- Optimized for security, scalability, and production performance.

## 5. Project Structure

```bash
auth-service/
│
├── docs/
│   ├── architecture/
│   ├── api/
│   └── database/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/example/authservice/
│   │   │
│   │   │       ├── config/
│   │   │       │
│   │   │       ├── common/
│   │   │       │   ├── constants/
│   │   │       │   ├── enums/
│   │   │       │   ├── response/
│   │   │       │   └── exception/
│   │   │       │
│   │   │       ├── controller/
│   │   │       │
│   │   │       ├── dto/
│   │   │       │   ├── request/
│   │   │       │   └── response/
│   │   │       │
│   │   │       ├── entity/
│   │   │       │
│   │   │       ├── repository/
│   │   │       │
│   │   │       ├── service/
│   │   │       │
│   │   │       ├── mapper/
│   │   │       │
│   │   │       ├── security/
│   │   │       │   ├── jwt/
│   │   │       │   ├── filter/
│   │   │       │   ├── handler/
│   │   │       │   ├── principal/
│   │   │       │   └── oauth2/
│   │   │       │
│   │   │       ├── validation/
│   │   │       │
│   │   │       ├── client/
│   │   │       │
│   │   │       ├── event/
│   │   │       │
│   │   │       ├── audit/
│   │   │       │
│   │   │       ├── scheduler/
│   │   │       │
│   │   │       ├── utils/
│   │   │       │
│   │   │       └── AuthServiceApplication.java
│   │   │
│   │   └── resources/
│   │       ├── db/
│   │       │   └── migration/
│   │       │
│   │       ├── static/
│   │       ├── templates/
│   │       │
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       ├── application-prod.yml
│   │       ├── application-test.yml
│   │       │
│   │       ├── logback-spring.xml
│   │       └── messages.properties
│   │
│   └── test/
│       └── java/
│           └── org/example/authservice/
│
├── .env
├── docker-compose.yml
├── Dockerfile
├── pom.xml
└── README.md
```

## 6. Authentication Overview
The system uses a centralized authentication mechanism based on JWT and OAuth2 Resource Server to provide unified authentication and authorization capabilities for the entire application ecosystem.

Basic authentication flow:

1. User sends login credentials to Auth Service.
2. Auth Service authenticates the account, checks user status, MFA, and application access permissions.
3. After successful authentication, the system issues Access Token and Refresh Token.
4. Clients/services use Access Token to access resources through API Gateway or directly to Resource Server.
5. Resource Server validates JWT and performs authorization based on Role/Permission embedded in the token.

The system supports multiple authentication mechanisms:

- Email/Password Authentication
- OAuth2 Login (Google, Facebook, ...)
- MFA/OTP Verification
- Refresh Token Flow
- Application-based Authorization

Additionally, the system supports:

- Temporary account lockout for multiple failed login attempts.
- Security token management for email verification and password reset.
- Login history tracking and audit logs for monitoring and security auditing.

## 7. Tech Stack
Backend
- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- Spring Validation
- OAuth2 Resource Server
- JWT Authentication
- Lombok
- MapStruct

Database & Cache
- PostgreSQL
- Flyway Migration
- Redis (Caching / Token blacklist / Rate limiting)

Messaging & Communication
- Apache Kafka
- OpenFeign

DevOps & Infrastructure
- Docker
- Docker Compose
- API Gateway
- GitHub Actions (CI/CD)

Monitoring & Logging
- Spring Actuator
- Prometheus & Grafana
- ELK Stack / Loki
- Audit Logging

Documentation & Testing
- Swagger / OpenAPI
- JUnit 5
- Mockito
- Testcontainers
- SonarQube

Security
- JWT
- OAuth2
- MFA (TOTP)
- BCrypt Password Encoder
- Rate Limiting
- Role-Based Access Control (RBAC)

## 8. Documentation

## 9. Getting Started