package com.tvconss.printermanagerspring.dto.response.user;


import lombok.Data;

@Data
public class AuthResponse {

    private User user;
    private JwtTokenPair token;
}
