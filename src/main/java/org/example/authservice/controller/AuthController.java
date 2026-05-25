package org.example.authservice.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.example.authservice.common.constant.Const;
import org.example.authservice.dto.request.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

  // TODO: 1. POST /api/v1/auth/register
  @PostMapping
  public ResponseEntity<Object> register() {
    return null;
  }

  /** Login API */
  @PostMapping("/login")
  public ResponseEntity<Object> login(
      @RequestHeader(Const.AUTH_HEADER_CLIENT_ID) @NotBlank String clientId,
      @Valid @RequestBody LoginRequest request,
      HttpServletResponse response) {
    return null;
  }

  // TODO: 3. POST /api/v1/auth/refresh
  // TODO: 4. POST /api/v1/auth/logout
}
