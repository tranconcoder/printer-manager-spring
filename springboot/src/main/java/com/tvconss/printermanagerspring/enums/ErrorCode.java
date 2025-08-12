package com.tvconss.printermanagerspring.enums;

import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
//    Server
    INTERNAL_SERVER_ERROR("INTERNAL_0000", "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    PAGE_NOT_FOUND("INTERNAL_0001", "Page not found", HttpStatus.NOT_FOUND),
    MISSING_PAYLOAD("INTERNAL_0002", "Payload bị thiếu hoặc không hợp lệ"),

//    Authenticate
    AUTH_ERROR_INTERNAL("AUTH_0100", "Auth error internal", HttpStatus.INTERNAL_SERVER_ERROR),
    AUTH_USER_ALREADY_EXIST("AUTH_0101", "Tài khoản đã có người sử dụng", HttpStatus.FORBIDDEN),
    AUTH_FAILED("AUTH_0102", "Xác thực thất bại", HttpStatus.UNAUTHORIZED),
    AUTH_MISSING_TOKEN("AUTH_0103", "Thiếu token xác thực", HttpStatus.UNAUTHORIZED),
    AUTH_INVALID_TOKEN("AUTH_0104", "Token xác thực không hợp lệ", HttpStatus.UNAUTHORIZED),

//    Upload
    UPLOAD_ERROR_INTERNAL("UPLOAD_0200", "Upload error internal", HttpStatus.INTERNAL_SERVER_ERROR),
    UPLOAD_FAILED("UPLOAD_0201", "Upload failed", HttpStatus.BAD_REQUEST),

//    User
    USER_ERROR_INTERNAL("USER_0300", "User error internal", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_NOT_FOUND("USER_0301", "User not found", HttpStatus.NOT_FOUND),
    USER_UPDATE_INVALID_PAYLOAD("USER_0302", "Invalid payload for user update", HttpStatus.BAD_REQUEST),

//    Media service
    MEDIA_ERROR_INTERNAL("MEDIA_0400", "Media service error internal", HttpStatus.INTERNAL_SERVER_ERROR),
    MEDIA_UNSUPPORTED_TYPE("MEDIA_0401", "Unsupported media type", HttpStatus.BAD_REQUEST),
    MEDIA_GET_HASH_ERROR("MEDIA_0402", "Error getting media hash", HttpStatus.INTERNAL_SERVER_ERROR),
    MEDIA_NOT_FOUND("MEDIA_0403", "Media not found", HttpStatus.NOT_FOUND),
    MEDIA_FORBIDDEN("MEDIA_0404", "Media access forbidden", HttpStatus.FORBIDDEN);



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
}
