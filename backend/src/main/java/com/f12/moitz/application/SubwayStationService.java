package com.f12.moitz.application;

import com.f12.moitz.domain.repository.SubwayStationRepository;
import com.f12.moitz.domain.subway.SubwayStation;
import com.f12.moitz.domain.subway.SubwayStationEntity;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Point;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubwayStationService {

    private final static int DISTANCE_VALUE = 5;

    private final SubwayStationRepository subwayStationRepository;
    private final GeometryFactory geometryFactory;

    public List<SubwayStationEntity> findAll() {
        return subwayStationRepository.findAll();
    }

    public SubwayStation findByName(final String name) {
        return subwayStationRepository.findByName(name)
                .orElseThrow(() -> new NoSuchElementException("이름이 일치하는 지하철역이 존재하지 않습니다."));
    }

    public List<SubwayStation> findByNames(final List<String> names) {
        return names.stream()
                .map(this::findByName)
                .toList();
    }

    public long getCount() {
        return subwayStationRepository.count();
    }

    public void saveAll(final List<SubwayStationEntity> subwayStationEntity) {
        subwayStationRepository.saveAll(subwayStationEntity);
    }

    public List<SubwayStation> generateCandidatePlace(List<SubwayStation> startingStations) {
        List<SubwayStationEntity> stationEntities = startingStations.stream()
                .map(SubwayStationEntity::toFromSubwayStation)
                .toList();

        Coordinate[] coordinateArr = getCoordinates(stationEntities);

        org.springframework.data.geo.Point center = getCenterPoint(coordinateArr);

        Distance distance = new Distance(DISTANCE_VALUE, Metrics.KILOMETERS);

        return subwayStationRepository.findByPointNear(center, distance).stream()
                .map(SubwayStationEntity::toDomain)
                .toList();
    }

    private Coordinate[] getCoordinates(List<SubwayStationEntity> stationEntities) {
        return stationEntities.stream()
                .map(place -> {
                    List<Double> coordinates = place.getPoint().getCoordinates();
                    return new Coordinate(coordinates.get(0), coordinates.get(1));
                })
                .toArray(Coordinate[]::new);
    }

    private org.springframework.data.geo.Point getCenterPoint(Coordinate[] coordinateArr) {
        MultiPoint multiPoint = geometryFactory.createMultiPointFromCoords(coordinateArr);

        Point centroid = multiPoint.getCentroid();

        log.info("centroid : {}", centroid.getX() + " "  + centroid.getY());

        return new org.springframework.data.geo.Point(centroid.getX(), centroid.getY());
    }
}
