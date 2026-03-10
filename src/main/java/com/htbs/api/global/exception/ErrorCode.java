package com.htbs.api.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 400 BAD_REQUEST
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    // 401 UNAUTHORIZED
    TOKEN_NOT_MATCH(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    LOGGED_OUT_TOKEN(HttpStatus.UNAUTHORIZED, "로그아웃된 토큰입니다."),

    // 404 NOT_FOUND
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),

    // 409 CONFLICT
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 가입된 이메일입니다.");

    private final HttpStatus status;
    private final String message;
}
