package com.tvconss.printermanagerspring.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.tvconss.printermanagerspring.enums.ErrorCode;
import com.tvconss.printermanagerspring.enums.MediaCategory;
import com.tvconss.printermanagerspring.enums.MediaSize;
import com.tvconss.printermanagerspring.exception.ErrorResponse;
import com.tvconss.printermanagerspring.service.CloudinaryService;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.TreeMap;

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

            if (url == null) {
                throw new ErrorResponse(ErrorCode.UPLOAD_FAILED, "Upload avatar failed");
            }

            return this.getAvatarUrl(userId, MediaSize.AVATAR_SMALL);
        } catch(ErrorResponse ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ErrorResponse(ErrorCode.UPLOAD_ERROR_INTERNAL, "Error on processing avatar upload");
        }
    }

    @Override
    public String getImageUrl(String publicId, MediaSize size) {
        return this.cloudinary.url()
                .resourceType("image")
                .transformation(new Transformation()
                        .width(size.getWidth())
                        .height(size.getHeight())
                        .crop("fill")
                        .gravity("g_auto")
                        .quality("auto:best")
                        .fetchFormat("auto")
                )
                .generate(publicId);
    }

    @Override
    public String getAvatarUrl(Long userId, MediaSize size) {
        String publicId = this.getPublicIdForAvatar(userId);

        return this.cloudinary.url()
                .resourceType("image")
                .transformation(new Transformation()
                        .aspectRatio("1:1")
                        .width(size.getWidth())
                        .crop("fill")
                        .gravity("g_auto")
                        .quality("auto:best")
                        .fetchFormat("auto")
                )
                .generate(publicId);
    }


    @Override
    public Map<String, String> getAvatarUrls(Long userId) {
        String publicId = this.getPublicIdForAvatar(userId);
        Map<String, String> urls = new TreeMap<>();

        for (MediaSize size : MediaSize.values()) {
            String key = String.format("%dx%d", size.getWidth(), size.getHeight());

            String url = this.cloudinary.url()
                .resourceType("image")
                .transformation(new Transformation()
                    .aspectRatio("1:1")
                    .width(size.getWidth())
                    .crop("fill")
                    .gravity("face")
                    .quality("auto:best")
                    .fetchFormat("auto")
                )
                .generate(publicId);

            urls.put(key, url);
        }

        urls.remove("@class");

        return urls;
    }

    public String getPublicIdForAvatar(Long userId) {
        return String.format("%s/%s",
            MediaCategory.MEDIA_AVATAR.getMediaCategory(),
            userId.toString()
        );
    }
}