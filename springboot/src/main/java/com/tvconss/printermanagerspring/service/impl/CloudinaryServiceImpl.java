package com.tvconss.printermanagerspring.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.tvconss.printermanagerspring.enums.ErrorCode;
import com.tvconss.printermanagerspring.enums.MediaCategory;
import com.tvconss.printermanagerspring.exception.ErrorResponse;
import com.tvconss.printermanagerspring.service.CloudinaryService;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public Map uploadImage(MultipartFile imageFile, MediaCategory mediaCategory) {
        try{
            Map params = ObjectUtils.asMap(
                "folder", mediaCategory.getMediaCategory()
            );

            return this.cloudinary.uploader().upload(imageFile.getBytes(), params);
        } catch(IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Map uploadAvatar(MultipartFile imageFile, Long userId) {
        try {
            String publicId = String.format("user/avatar/%d", userId);

            Map params = ObjectUtils.asMap(
                "folder", MediaCategory.MEDIA_AVATAR.getMediaCategory(),
                "public_id", publicId,
                "overwrite", true
            );

            return this.cloudinary.uploader().upload(imageFile.getBytes(), params);
        } catch (IOException ex) {
            throw new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR, ex.getMessage());
        }

    }
}