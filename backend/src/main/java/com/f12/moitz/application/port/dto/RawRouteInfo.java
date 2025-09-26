package com.f12.moitz.application.port.dto;

import java.util.List;

public record RawRouteInfo(
        List<RawPathInfo> paths
) {

}
