package com.community.back.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // Auth related
    EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "EMAIL_ALREADY_EXISTS", "Email already exists"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "INVALID_PASSWORD", "Invalid password"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "User not found"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "INVALID_TOKEN", "Invalid or expired token"),

    // Field related
    FIELD_NOT_FOUND(HttpStatus.NOT_FOUND, "FIELD_NOT_FOUND", "Field not found"),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "INVALID_INPUT", "Invalid field data"),

    // Common
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "Internal server error");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
