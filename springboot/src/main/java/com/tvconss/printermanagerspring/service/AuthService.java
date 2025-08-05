package com.tvconss.printermanagerspring.service;

import com.tvconss.printermanagerspring.dto.request.user.AuthLoginRequest;
import com.tvconss.printermanagerspring.dto.request.user.AuthRegisterRequest;
import com.tvconss.printermanagerspring.dto.response.user.AuthResponse;

public interface AuthService {

    AuthResponse register(AuthRegisterRequest data);

    AuthResponse login(AuthLoginRequest data);

}
