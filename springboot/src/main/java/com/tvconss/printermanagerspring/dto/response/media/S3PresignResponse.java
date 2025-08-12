package com.tvconss.printermanagerspring.dto.response.media;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class S3PresignResponse {

    private String url;
    private String key;
    private Date expiredAt;
    private List<String> mergeFields;
}
