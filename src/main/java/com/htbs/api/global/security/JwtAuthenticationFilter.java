package com.htbs.api.global.security;

import com.htbs.api.domain.auth.RedisService;
import com.htbs.api.global.exception.CustomException;
import com.htbs.api.global.exception.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwt;
    private final RedisService redis;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = parseToken(request);

            if (StringUtils.hasText(token) && jwt.validateToken(token)) {
                if (redis.isBlacklist(token)) {
                    log.warn("접근 차단: 로그아웃된 토큰입니다.");
                } else {
                    String email = jwt.getEmail(token);
                    String role = jwt.getRole(token);

                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
                    Authentication auth = new UsernamePasswordAuthenticationToken(
                            email, null, Collections.singletonList(authority)
                    );

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (CustomException e) {
            log.warn("JWT 인증 필터 에러: {}", e.getErrorCode().getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private String parseToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
