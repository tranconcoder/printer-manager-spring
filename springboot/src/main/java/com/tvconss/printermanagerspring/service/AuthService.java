package com.tvconss.printermanagerspring.service;

import com.tvconss.printermanagerspring.dto.request.user.AuthLoginRequest;
import com.tvconss.printermanagerspring.dto.request.user.AuthRegisterRequest;
import com.tvconss.printermanagerspring.dto.response.user.AuthResponse;
import com.tvconss.printermanagerspring.dto.response.user.JwtTokenPair;

public interface AuthService {

    AuthResponse register(AuthRegisterRequest data);

    AuthResponse login(AuthLoginRequest data);

    JwtTokenPair refreshToken(String refreshToken);

}
