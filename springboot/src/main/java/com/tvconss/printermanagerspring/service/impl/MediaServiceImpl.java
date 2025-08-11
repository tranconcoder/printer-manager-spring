package com.tvconss.printermanagerspring.service.impl;

import com.tvconss.printermanagerspring.enums.ErrorCode;
import com.tvconss.printermanagerspring.exception.ErrorResponse;
import com.tvconss.printermanagerspring.service.MediaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class MediaServiceImpl implements MediaService {

    private final S3Client awsS3Client;

    @Value("${cloudflare.r2.bucket-name}")
    private String bucketName;

    @Override
    public String uploadDocumentFile(MultipartFile file, Long userId) {
        // Get file metadata information
        String fileName = Optional.ofNullable(file.getOriginalFilename())
                .orElseThrow(() -> new ErrorResponse(ErrorCode.MEDIA_UNSUPPORTED_TYPE))
                .toLowerCase();

        String contentType = Optional.ofNullable(file.getContentType())
                .orElseThrow(() -> new ErrorResponse(ErrorCode.MEDIA_UNSUPPORTED_TYPE));

        String fileExtension = StringUtils.getFilenameExtension(fileName);

        String fileType = switch (contentType) {
            // Microsoft Office Word
            case "application/msword",
                 "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> "word";
            default -> throw new ErrorResponse(ErrorCode.MEDIA_UNSUPPORTED_TYPE);
        };

        // Extract merge fields
        List<String> mergeFields = this.extractMergeFields(file);
        log.info("Extracted merge fields: {}", mergeFields);

        String key = String.format("/documents/%s/%d/%s.%s", fileType, userId, System.currentTimeMillis(), fileExtension);

        // Setup S3 PutObjectRequest
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(this.bucketName)
                .key(key)
                .contentType(contentType)
                .build();

        // Handle file upload
        try {
            byte[] fileBytes = file.getBytes();
            RequestBody requestBody = RequestBody.fromBytes(fileBytes);
            this.awsS3Client.putObject(request, requestBody);
        } catch (Exception e) {
            log.error("Failed to upload document file", e);
            throw new ErrorResponse(ErrorCode.MEDIA_ERROR_INTERNAL, "Failed to upload document file!");
        }

        // Return the S3 key of the uploaded file
        return key;
    }

    @Override
    public List<String> extractMergeFields(MultipartFile file) {
        List<String> mergeFields = new ArrayList<>();
        
        try (InputStream inputStream = file.getInputStream()) {
            // Configure docx4j to avoid JAXB binding issues
            System.setProperty("docx4j.jaxb.formatted.output", "false");
            System.setProperty("docx4j.model.datasource.TableBinding.MissingBindingException", "false");
            
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(inputStream);
            MainDocumentPart mainDocumentPart = wordMLPackage.getMainDocumentPart();

            // Get the raw XML content directly to avoid JAXB XPath issues
            String documentXml = null;
            try {
                documentXml = mainDocumentPart.getXML();
            } catch (Exception jaxbException) {
                log.warn("JAXB XML extraction failed, falling back to alternative method: {}", jaxbException.getMessage());
                // Fallback: try to get content without XML serialization
                try {
                    // Use document structure traversal instead of XML parsing
                    String textContent = mainDocumentPart.toString();
                    return extractMergeFieldsFromText(textContent);
                } catch (Exception fallbackException) {
                    log.error("All extraction methods failed", fallbackException);
                    throw new ErrorResponse(ErrorCode.MEDIA_ERROR_INTERNAL, "Failed to extract merge fields from document file!");
                }
            }
            
            if (documentXml != null) {
                // Use more precise regex patterns to extract clean field names
                // Pattern 1: MERGEFIELD instruction with field name
                Pattern mergeFieldPattern1 = Pattern.compile("MERGEFIELD\\s+([A-Za-z_][\\w]*)", Pattern.CASE_INSENSITIVE);

                // Pattern 2: MERGEFIELD with quotes
                Pattern mergeFieldPattern2 = Pattern.compile("MERGEFIELD\\s+\"([A-Za-z_][\\w]*)\"", Pattern.CASE_INSENSITIVE);

                // Pattern 3: Word merge field brackets (visual representation)
                Pattern mergeFieldPattern3 = Pattern.compile("«([A-Za-z_][\\w]*)»");

                // Pattern 4: Complex MERGEFIELD with formatting options - extract only the field name part
                Pattern mergeFieldPattern4 = Pattern.compile("MERGEFIELD\\s+([A-Za-z_][\\w]*)(?:\\s+\\\\[^>]*)?", Pattern.CASE_INSENSITIVE);

                Pattern[] patterns = {mergeFieldPattern1, mergeFieldPattern2, mergeFieldPattern3, mergeFieldPattern4};

                for (Pattern pattern : patterns) {
                    Matcher matcher = pattern.matcher(documentXml);
                    while (matcher.find()) {
                        String fieldName = matcher.group(1).trim();

                        // Additional cleaning to ensure we only get valid field names
                        if (fieldName.matches("[A-Za-z_][\\w]*") && !mergeFields.contains(fieldName)) {
                            mergeFields.add(fieldName);
                            log.debug("Found merge field: {}", fieldName);
                        }
                    }
                }

                // Additional pattern to catch field names in w:instrText elements
                Pattern instrTextPattern = Pattern.compile("<w:instrText[^>]*>([^<]*MERGEFIELD\\s+([A-Za-z_][\\w]*)[^<]*)</w:instrText>", Pattern.CASE_INSENSITIVE);
                Matcher instrMatcher = instrTextPattern.matcher(documentXml);
                while (instrMatcher.find()) {
                    String fieldName = instrMatcher.group(2).trim();
                    if (fieldName.matches("[A-Za-z_][\\w]*") && !mergeFields.contains(fieldName)) {
                        mergeFields.add(fieldName);
                        log.debug("Found merge field in instrText: {}", fieldName);
                    }
                }
            }

            log.info("Successfully extracted {} merge fields: {}", mergeFields.size(), mergeFields);
            return mergeFields;
            
        } catch (Exception e) {
            log.error("Failed to extract merge fields from document", e);
            throw new ErrorResponse(ErrorCode.MEDIA_ERROR_INTERNAL, "Failed to extract merge fields from document file: " + e.getMessage());
        }
    }
    
    private List<String> extractMergeFieldsFromText(String textContent) {
        List<String> mergeFields = new ArrayList<>();
        
        // Simple text-based extraction patterns with stricter field name validation
        Pattern pattern1 = Pattern.compile("MERGEFIELD\\s+([A-Za-z_][\\w]*)", Pattern.CASE_INSENSITIVE);
        Pattern pattern2 = Pattern.compile("«([A-Za-z_][\\w]*)»");
        Pattern pattern3 = Pattern.compile("<<([A-Za-z_][\\w]*)>>");

        Pattern[] patterns = {pattern1, pattern2, pattern3};
        
        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(textContent);
            while (matcher.find()) {
                String fieldName = matcher.group(1).trim();

                // Validate field name format and add if not already present
                if (fieldName.matches("[A-Za-z_][\\w]*") && !mergeFields.contains(fieldName)) {
                    mergeFields.add(fieldName);
                }
            }
        }
        
        return mergeFields;
    }
}
