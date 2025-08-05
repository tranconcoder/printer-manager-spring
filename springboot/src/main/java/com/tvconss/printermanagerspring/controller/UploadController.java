package com.tvconss.printermanagerspring.controller;

import com.tvconss.printermanagerspring.enums.MediaCategory;
import com.tvconss.printermanagerspring.service.CloudinaryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/upload")
public class UploadController {

    private CloudinaryService cloudinaryService;

    public UploadController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping("/avatar")
    public ResponseEntity<Map>  uploadAvatar(@RequestBody MultipartFile imageFile) {
        Map response = this.cloudinaryService.uploadImage(imageFile, MediaCategory.MEDIA_AVATAR);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
