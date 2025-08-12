package com.tvconss.printermanagerspring.service;

import com.tvconss.printermanagerspring.dto.response.media.DocumentResponse;
import com.tvconss.printermanagerspring.dto.response.media.S3PresignResponse;
import org.springframework.data.web.PagedModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MediaService {

    public String uploadDocumentFile(MultipartFile file, Long userId, String description);

    public PagedModel<DocumentResponse> getAllUserMedia(Long userId, int page, int size);

    public S3PresignResponse getS3PresignUrl(Long authorId, Long fileId);

    public List<String> extractMergeFields(MultipartFile file);
}
