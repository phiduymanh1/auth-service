package org.example.authservice.dto.response.common;

public record FieldErrorResponse(
        String field,
        String message
) {
}
