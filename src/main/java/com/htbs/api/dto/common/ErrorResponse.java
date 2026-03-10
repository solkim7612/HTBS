package com.htbs.api.dto.common;

import com.htbs.api.global.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {
    private final int status;
    private final String error;
    private final String message;

    public static ErrorResponse from(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .error(errorCode.name())
                .message(errorCode.getMessage())
                .build();
    }
}
