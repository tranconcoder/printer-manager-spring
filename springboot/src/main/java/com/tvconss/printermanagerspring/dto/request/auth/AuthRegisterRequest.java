package com.tvconss.printermanagerspring.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AuthRegisterRequest {

    @NotBlank(message =  "Email is required!")
    @Email(message = "Please enter valid email value!")
    private String email;

    @NotBlank(message = "Password is required!")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,24}$",
            message = "Password must have lowercase, uppercase, number and special character; length 8-24")
    private String password;

    @NotBlank(message = "First name is required!")
    private String firstName;

    @NotBlank(message = "Last name is required!")
    private String lastName;

    @NotNull(message = "Gender is required")
    @Pattern(regexp = "male|female", message = "Gender must be either male or female")
    private String gender;

}
