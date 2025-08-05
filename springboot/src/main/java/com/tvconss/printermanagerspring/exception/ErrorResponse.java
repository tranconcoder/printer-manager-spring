package com.tvconss.printermanagerspring.exception;

import com.tvconss.printermanagerspring.enums.ErrorCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Data
@EqualsAndHashCode(callSuper = false)
public class ErrorResponse extends RuntimeException {
    public ErrorResponse() {
        super("INTERNAL_SERVER_ERROR");

        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;

        this.setErrorHttpCode(errorCode.getErrorHttpCode().value());
        this.setErrorCode(errorCode.getErrorCode());
        this.setErrorMessage(errorCode.getErrorMessage());
        this.setErrorTimestamp(new Date());
    }

    public ErrorResponse(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());

        this.setErrorDesc("");
        this.setErrorHttpCode(errorCode.getErrorHttpCode().value());
        this.setErrorCode(errorCode.getErrorCode());
        this.setErrorMessage(errorCode.getErrorMessage());
        this.setErrorTimestamp(new Date());
    }

    public ErrorResponse(ErrorCode errorCode, String errorDesc) {
        super(errorCode.getErrorMessage());

        this.setErrorHttpCode(errorCode.getErrorHttpCode().value());
        this.setErrorCode(errorCode.getErrorCode());
        this.setErrorMessage(errorCode.getErrorMessage());
        this.setErrorDesc(errorDesc);
        this.setErrorTimestamp(new Date());
    }

    private String errorMessage;

    private String errorCode;

    private Integer errorHttpCode;

    private String errorDesc;

    private Date errorTimestamp;

    @Override
    public String toString() {
        String timeStr = this.getErrorTimestamp().toString();

        return String.format("[%s] %s: %s", timeStr, this.getErrorMessage(), this.getErrorDesc());
    }

    public Map getResponse(String url) {
        Map<String, Object> response =  this.getResponse();

        response.put("errorRequestUrl", url);

        return response;
    }

    public Map getResponse() {
        Map<String, Object> response =  new TreeMap<>();

        response.put("errorHttpCode", this.getErrorHttpCode());
        response.put("errorCode", this.getErrorCode());
        response.put("errorMessage", this.getErrorMessage());
        response.put("errorDesc", this.getErrorDesc());
        response.put("errorTimestamp", this.getErrorTimestamp().getTime());

        return response;
    }
}
