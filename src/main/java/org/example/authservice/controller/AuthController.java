package org.example.authservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    //TODO: 1. POST /api/v1/auth/register
    @PostMapping
    public ResponseEntity<Object> register(){
        return null;
    }

    //TODO: 2. POST /api/v1/auth/login
    //TODO: 3. POST /api/v1/auth/refresh
    //TODO: 4. POST /api/v1/auth/logout
}
