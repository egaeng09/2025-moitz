package com.f12.moitz.application.port.dto;

import java.util.List;

public record StationWithEdgesDto(
        String name,
        List<EdgeDto> possibleEdges
) {

}
