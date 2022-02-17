package com.file.server.api.common;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INVALID_INPUT_VALUE(400, "C001", "Invalid Input Value"),
    HANDLE_ACCESS_DENIED(403, "C002", "Access is Denied"),
    NOT_FOUND(404, "C003", "Not Found"),
    METHOD_NOT_ALLOWED(405, "C004", "Method Not Allowed"),
    INTERNAL_SERVER_ERROR(500, "S001", "Internal Server error");

    private final String code;
    private final String message;
    private int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
