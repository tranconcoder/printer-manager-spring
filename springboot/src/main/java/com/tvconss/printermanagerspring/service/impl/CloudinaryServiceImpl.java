package com.tvconss.printermanagerspring.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.transformation.Layer;
import com.cloudinary.utils.ObjectUtils;
import com.tvconss.printermanagerspring.enums.ErrorCode;
import com.tvconss.printermanagerspring.enums.MediaCategory;
import com.tvconss.printermanagerspring.exception.ErrorResponse;
import com.tvconss.printermanagerspring.service.CloudinaryService;

import java.util.HashMap;
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

    @SuppressWarnings("unchecked")
    public String uploadAvatar(MultipartFile imageFile, Long userId) {
        try {
            Map<String, Object> params = new HashMap<>();

            params.put("folder", MediaCategory.MEDIA_AVATAR.getMediaCategory());
            params.put("public_id", userId.toString());
            params.put("overwrite", true);

            Map result = this.cloudinary.uploader().upload(imageFile.getBytes(), params);

            String url = result.get("secure_url").toString();
            String publicId  = result.get("public_id").toString();

            if (url == null || publicId == null) {
                throw new ErrorResponse(ErrorCode.UPLOAD_FAILED, "Upload avatar failed");
            }

            // Generate optimized avatar URL with proper transformations
            return this.cloudinary.url()
                    .resourceType("image")
                    .transformation(new Transformation()
                            .aspectRatio("1:1")
                            .width(120)
                            .crop("fill")
                            .gravity("face")
                            .quality("auto:best")
                            .fetchFormat("auto")
                    )
                    .generate(publicId);
        } catch(ErrorResponse ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ErrorResponse(ErrorCode.UPLOAD_ERROR_INTERNAL, "Error on processing avatar upload");
        }

    }
}