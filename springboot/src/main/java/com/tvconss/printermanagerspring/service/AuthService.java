package com.tvconss.printermanagerspring.service;

import com.tvconss.printermanagerspring.dto.request.auth.AuthLoginRequest;
import com.tvconss.printermanagerspring.dto.request.auth.AuthRegisterRequest;
import com.tvconss.printermanagerspring.dto.response.auth.AuthResponse;
import com.tvconss.printermanagerspring.dto.response.auth.JwtTokenPair;

public interface AuthService {

    AuthResponse register(AuthRegisterRequest data);

    AuthResponse login(AuthLoginRequest data);

    JwtTokenPair refreshToken(String refreshToken);

}
