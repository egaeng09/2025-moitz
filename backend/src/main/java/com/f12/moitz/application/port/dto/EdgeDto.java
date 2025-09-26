package com.f12.moitz.application.port.dto;

public record EdgeDto(
        String destination,
        int distance,
        String lineName,
        int timeInSeconds
) {

}
