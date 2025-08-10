package com.tvconss.printermanagerspring.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateUserPut {

    @NotBlank(message = "First name is required!")
    private String firstName;

    @NotBlank(message = "Last name is required!")
    private String lastName;

    @NotBlank(message = "Gender is required")
    @Pattern(regexp = "male|female", message = "Gender must be either male or female")
    private String gender;
}
