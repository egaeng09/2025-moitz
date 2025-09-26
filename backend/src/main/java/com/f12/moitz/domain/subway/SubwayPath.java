package com.f12.moitz.domain.subway;

import com.f12.moitz.domain.TravelMethod;

public record SubwayPath(
        SubwayStation from,
        SubwayStation to,
        TravelMethod travelMethod,
        int totalTime,
        // TODO: 기존 Path와 묶을 방법 고민, 확장성을 고려했을 때 SubwayPath와 Path를 구분하는 게 맞을지?
        // TravelMethod와 lineName은 동일하게 따라간다. 그러면 어떻게 묶어낼 수 있지?
        // 예를 들어 버스인 경우에는 lineName이 버스 번호일 수도 있고, 지하철인 경우에는 노선 이름일 수도 있다.
        SubwayLine line
) {

}
