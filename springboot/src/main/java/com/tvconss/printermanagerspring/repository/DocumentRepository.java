package com.tvconss.printermanagerspring.repository;

import com.tvconss.printermanagerspring.entity.DocumentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface DocumentRepository extends CrudRepository<DocumentEntity, Long> {

    Page<DocumentEntity> findAllByDocumentAuthorId(Long documentAuthorId, Pageable pageable);
}
