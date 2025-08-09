package com.tvconss.printermanagerspring.controller;

import com.tvconss.printermanagerspring.dto.request.user.UpdateUser;
import com.tvconss.printermanagerspring.dto.response.user.UserResponse;
import com.tvconss.printermanagerspring.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile(HttpServletRequest request) {
        Claims jwtClaims = (Claims) request.getAttribute("jwtClaims");
        Long userId = jwtClaims.get("userId", Long.class);

        System.out.println("Fetching user with ID: " + userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(this.userService.getUserById(userId));
    }

    @PatchMapping("/profile")
    public ResponseEntity<UserResponse> updateProfile(HttpServletRequest request, @Valid  @RequestBody UpdateUser updateUserFields) {
        Claims jwtClaims = (Claims) request.getAttribute("jwtClaims");
        Long userId = jwtClaims.get("userId", Long.class);

        UserResponse updatedUser = this.userService.updateUser(userId, updateUserFields);

        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

}
