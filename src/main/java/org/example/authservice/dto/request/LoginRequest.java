package org.example.authservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.example.authservice.common.constant.Const;

public record LoginRequest(
    @NotBlank(message = "")
        @Email(regexp = Const.REGEX_EMAIL, message = "")
        @Size(max = 100, message = "")
        String email,
    @NotBlank(message = "") String password) {}
