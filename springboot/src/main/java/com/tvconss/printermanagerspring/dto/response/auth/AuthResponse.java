package com.tvconss.printermanagerspring.dto.response.auth;


import com.tvconss.printermanagerspring.dto.response.user.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private UserResponse user;
    private JwtTokenPair token;
}
