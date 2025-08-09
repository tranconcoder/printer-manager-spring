package com.tvconss.printermanagerspring.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.concurrent.TimeUnit;
import java.util.Base64;

@RedisHash("user:key_token")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeyTokenEntity {

    @Id
    private String key;

    private Long userId;
    private Long jti;
    private String publicKey;

    @TimeToLive(unit = TimeUnit.SECONDS)
    private Long ttl;
    
    public PublicKey getPublicKeyAsObject() {
        try {
            if (this.publicKey == null || this.publicKey.trim().isEmpty()) {
                throw new IllegalArgumentException("Public key string cannot be null or empty");
            }

            // Sanitize the Base64 string
            String sanitized = this.publicKey.trim();
            if (sanitized.startsWith("\"") && sanitized.endsWith("\"")) {
                sanitized = sanitized.substring(1, sanitized.length() - 1);
            }
            sanitized = sanitized.replaceAll("\\s", "");
            sanitized = sanitized.replaceAll("[^A-Za-z0-9+/=]", "");

            // Decode Base64 string to bytes
            byte[] keyBytes = Base64.getMimeDecoder().decode(sanitized);

            // Create X509EncodedKeySpec and generate PublicKey
            X509EncodedKeySpec spec = new java.security.spec.X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = java.security.KeyFactory.getInstance("RSA");

            return keyFactory.generatePublic(spec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Failed to convert public key", e);
        }
    }

}
