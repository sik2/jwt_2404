package com.sb.jwt.global.jwt;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;

@Component
public class JwtProvider {
    private SecretKey cachedSecretKey;
    @Value("${custom.jwt.secretKey}")
    private String originSecretKey;

    private SecretKey _getSecretKey () {
        String keyBase64Encoded =  Base64.getEncoder().encodeToString(originSecretKey.getBytes());

        return Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());
    }

    public SecretKey getSecretKey() {
        if (cachedSecretKey == null) cachedSecretKey = _getSecretKey();
        return cachedSecretKey;
    }

}
