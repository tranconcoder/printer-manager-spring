package com.tvconss.printermanagerspring.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@NotEmpty(message = "Update request cannot be empty")
public class UpdateUser {

    private String firstName;

    private String lastName;

    @Email(message = "Email is required!")
    private String email;

    @Pattern(regexp = "male|female", message = "Gender must be either male or female")
    private String gender;

}
