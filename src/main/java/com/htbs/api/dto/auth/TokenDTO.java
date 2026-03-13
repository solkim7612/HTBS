package com.htbs.api.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenDTO {
    private String accessToken;
    private String refreshToken;
    private String email;
    private String name;

    public static TokenDTO from(String accessToken, String refreshToken, String email, String name){
        return TokenDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .email(email)
                .name(name)
                .build();
    }
}
