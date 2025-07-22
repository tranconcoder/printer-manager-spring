package com.tvconss.printermanagerspring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(String username, String password) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/is-logged")
    public ResponseEntity<Boolean> isLoggedIn(String token) {
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUser(String token) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/hello")
    public ResponseEntity<?> hello() {
        return new ResponseEntity<>("Hello world!",HttpStatus.NOT_IMPLEMENTED);
    }
}
