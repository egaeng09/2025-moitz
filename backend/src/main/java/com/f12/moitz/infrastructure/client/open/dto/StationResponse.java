package com.f12.moitz.infrastructure.client.open.dto;

import com.f12.moitz.infrastructure.client.open.dto.utils.StationResponseDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = StationResponseDeserializer.class)
public record StationResponse(
        String stationName,
        String lineName
) {

    public static StationResponse of(final String stationName, final String lineName) {
        return new StationResponse(stationName, lineName);
    }

}
