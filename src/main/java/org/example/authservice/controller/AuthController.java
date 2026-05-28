package org.example.authservice.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.example.authservice.common.constant.Const;
import org.example.authservice.common.enums.MessageConst;
import org.example.authservice.dto.request.auth.LoginRequest;
import org.example.authservice.dto.response.auth.AuthResponse;
import org.example.authservice.dto.response.common.ApiResponse;
import org.example.authservice.service.AuthService;
import org.example.authservice.utils.response.ApiResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final AuthService authService;
  private final ApiResponseUtil apiResponseUtil;

  // TODO: 1. POST /api/v1/auth/register
  @PostMapping
  public ResponseEntity<Object> register() {
    return null;
  }

  /** Login API */
  @PostMapping("/token")
  public ResponseEntity<ApiResponse<Object>> login(
      @RequestHeader(Const.AUTH_HEADER_CLIENT_ID) @NotBlank String clientId,
      @Valid @RequestBody LoginRequest request,
      HttpServletResponse response) {

    Map<String, String> tokens = authService.login(request, clientId);

    // Set refresh token cookie
    Cookie cookie = new Cookie(Const.TEXT_REFRESH_TOKEN, tokens.get(Const.TEXT_REFRESH_TOKEN));
    cookie.setHttpOnly(true);
    cookie.setSecure(false); // deploy HTTPS -> true
    cookie.setPath("/");
    cookie.setMaxAge(7 * 24 * 60 * 60);
    cookie.setAttribute("SameSite", "Strict");
    response.addCookie(cookie);

    AuthResponse authResponse = new AuthResponse(tokens.get(Const.TEXT_ACCESS_TOKEN));

    return ResponseEntity.ok(
        apiResponseUtil.success(MessageConst.AUTH_LOGIN_SUCCESS, authResponse));
  }

  // TODO: 3. POST /api/v1/auth/refresh
  // TODO: 4. POST /api/v1/auth/logout
}
