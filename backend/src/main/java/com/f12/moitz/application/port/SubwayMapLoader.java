package com.f12.moitz.application.port;

import com.f12.moitz.application.port.dto.RawRouteInfo;
import java.util.List;

public interface SubwayMapLoader {

    List<RawRouteInfo> loadRawRoutes();

}
