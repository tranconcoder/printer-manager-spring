package com.tvconss.printermanagerspring.service;

import com.tvconss.printermanagerspring.enums.MediaCategory;
import com.tvconss.printermanagerspring.enums.MediaSize;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface CloudinaryService {

    public Map<?, ?> uploadImage(MultipartFile imageFile, MediaCategory mediaCategory);

    public String uploadAvatar(MultipartFile imageFile, Long userId);

    public String getImageUrl(String publicId, MediaSize size);

    public String getAvatarUrl(Long userId, MediaSize size);

    public Map<String, String> getAvatarUrls(Long userId);

}
