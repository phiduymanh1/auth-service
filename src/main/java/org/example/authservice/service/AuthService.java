package org.example.authservice.service;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.example.authservice.common.constant.Const;
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

  /** login */
  public Map<String, String> login(String userEmail, String password, String clientId) {
    Authentication auth =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(userEmail, password));
    // If authenticate success, create JWT
    String accessToken = null;
    //                jwtUtil.generateAccessToken(auth.getName());
    String refreshToken = null;
    //                jwtUtil.generateRefreshToken(auth.getName());

    // Return both tokens, controller will set cookies
    return Map.of(Const.TEXT_ACCESS_TOKEN, accessToken, Const.TEXT_REFRESH_TOKEN, refreshToken);
  }
}
