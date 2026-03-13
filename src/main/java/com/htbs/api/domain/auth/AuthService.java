package com.htbs.api.domain.auth;

import com.htbs.api.dto.auth.LoginRequest;
import com.htbs.api.dto.auth.SignupRequest;
import com.htbs.api.dto.auth.TokenDTO;
import com.htbs.api.entity.user.User;
import com.htbs.api.global.exception.CustomException;
import com.htbs.api.global.exception.ErrorCode;
import com.htbs.api.global.security.JwtProvider;
import com.htbs.api.repository.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public TokenDTO login(LoginRequest request) {
        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        String accessToken = jwt.createAccessToken(user.getEmail(), user.getRole().name());
        String refreshToken = jwt.createRefreshToken(user.getEmail());

        long refreshExp = jwt.getExp(refreshToken);
        redis.saveRefreshToken(user.getEmail(), refreshToken, refreshExp);

        return TokenDTO.from(accessToken, refreshToken, user.getEmail(), user.getName());
    }

    @Transactional
    public void logout(String bearer) {
        String accessToken = parseToken(bearer);

        String email = jwt.getEmail(accessToken);
        redis.deleteRefreshToken(email);

        long exp = jwt.getExp(accessToken);

        if (exp > 0) {
            redis.setBlacklist(accessToken, exp);
        }
    }

    @Transactional
    public TokenDTO refresh(String refreshToken) {
        if (!jwt.validateToken(refreshToken)) {
            throw new CustomException(ErrorCode.TOKEN_NOT_MATCH);
        }

        String email = jwt.getEmail(refreshToken);
        String oldRefreshToken = redis.getRefreshToken(email);

        if (oldRefreshToken == null || !oldRefreshToken.equals(refreshToken)) {
            redis.deleteRefreshToken(email);
            throw new CustomException(ErrorCode.TOKEN_NOT_MATCH);
        }

        User user = repository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String newAccessToken = jwt.createAccessToken(user.getEmail(), user.getRole().name());
        String newRefreshToken = jwt.createRefreshToken(user.getEmail());

        long refreshExp = jwt.getExp(newRefreshToken);
        redis.saveRefreshToken(user.getEmail(), newRefreshToken, refreshExp);

        return TokenDTO.from(newAccessToken, newRefreshToken, user.getEmail(), user.getName());
    }

    private String parseToken(String bearer) {
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        throw new CustomException(ErrorCode.TOKEN_NOT_MATCH);
    }
}
