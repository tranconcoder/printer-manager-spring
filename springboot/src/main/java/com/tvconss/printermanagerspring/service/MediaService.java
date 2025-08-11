package com.tvconss.printermanagerspring.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MediaService {

    public String uploadDocumentFile(MultipartFile file, Long userId);

    public List<String> extractMergeFields(MultipartFile file);
}
