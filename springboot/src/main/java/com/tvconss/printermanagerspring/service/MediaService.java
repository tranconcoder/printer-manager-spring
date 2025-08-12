package com.tvconss.printermanagerspring.service;

import com.tvconss.printermanagerspring.dto.response.media.DocumentResponse;
import org.springframework.data.web.PagedModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MediaService {

    public String uploadDocumentFile(MultipartFile file, Long userId, String description);

    public PagedModel<DocumentResponse> getAllUserMedia(Long userId, int page, int size);

    public List<String> extractMergeFields(MultipartFile file);
}
