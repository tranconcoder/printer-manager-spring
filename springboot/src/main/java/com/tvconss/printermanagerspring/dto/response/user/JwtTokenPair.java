package com.tvconss.printermanagerspring.dto.response.user;

import lombok.Data;

@Data
public class JwtTokenPair {

    private String accessToken;
    private String refreshToken;
}
