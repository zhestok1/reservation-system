package com.example.reservation_system.model.dto;

import java.time.LocalDateTime;

public record ErrorResponseDto(
        String message,
        String detaildMessage,
        LocalDateTime errorTime
) {

}
