package com.tvconss.printermanagerspring.enums;

import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
//    Server
    INTERNAL_SERVER_ERROR("INTERNAL_0000", "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    PAGE_NOT_FOUND("INTERNAL_0001", "Page not found", HttpStatus.NOT_FOUND),
    MISSING_PAYLOAD("INTERNAL_0002", "Missing or conflict payload fields in request body"),

//    Authenticate
    AUTH_ERROR_INTERNAL("AUTH_0100", "Auth error internal", HttpStatus.INTERNAL_SERVER_ERROR),
    AUTH_USER_ALREADY_EXIST("AUTH_0101", "Tài khoản đã có người sử dụng"),
    AUTH_FAILED("AUTH_0102", "Xác thực thất bại", HttpStatus.UNAUTHORIZED),;

    public final String errorCode;
    public final String errorMessage;
    public final HttpStatus errorHttpCode;

    ErrorCode(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.errorHttpCode = HttpStatus.BAD_REQUEST;
    }

    ErrorCode(String errorCode, String errorMessage, HttpStatus errorHttpCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.errorHttpCode = errorHttpCode;
    }

//    public String getErrorCode() {
//        return this.errorCode;
//    }
//
//    public String getErrorMessage() {
//        return this.errorMessage;
//    }
//
//    public Integer getErrorHttpCode() {
//        return this.errorHttpCode.value();
//    }
}
