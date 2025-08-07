package com.tvconss.printermanagerspring.dto.response.auth;

import lombok.Data;

@Data
public class JwtTokenPair {

    private String accessToken;
    private String refreshToken;
}
