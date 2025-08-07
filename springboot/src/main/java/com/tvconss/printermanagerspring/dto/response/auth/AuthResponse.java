package com.tvconss.printermanagerspring.dto.response.auth;


import com.tvconss.printermanagerspring.dto.response.user.UserResponse;
import lombok.Data;

@Data
public class AuthResponse {

    private UserResponse user;
    private JwtTokenPair token;
}
