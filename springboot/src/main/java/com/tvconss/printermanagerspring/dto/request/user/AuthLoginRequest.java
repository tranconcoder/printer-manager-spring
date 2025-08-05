package com.tvconss.printermanagerspring.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AuthLoginRequest {

    @NotBlank(message =  "Email is required!")
    private String email;

    @NotBlank(message = "Password is required!")
    private String password;
}
