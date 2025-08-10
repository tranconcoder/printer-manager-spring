package com.tvconss.printermanagerspring.service;

import org.springframework.web.multipart.MultipartFile;

public interface MediaService {

    public String uploadDocumentFile(MultipartFile file, Long userId);
}
