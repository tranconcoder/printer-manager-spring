package com.tvconss.printermanagerspring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Table(name = "document")
@Entity
@Data
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
public class DocumentEntity {

    @Id
    @Column(name = "document_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long documentId;

    @Column(name = "document_key", nullable = false, unique = true)
    private String documentKey;

    @Column(name = "document_name", nullable = false)
    private String documentName;

    @Column(name = "document_size", nullable = false)
    private Long documentSize;

    @Column(name = "document_thumb")
    private String documentThumb;

    @Column(name = "document_author_id", nullable = false)
    private Long documentAuthorId;

    @Lob
    @Column(name = "document_description")
    private String documentDescription;

    @Column(name = "is_public", columnDefinition = "boolean default false")
    private Boolean isPublic = false;


    @Column(name = "file_type")
    private String fileType;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "document_merge_fields", joinColumns = @JoinColumn(name = "document_id"))
    @Column(name = "merge_field")
    private List<String> mergeFields;

    @Column(name = "checksum")
    private String checksum;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "deleted_at")
    private Date deletedAt;
}
