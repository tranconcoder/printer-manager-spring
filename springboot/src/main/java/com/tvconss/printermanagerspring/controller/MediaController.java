package com.tvconss.printermanagerspring.controller;

import com.tvconss.printermanagerspring.dto.request.media.UploadMedia;
import com.tvconss.printermanagerspring.dto.response.media.DocumentResponse;
import com.tvconss.printermanagerspring.dto.response.media.S3PresignResponse;
import com.tvconss.printermanagerspring.entity.DocumentEntity;
import com.tvconss.printermanagerspring.service.MediaService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @PostMapping("/upload")
    private ResponseEntity<String> uploadMedia(HttpServletRequest request,
                                               @RequestParam("file")  MultipartFile file,
                                               @RequestParam(name = "description", required = false) String description) {
        Claims jwtClaims = (Claims) request.getAttribute("jwtClaims");
        Long userId = jwtClaims.get("userId", Long.class);

        String key = this.mediaService.uploadDocumentFile(file, userId, description);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(key);
    }

    @GetMapping("/user/all")
    private ResponseEntity<PagedModel<DocumentResponse>> getAllUserMedia(@RequestParam(name = "page", defaultValue = "0") int page,
                                                                        @RequestParam(name = "size", defaultValue = "10") int size,
                                                                        HttpServletRequest request) {

        Claims jwtClaims = (Claims) request.getAttribute("jwtClaims");
        Long userId = jwtClaims.get("userId", Long.class);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.mediaService.getAllUserMedia(userId, page, size));
    }


    @GetMapping("/user/file")
    private ResponseEntity<S3PresignResponse> getUserFileById(@RequestParam("fileId") Long fileId, HttpServletRequest request) {
        Claims jwtClaims = (Claims) request.getAttribute("jwtClaims");
        Long userId = jwtClaims.get("userId", Long.class);

        S3PresignResponse documentResponse = this.mediaService.getS3PresignUrl(userId, fileId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(documentResponse);
    }
}
