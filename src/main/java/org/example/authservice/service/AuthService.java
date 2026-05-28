package org.example.authservice.service;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.example.authservice.common.constant.Const;
import org.example.authservice.dto.request.auth.LoginRequest;
import org.example.authservice.entity.Application;
import org.example.authservice.repository.ApplicationRepository;
import org.example.authservice.utils.jwt.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;
  private final ApplicationRepository applicationRepository;

  /** login */
  public Map<String, String> login(LoginRequest request, String clientId) {

    Application application = applicationRepository.findByClientId(clientId);

    Authentication auth =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email(), request.password()));
    // If authenticate success, create JWT
    String accessToken = null;
    //                jwtUtil.generateAccessToken(auth.getName());
    String refreshToken = null;
    //                jwtUtil.generateRefreshToken(auth.getName());

    // Return both tokens, controller will set cookies
    return Map.of(Const.TEXT_ACCESS_TOKEN, accessToken, Const.TEXT_REFRESH_TOKEN, refreshToken);
  }
}
