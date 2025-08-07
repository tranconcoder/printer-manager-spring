package com.tvconss.printermanagerspring.controller;

import com.tvconss.printermanagerspring.dto.request.RefreshTokenRequest;
import com.tvconss.printermanagerspring.dto.request.auth.AuthLoginRequest;
import com.tvconss.printermanagerspring.dto.request.auth.AuthRegisterRequest;
import com.tvconss.printermanagerspring.dto.response.auth.AuthResponse;
import com.tvconss.printermanagerspring.dto.response.auth.JwtTokenPair;
import com.tvconss.printermanagerspring.service.AuthService;
import com.tvconss.printermanagerspring.service.KeyTokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService, KeyTokenService keyTokenService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody AuthRegisterRequest registerPayload) {
        AuthResponse authInformation = this.authService.register(registerPayload);

        return ResponseEntity.status(HttpStatus.CREATED).body(authInformation);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@Valid @RequestBody AuthLoginRequest loginPayload) {
        AuthResponse authInformation = this.authService.login(loginPayload);

        return ResponseEntity.accepted().body(authInformation);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<JwtTokenPair> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        JwtTokenPair jwtTokenPair = this.authService.refreshToken(request.getRefreshToken());

        return ResponseEntity.status(HttpStatus.CREATED).body(jwtTokenPair);
    }

    @GetMapping("/check-logged-in")
    public ResponseEntity<Boolean> checkLoggedIn() {
        return ResponseEntity.ok(true);
    }
}
