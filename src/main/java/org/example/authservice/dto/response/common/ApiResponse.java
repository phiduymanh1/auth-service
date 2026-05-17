package org.example.authservice.dto.response.common;

public record ApiResponse<T>(ResponseMetaData metaData, T data) {}
