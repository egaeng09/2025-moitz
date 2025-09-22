package com.f12.moitz.application;

import com.f12.moitz.application.port.PlaceFinder;
import com.f12.moitz.application.port.SubwayMapLoader;
import com.f12.moitz.application.port.dto.RawPathInfo;
import com.f12.moitz.application.port.dto.RawRouteInfo;
import com.f12.moitz.domain.subway.Edge;
import com.f12.moitz.domain.subway.SubwayEdges;
import com.f12.moitz.domain.subway.SubwayLine;
import com.f12.moitz.domain.subway.SubwayStation;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SetupService {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final SubwayStationService subwayStationService;
    private final SubwayEdgeService subwayEdgeService;
    private final SubwayMapLoader subwayMapLoader;
    private final PlaceFinder placeFinder;

    public void setup() {
        log.info("SubwayMapPathFinder 초기화 시작");

        // 지하철 노선도 데이터 존재하는지 확인
        if (subwayStationService.getCount() > 0 && subwayEdgeService.getCount() > 0) {
            return;
        }

        // TODO: 비교해서 필요한 것만 저장하도록 해야 할듯?

        // 지하철 데이터 로딩
        final List<RawRouteInfo> rawRoutes = subwayMapLoader.loadRawRoutes();

        // 기존 역 데이터를 삭제하고 지하철 데이터로부터 새로운 역 데이터 저장 (?)
        final List<String> stationNames = rawRoutes.stream()
                .flatMap(route -> route.paths().stream())
                .flatMap(path -> Stream.of(
                        path.departureStation().stationName(),
                        path.arrivalStation().stationName()
                ))
                .distinct()
                .toList();

        List<SubwayStation> subwayStations = placeFinder.findPlacesByNames(stationNames)
                .stream()
                .map(place -> new SubwayStation(place.getName(), place.getPoint()))
                .toList();

        subwayStationService.saveAll(subwayStations);

        // 역과 엣지 조립
        final SubwayEdges subwayEdges = assembleSubwayStations(subwayStations, rawRoutes);

        // 엣지 데이터 저장
        subwayEdgeService.saveAll(subwayEdges);
    }

    private SubwayEdges assembleSubwayStations(final List<SubwayStation> stations, final List<RawRouteInfo> rawRoutes) {
        Map<SubwayStation, List<Edge>> stationMap = new HashMap<>();
        for (RawRouteInfo rawRoute : rawRoutes) {
            final List<RawPathInfo> paths = rawRoute.paths();
            for (int i = 0; i < paths.size(); i++) {
                final RawPathInfo currentPath = paths.get(i);
                final String fromName = currentPath.departureStation().stationName();
                final String toName = currentPath.arrivalStation().stationName();
                final String fromLine = currentPath.departureStation().lineName();
                final String toLine = currentPath.arrivalStation().lineName();

                final SubwayStation fromStation = getStationByName(stations, fromName);
                final SubwayStation toStation = getStationByName(stations, toName);

                final int distance = currentPath.stationSectionDistance();
                final int travelTimeInSeconds = calculateTravelTime(currentPath, paths, i);

                final List<Edge> fromEdges = stationMap.computeIfAbsent(fromStation, k -> new ArrayList<>());
                final List<Edge> toEdges = stationMap.computeIfAbsent(toStation, k -> new ArrayList<>());

                fromEdges.add(new Edge(toStation, travelTimeInSeconds, distance, fromLine));
                if (currentPath.isTransfer()) {
                    fromEdges.add(new Edge(toStation, travelTimeInSeconds, distance, toLine));
                } else {
                    toEdges.add(new Edge(fromStation, travelTimeInSeconds, distance, fromLine));
                }
            }
        }
        final SubwayEdges subwayEdges = SubwayEdges.of(stationMap);
        addMissingData(subwayEdges);
        return subwayEdges;
    }

    private SubwayStation getStationByName(final List<SubwayStation> stations, final String fromName) {
        return stations.stream()
                .filter(station -> station.getName().equals(fromName))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("이름이 일치하는 SubwayStation이 존재하지 않습니다: " + fromName));
    }

    private int calculateTravelTime(final RawPathInfo currentPath, final List<RawPathInfo> allPaths,
                                    final int currentIndex) {
        if (currentPath.isTransfer()) {
            return currentPath.waitingSeconds() + currentPath.requiredSeconds();
        }

        if (currentIndex < allPaths.size() - 1) {
            final RawPathInfo nextPath = allPaths.get(currentIndex + 1);
            final String currentDepartureTime = currentPath.trainDepartureTime();
            final String nextDepartureTime = nextPath.trainDepartureTime();

            if (currentDepartureTime != null && nextDepartureTime != null) {
                return calculateTimeDifference(currentDepartureTime, nextDepartureTime);
            }
        }
        return currentPath.requiredSeconds();
    }

    private int calculateTimeDifference(final String currentDepartureTime, final String nextDepartureTime) {
        try {
            final LocalTime current = LocalTime.parse(currentDepartureTime, TIME_FORMATTER);
            LocalTime next = LocalTime.parse(nextDepartureTime, TIME_FORMATTER);
            if (next.isBefore(current)) {
                next = next.plusHours(24);
            }
            return (int) Duration.between(current, next).getSeconds();
        } catch (DateTimeException | ArithmeticException e) {
            throw new IllegalStateException("경로의 출발 시간을 파싱할 수 없습니다.");
        }
    }

    private void addMissingData(final SubwayEdges subwayEdges) {
        log.debug("addMissingData 호출됨. 추가 데이터 처리 시작");

        final SubwayStation joongrang = subwayEdges.getStationByName("중랑역");
        subwayEdges.addEdge(joongrang, new Edge(joongrang, 1200, 0, SubwayLine.GYEONGUI_JOUNGANG.getTitle()));
        subwayEdges.addEdge(joongrang, new Edge(joongrang, 1200, 0, SubwayLine.GYEONGCHUN.getTitle()));
        log.debug("중랑역 환승 Edge 추가됨: 경의중앙선, 경춘선");

        final SubwayStation euljiro4ga = subwayEdges.getStationByName("을지로4가역");
        subwayEdges.addEdge(euljiro4ga, new Edge(euljiro4ga, 550, 0, SubwayLine.SEOUL_METRO_LINE2.getTitle()));
        subwayEdges.addEdge(euljiro4ga, new Edge(euljiro4ga, 550, 0, SubwayLine.SEOUL_METRO_LINE5.getTitle()));
        log.debug("을지로4가역 환승 Edge 추가됨: 2호선, 5호선");

        final SubwayStation cheongnyangni = subwayEdges.getStationByName("청량리역");
        subwayEdges.addEdge(cheongnyangni, new Edge(cheongnyangni, 1000, 0, SubwayLine.GYEONGCHUN.getTitle()));
        log.debug("청량리역 환승 Edge 추가됨: 경춘선");

        final SubwayStation oido = subwayEdges.getStationByName("오이도역");
        subwayEdges.addEdge(oido, new Edge(oido, 500, 0, SubwayLine.SEOUL_METRO_LINE4.getTitle()));
        subwayEdges.addEdge(oido, new Edge(oido, 500, 0, SubwayLine.SUIN_BUNDANG.getTitle()));
        log.debug("오이도역 환승 Edge 추가됨: 4호선, 수인분당선");

        final SubwayStation jeongwang = subwayEdges.getStationByName("정왕역");
        subwayEdges.addEdge(jeongwang, new Edge(jeongwang, 500, 0, SubwayLine.SEOUL_METRO_LINE4.getTitle()));
        subwayEdges.addEdge(jeongwang, new Edge(jeongwang, 500, 0, SubwayLine.SUIN_BUNDANG.getTitle()));
        log.debug("정왕역 환승 Edge 추가됨: 4호선, 수인분당선");

        final SubwayStation singiloncheon = subwayEdges.getStationByName("신길온천역");
        subwayEdges.addEdge(singiloncheon, new Edge(singiloncheon, 500, 0, SubwayLine.SEOUL_METRO_LINE4.getTitle()));
        subwayEdges.addEdge(singiloncheon, new Edge(singiloncheon, 500, 0, SubwayLine.SUIN_BUNDANG.getTitle()));
        log.debug("신길온천역 환승 Edge 추가됨: 4호선, 수인분당선");

        final SubwayStation ansan = subwayEdges.getStationByName("안산역");
        subwayEdges.addEdge(ansan, new Edge(ansan, 500, 0, SubwayLine.SEOUL_METRO_LINE4.getTitle()));
        subwayEdges.addEdge(ansan, new Edge(ansan, 500, 0, SubwayLine.SUIN_BUNDANG.getTitle()));
        log.debug("안산역 환승 Edge 추가됨: 4호선, 수인분당선");

        final SubwayStation choji = subwayEdges.getStationByName("초지역");
        subwayEdges.addEdge(choji, new Edge(choji, 500, 0, SubwayLine.SEOUL_METRO_LINE4.getTitle()));
        subwayEdges.addEdge(choji, new Edge(choji, 500, 0, SubwayLine.SUIN_BUNDANG.getTitle()));
        log.debug("초지역 환승 Edge 추가됨: 4호선, 수인분당선");

        final SubwayStation gojan = subwayEdges.getStationByName("고잔역");
        subwayEdges.addEdge(gojan, new Edge(gojan, 500, 0, SubwayLine.SEOUL_METRO_LINE4.getTitle()));
        subwayEdges.addEdge(gojan, new Edge(gojan, 500, 0, SubwayLine.SUIN_BUNDANG.getTitle()));
        log.debug("고잔역 환승 Edge 추가됨: 4호선, 수인분당선");

        final SubwayStation jungang = subwayEdges.getStationByName("중앙역");
        subwayEdges.addEdge(jungang, new Edge(jungang, 500, 0, SubwayLine.SEOUL_METRO_LINE4.getTitle()));
        subwayEdges.addEdge(jungang, new Edge(jungang, 500, 0, SubwayLine.SUIN_BUNDANG.getTitle()));
        log.debug("중앙역 환승 Edge 추가됨: 4호선, 수인분당선");
    }

}
