package com.tvconss.printermanagerspring.interceptor;

import com.tvconss.printermanagerspring.enums.ErrorCode;
import com.tvconss.printermanagerspring.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.header.Header;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null) {
            throw new ErrorResponse(ErrorCode.AUTH_FAILED, "Please enter access token");
        }

//        Validate token in header
        if (!authorizationHeader.startsWith("Bearer ")) {
            throw new ErrorResponse(ErrorCode.AUTH_FAILED, "Please use bearer token");
        }

//        Get token and verify
        String token = authorizationHeader.substring(7);
        System.out.println(token);

        return true;
    }
}
