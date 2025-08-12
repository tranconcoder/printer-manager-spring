package com.tvconss.printermanagerspring.dto.request.media;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadMedia {

    @NotNull
    private MultipartFile file;

    private String description;
}
