package com.tvconss.printermanagerspring.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateUser {

    private String firstName;

    private String lastName;

    @Pattern(regexp = "male|female", message = "Gender must be either male or female")
    private String gender;

}
