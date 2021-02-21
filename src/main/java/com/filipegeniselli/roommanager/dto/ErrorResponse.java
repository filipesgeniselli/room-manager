package com.filipegeniselli.roommanager.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {
    
    private final LocalDateTime time;
    private final String errorMessage;

    public ErrorResponse(String errorMessage) {
        this.time = LocalDateTime.now();
        this.errorMessage = errorMessage;
    }

}
