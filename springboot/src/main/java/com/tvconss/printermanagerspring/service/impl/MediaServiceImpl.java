package com.tvconss.printermanagerspring.service.impl;

import com.tvconss.printermanagerspring.enums.ErrorCode;
import com.tvconss.printermanagerspring.exception.ErrorResponse;
import com.tvconss.printermanagerspring.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {

    private final S3Client awsS3Client;

    @Value("${cloudflare.r2.bucket-name}")
    private String bucketName;

    @Override
    public String uploadDocumentFile(MultipartFile file, Long userId) {
//        Get file metadata information
        String fileName = Optional.ofNullable(file.getOriginalFilename())
                .orElseThrow(() -> new ErrorResponse(ErrorCode.MEDIA_UNSUPPORTED_TYPE))
                .toLowerCase();

        String contentType = Optional.ofNullable(file.getContentType())
                .orElseThrow(() -> new ErrorResponse(ErrorCode.MEDIA_UNSUPPORTED_TYPE));

        String fileExtension = StringUtils.getFilenameExtension(fileName);

        String fileType =  switch (contentType) {
//            Microsoft Office Word
            case "application/msword",
                 "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> "word";

            default -> throw new ErrorResponse(ErrorCode.MEDIA_UNSUPPORTED_TYPE);
        };

        String key = String.format("/documents/%s/%d/%s.%s", fileType, userId, System.currentTimeMillis(), fileExtension);

//        Setup S3 PutObjectRequest
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(this.bucketName)
                .key(key)
                .contentType(contentType)
                .build();

//        Handle file upload
        try {
            byte[] fileBytes = file.getBytes();
            RequestBody requestBody = RequestBody.fromBytes(fileBytes);

            this.awsS3Client.putObject(request, requestBody);
        } catch (Exception e) {
            throw new ErrorResponse(ErrorCode.MEDIA_ERROR_INTERNAL, "Failed to upload document file!");
        }

//        Return the S3 key of the uploaded file
        return key;
    }
}
