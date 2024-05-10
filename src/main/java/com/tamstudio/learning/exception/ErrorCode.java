package com.tamstudio.learning.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public enum ErrorCode {
    USER_EXITS(100, "User already exists", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(104, "Role not found", HttpStatus.BAD_REQUEST),
    INVALID_INPUT(1, "Invalid Input", HttpStatus.BAD_REQUEST),
    FILE_NOT_FOUND(2, "File Not Found", HttpStatus.NOT_FOUND),
    DATABASE_ERROR(3, "Database Error", HttpStatus.INTERNAL_SERVER_ERROR),
    NETWORK_ERROR(4, "Network Error", HttpStatus.INTERNAL_SERVER_ERROR),
    UNKNOWN_ERROR(6, "Unknown Error", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_NOT_FOUND(101, "User not found", HttpStatus.NOT_FOUND),
    AUTHENTICATION_FAILED(102, "Authentication failed", HttpStatus.UNAUTHORIZED),
    AUTHORIZATION_FAILED(103, "Authorization failed", HttpStatus.FORBIDDEN),
    ;

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }
}
