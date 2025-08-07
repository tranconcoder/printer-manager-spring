package com.tvconss.printermanagerspring.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthLoginRequest {

    @NotBlank(message =  "Email is required!")
    private String email;

    @NotBlank(message = "Password is required!")
    private String password;
}
