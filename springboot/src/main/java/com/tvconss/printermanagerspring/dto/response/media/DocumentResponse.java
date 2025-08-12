package com.tvconss.printermanagerspring.dto.response.media;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Data
public class DocumentResponse {
    private Long documentId;

    private String documentKey;

    private String documentName;

    private Long documentSize;

    private String documentThumb;

    private Long documentAuthorId;

    private String documentDescription;

    private Boolean isPublic = false;

    private String fileType;

    private List<String> mergeFields;

    private Date createdAt;

    private Date updatedAt;
}
