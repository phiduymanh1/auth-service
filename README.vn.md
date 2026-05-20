# auth-service

en English: [README.en.md](README.md)

## 1. Giới thiệu
- Hệ thống Auth Service được thiết kế như một trung tâm xác thực và phân quyền dùng chung cho nhiều ứng dụng trong hệ sinh thái. Service hỗ trợ quản lý tài khoản người dùng, phân quyền theo ứng dụng, xác thực đăng nhập, OAuth2, MFA, quản lý token bảo mật, ghi nhận lịch sử đăng nhập và audit log nhằm đảm bảo tính bảo mật, khả năng mở rộng và dễ dàng tích hợp cho các hệ thống vệ tinh trong tương lai.
## 2. Mục tiêu
- Xây dựng một hệ thống xác thực và phân quyền tập trung dùng chung cho nhiều ứng dụng.
- Đảm bảo bảo mật tài khoản người dùng với các cơ chế như JWT, MFA, OAuth2 và quản lý token bảo mật.
- Hỗ trợ quản lý vai trò (Role) và quyền hạn (Permission) linh hoạt theo từng ứng dụng.
- Cung cấp khả năng theo dõi và giám sát hoạt động đăng nhập, thay đổi dữ liệu thông qua login history và audit log.
- Thiết kế theo hướng dễ mở rộng, dễ tích hợp với các microservice và hệ thống trong tương lai.
- Tối ưu hiệu năng truy vấn và khả năng mở rộng cơ sở dữ liệu cho môi trường production.
## 3. Tính năng
- Quản lý tài khoản người dùng tập trung.
- Xác thực đăng nhập bằng email/password và OAuth2 (Google, Facebook, ...).
- Hỗ trợ phân quyền theo Role và Permission cho từng ứng dụng.
- Quản lý nhiều application/client trong cùng hệ thống auth.
- Hỗ trợ MFA (2FA) bằng OTP và backup codes.
- Quản lý security token cho xác thực email, reset mật khẩu và MFA verification.
- Theo dõi lịch sử đăng nhập và trạng thái đăng nhập.
- Ghi nhận audit log cho các hành động quan trọng trong hệ thống.
- Cơ chế khóa tài khoản tạm thời khi đăng nhập thất bại nhiều lần.
- Hỗ trợ mở rộng tích hợp với API Gateway và các microservice khác.
- Thiết kế database chuẩn hóa, tối ưu index và hỗ trợ scale trong môi trường production.
## 4. Kiến trúc tổng quan
Hệ thống được xây dựng theo mô hình tập trung (Centralized Authentication Service), đóng vai trò là trung tâm xác thực và phân quyền cho toàn bộ hệ sinh thái ứng dụng và microservice.

Các ứng dụng client sẽ giao tiếp với Auth Service thông qua API hoặc API Gateway để thực hiện các chức năng như đăng nhập, xác thực token, phân quyền và quản lý người dùng. Sau khi xác thực thành công, hệ thống phát hành JWT Access Token/Refresh Token để các service khác sử dụng trong quá trình authorization.

Auth Service quản lý tập trung:

- Người dùng (Users)
- Vai trò và quyền hạn (Roles & Permissions)
- Phân quyền theo từng ứng dụng (Application-based Authorization)
- OAuth2 Login
- MFA/OTP Security
- Security Tokens
- Login History & Audit Logs

Kiến trúc được thiết kế theo hướng:

- Stateless authentication bằng JWT.
- Dễ mở rộng theo mô hình microservice.
- Tách biệt rõ Authentication và Authorization.
- Hỗ trợ tích hợp API Gateway, Kafka/Event-Driven hoặc Internal Service Communication.
- Tối ưu bảo mật, khả năng mở rộng và hiệu năng production.
## 5. Cấu trúc dự án

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

## 6. Tổng quan về xác thực
Hệ thống sử dụng cơ chế xác thực tập trung dựa trên JWT và OAuth2 Resource Server nhằm cung cấp khả năng xác thực và phân quyền thống nhất cho toàn bộ hệ sinh thái ứng dụng.

Quy trình xác thực cơ bản:

1. Người dùng gửi thông tin đăng nhập đến Auth Service.
2. Auth Service xác thực tài khoản, kiểm tra trạng thái user, MFA và quyền truy cập ứng dụng.
3. Sau khi xác thực thành công, hệ thống phát hành Access Token và Refresh Token.
4. Các client/service sử dụng Access Token để truy cập tài nguyên thông qua API Gateway hoặc trực tiếp tới Resource Server.
5. Resource Server xác thực JWT và thực hiện authorization dựa trên Role/Permission được nhúng trong token.

Hệ thống hỗ trợ nhiều cơ chế xác thực:

- Email/Password Authentication
- OAuth2 Login (Google, Facebook, ...)
- MFA/OTP Verification
- Refresh Token Flow
- Application-based Authorization

Ngoài ra, hệ thống còn hỗ trợ:

- Khóa tài khoản tạm thời khi đăng nhập thất bại nhiều lần.
- Quản lý security token cho verify email và reset password.
- Theo dõi login history và audit logs phục vụ monitoring và security auditing.

## 7. Công nghệ sử dụng
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
## 8. Tài liệu

## 9. Bắt đầu