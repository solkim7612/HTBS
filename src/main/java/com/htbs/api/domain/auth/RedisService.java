package com.htbs.api.domain.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final StringRedisTemplate template;

    public void saveRefreshToken(String email, String refreshToken, Long duration) {
        template.opsForValue().set("RT:" + email, refreshToken, duration, TimeUnit.MILLISECONDS);
    }

    public void deleteRefreshToken(String email) {
        template.delete("RT:" + email);
    }

    public void setBlacklist(String accessToken, Long duration) {
        template.opsForValue().set(accessToken, "logout", duration, TimeUnit.MILLISECONDS);
    }

    public boolean isBlacklist(String accessToken) {
        return template.hasKey(accessToken);
    }

    public String getRefreshToken(String email) {
        return template.opsForValue().get("RT:" + email);
    }
}
