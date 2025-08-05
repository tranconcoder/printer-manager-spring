package com.tvconss.printermanagerspring.service;

import com.tvconss.printermanagerspring.enums.MediaCategory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface CloudinaryService {

    public Map uploadImage(MultipartFile imageFile, MediaCategory mediaCategory);

    public Map uploadAvatar(MultipartFile imageFile, Long userId);

}
