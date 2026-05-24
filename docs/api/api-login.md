# Login API

## Purpose

Authenticate user credentials and return JWT tokens for accessing ecosystem applications securely.

---

# Endpoint

```http
POST /auth/login
```

---

# Related Documents

- [Login Flow](./login-flow.md)

---

# Request Headers

| Header | Required | Description |
|---|---|---|
| Content-Type | Yes | application/json |
| X-Client-Id | Yes | Application client identifier |

---

# Request Body

```json
{
  "email": "user@example.com",
  "password": "your_password"
}
```

| Field | Type | Required | Description |
|---|---|---|---|
| email | string | Yes | User email address |
| password | string | Yes | User password |

---

# Success Response

## HTTP 200 OK

```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "access_token": "jwt-access-token",
    "refresh_token": "jwt-refresh-token",
    "token_type": "Bearer",
    "expires_in": 900
  },
  "timestamp": "2026-05-24T10:00:00Z"
}
```

---

# Error Responses

## 400 Bad Request

```json
{
  "success": false,
  "message": "Validation failed",
  "errors": {
    "email": "Invalid email format"
  }
}
```

---

## 401 Unauthorized

```json
{
  "success": false,
  "message": "Invalid credentials"
}
```

---

## 403 Forbidden

```json
{
  "success": false,
  "message": "Email not verified"
}
```

---

## 423 Locked

```json
{
  "success": false,
  "message": "Account locked"
}
```

---

## 428 Precondition Required

```json
{
  "success": false,
  "message": "MFA verification required"
}
```

---

# Validation Rules

| Field | Rule |
|---|---|
| email | Must be valid email format |
| password | Must not be empty |

---

# Authentication Process

The authentication process includes:

1. Validate application client
2. Retrieve user by email
3. Validate account status
4. Validate temporary lock state
5. Verify password using BCrypt
6. Verify email status
7. Check MFA configuration
8. Generate JWT tokens
9. Store login history
10. Return authentication tokens

---

# JWT Token Information

| Token Type | Expiration |
|---|---|
| Access Token | 15 minutes |
| Refresh Token | 7 days |

---

# Database Interaction

| Action | Table |
|---|---|
| Validate application | applications |
| Find user | users |
| Update failed attempts | users |
| Update login information | users |
| Save login history | login_histories |
| Store refresh token | refresh_tokens |
| Check MFA settings | mfa_settings |

---

# Security Notes

- Passwords are verified using BCrypt
- JWT tokens are securely signed
- Failed login attempts are tracked
- Account temporary locking is supported
- MFA authentication is supported
- Refresh token revocation is supported
- Login history is stored for audit and monitoring

---

# Related APIs

- `POST /auth/register`
- `POST /auth/refresh`
- `POST /auth/logout`
- `POST /auth/mfa/verify`
- `POST /auth/forgot-password`

---

# Notes

- Access token must be included in the `Authorization` header using the `Bearer` scheme
- Refresh token should be stored securely by the client application
- Login attempts may be temporarily blocked after multiple failed attempts
