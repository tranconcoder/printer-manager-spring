package com.tvconss.printermanagerspring.dto.internal.jwt;

import lombok.Data;

@Data
public class JwtPayload {

    private Long userId;
    private String userEmail;
    private String userFirstName;
    private String userLastName;
    private boolean userGender;
}
