package com.tvconss.printermanagerspring.controller;

import com.tvconss.printermanagerspring.enums.ErrorCode;
import com.tvconss.printermanagerspring.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(ErrorResponse.class)
    public ResponseEntity<?> errorResponseException (HttpServletRequest req, ErrorResponse error) {
        log.error(error.getMessage() + " - " + error.getErrorHttpCode() + " - " + req.getRequestURI() );

        return ResponseEntity.status(error.getErrorHttpCode()).body(error.getResponse(req.getRequestURI()));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> handleNoHandlerFoundException(HttpServletRequest req, NoHandlerFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.PAGE_NOT_FOUND);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse.getResponse(req.getRequestURI()));
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, NoResourceFoundException.class})
    public ResponseEntity<?> handleMissingRequestBody(HttpServletRequest req, HttpMessageNotReadableException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.MISSING_PAYLOAD);

        return ResponseEntity.internalServerError().body(errorResponse.getResponse(req.getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(HttpServletRequest req, MethodArgumentNotValidException ex) {
//        Get validation error list
        List<String> messages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage()) // Chỉ lấy message ngắn
                .toList();

        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCode.MISSING_PAYLOAD,
                String.join(", ", messages)
        );

        return ResponseEntity.internalServerError().body(errorResponse.getResponse(req.getRequestURI()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> defaultExceptionHandler(HttpServletRequest req, HttpServletResponse res, Exception e) throws Exception {
        // If the exception is annotated with @ResponseStatus rethrow it and let
        // the framework handle it - like the OrderNotFoundException example
        // at the start of this post.
        // AnnotationUtils is a Spring Framework utility class.
        if (AnnotationUtils.findAnnotation
                (e.getClass(), ResponseStatus.class) != null)
            throw e;

        ErrorResponse error = new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error.getResponse(req.getRequestURI()));
    }
}
