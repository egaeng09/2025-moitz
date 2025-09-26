package com.f12.moitz.infrastructure.client.open.dto;

import com.f12.moitz.infrastructure.client.open.dto.utils.YnToBooleanConverter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public record PathResponse(
        @JsonProperty("dptreStn")
        StationResponse departureStation,

        @JsonProperty("arvlStn")
        StationResponse arrivalStation,

        @JsonProperty("stnSctnDstc")
        int stationSectionDistance,

        @JsonProperty("reqHr")
        int requiredSeconds,

        @JsonProperty("wtngHr")
        int waitingSeconds,

        @JsonProperty("upbdnbSe")
        String direction,

        @JsonProperty("trainDptreTm")
        String trainDepartureTime,

        @JsonProperty("trainArvlTm")
        String trainArrivalTime,

        @JsonProperty("trsitYn")
        @JsonDeserialize(using = YnToBooleanConverter.class)
        boolean isTransfer,

        @JsonProperty("etrnYn")
        @JsonDeserialize(using = YnToBooleanConverter.class)
        boolean isExpress,

        @JsonProperty("nonstopYn")
        @JsonDeserialize(using = YnToBooleanConverter.class)
        boolean isNonStop
) {

}
