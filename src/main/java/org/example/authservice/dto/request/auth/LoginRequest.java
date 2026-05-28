package org.example.authservice.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.example.authservice.common.constant.Const;

public record LoginRequest(
    @NotBlank(message = "{validation.email.required}")
        @Email(regexp = Const.REGEX_EMAIL, message = "{validation.email.invalid}")
        @Size(max = 100, message = "{validation.email.max-length}")
        String email,
    @NotBlank(message = "{validation.password.required}")
        @Size(min = 6, message = "{validation.password.min-length}")
        String password) {}
