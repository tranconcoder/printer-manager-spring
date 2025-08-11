package com.tvconss.printermanagerspring.controller;

import com.tvconss.printermanagerspring.service.MediaService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @PostMapping("/upload")
    private ResponseEntity<String> uploadMedia(HttpServletRequest request, @RequestBody MultipartFile file) throws IOException {
        Claims jwtClaims = (Claims) request.getAttribute("jwtClaims");
        Long userId = jwtClaims.get("userId", Long.class);

        String key = this.mediaService.uploadDocumentFile(file, userId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(key);
    }
}
