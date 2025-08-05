package com.tvconss.printermanagerspring.service;

import com.tvconss.printermanagerspring.enums.MediaCategory;
import org.hibernate.mapping.Map;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {

    public Map uploadImage(MultipartFile imageFile, MediaCategory mediaCategory) throws IOException;
    
}
