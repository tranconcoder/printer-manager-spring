package com.tvconss.printermanagerspring.controller;

import com.tvconss.printermanagerspring.dto.response.user.UserResponse;
import com.tvconss.printermanagerspring.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get")
    public ResponseEntity<UserResponse> getUser(HttpServletRequest request) {
        Claims jwtClaims = (Claims) request.getAttribute("jwtClaims");
        Long userId = jwtClaims.get("userId", Long.class);

        System.out.println("Fetching user with ID: " + userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(this.userService.getUserById(userId));
    }
}
