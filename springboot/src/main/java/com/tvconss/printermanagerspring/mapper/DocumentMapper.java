package com.tvconss.printermanagerspring.mapper;

import com.tvconss.printermanagerspring.dto.response.media.DocumentResponse;
import com.tvconss.printermanagerspring.entity.DocumentEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class DocumentMapper {
    public abstract DocumentResponse documentEntityToDocumentResponse(DocumentEntity documentEntity);
}
