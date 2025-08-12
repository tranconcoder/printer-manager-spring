package com.tvconss.printermanagerspring.dto.response.media;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class S3PresignResponse {

    private String url;
    private String publicKey;
    private Date expiredAt;
    private List<String> mergeFields;
}
