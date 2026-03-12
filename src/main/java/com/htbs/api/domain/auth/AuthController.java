package com.htbs.api.domain.auth;

import com.htbs.api.dto.auth.LoginRequest;
import com.htbs.api.dto.auth.LoginResponse;
import com.htbs.api.dto.auth.SignupRequest;
import com.htbs.api.global.exception.CustomException;
import com.htbs.api.global.exception.ErrorCode;
import com.htbs.api.global.security.CookieUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;
    private final CookieUtil util;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequest request) {
        service.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = service.login(request);

        ResponseCookie cookie = util.addCookie(response.getRefreshToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new LoginResponse(response.getAccessToken(), null));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String bearer) {
        service.logout(bearer);

        ResponseCookie cookie = util.deleteCookie();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("로그아웃 되었습니다.");
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(
            @CookieValue(value = "refresh_token", required = false) String refreshToken) {
        if (refreshToken == null) {
            throw new CustomException(ErrorCode.TOKEN_NOT_MATCH);
        }

        LoginResponse response = service.refresh(refreshToken);

        ResponseCookie cookie = util.addCookie(response.getRefreshToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new LoginResponse(response.getAccessToken(), null));
    }
}
