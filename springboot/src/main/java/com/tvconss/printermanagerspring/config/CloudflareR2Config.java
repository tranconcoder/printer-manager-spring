package com.tvconss.printermanagerspring.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@Slf4j
@Configuration
public class CloudflareR2Config {

    @Value("${cloudflare.r2.endpoint}")
    private String endPoint;

    @Value("${cloudflare.r2.access-key}")
    private String accessKey;

    @Value("${cloudflare.r2.secret-key}")
    private String secretKey;

    @Bean
    public S3Client s3Client() {
        S3Configuration serviceConfig = S3Configuration.builder()
                .pathStyleAccessEnabled(true)         // R2 needs path-style
                .chunkedEncodingEnabled(false)        // R2 does not support chunked uploads
                .checksumValidationEnabled(false)     // optional, avoids warning logs
                .build();

        return S3Client.builder()
                .httpClientBuilder(ApacheHttpClient.builder())
                .endpointOverride(URI.create(endPoint))
                .region(Region.of("apac"))              // <-- important
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKey, secretKey)))
                .serviceConfiguration(serviceConfig)
                .build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .endpointOverride(URI.create(endPoint))
                .region(Region.of("apac"))              // <-- important
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }
}