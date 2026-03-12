package com.htbs.api.global.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {
    private static final String REFRESH_TOKEN = "refresh_token";
    private final long maxAge;

    public CookieUtil(@Value("${jwt.refresh-expiration}") long maxAge) {
        this.maxAge = maxAge / 1000;
    }

    public ResponseCookie addCookie(String refreshToken) {
        return ResponseCookie.from(REFRESH_TOKEN, refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(maxAge)
                .sameSite("Strict")
                .build();
    }

    public ResponseCookie deleteCookie() {
        return ResponseCookie.from(REFRESH_TOKEN, "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();
    }
}
