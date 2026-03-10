package com.htbs.api.domain.auth;

import com.htbs.api.dto.auth.LoginRequest;
import com.htbs.api.dto.auth.LoginResponse;
import com.htbs.api.dto.auth.SignupRequest;
import com.htbs.api.entity.user.User;
import com.htbs.api.global.exception.CustomException;
import com.htbs.api.global.exception.ErrorCode;
import com.htbs.api.global.security.JwtAuthenticationFilter;
import com.htbs.api.global.security.JwtProvider;
import com.htbs.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redis;
    private final JwtProvider jwt;

    @Transactional
    public Long signup(SignupRequest request) {
        if (repository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = User.createUser(request.getEmail(), encodedPassword, request.getName());

        return repository.save(user).getId();
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        String accessToken = jwt.createAccessToken(user.getEmail(), user.getRole().name());
        String refreshToken = jwt.createRefreshToken(user.getEmail());

        long refreshExp = jwt.getExp(refreshToken);
        redis.saveRefreshToken(user.getEmail(), refreshToken, refreshExp);

        return new LoginResponse(accessToken, refreshToken);
    }

    @Transactional
    public void logout(String bearer) {
        String accessToken = parseToken(bearer);

        String email = jwt.getEmail(accessToken);
        redis.deleteRefreshToken(email);

        long exp = jwt.getExp(accessToken);

        redis.setBlacklist(accessToken, exp);
    }

    private String parseToken(String bearer) {
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        throw new CustomException(ErrorCode.TOKEN_NOT_MATCH);
    }
}
