package com.f12.moitz.application;

import com.f12.moitz.domain.Place;
import com.f12.moitz.domain.repository.SubwayStationRepository;
import com.f12.moitz.domain.subway.SubwayStation;
import com.f12.moitz.domain.subway.SubwayStationEntity;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
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

    private final static int DISTANCE_VALUE = 10;

    private final SubwayStationRepository subwayStationRepository;
    private final GeometryFactory geometryFactory;

    public List<SubwayStation> getAll() {
        return subwayStationRepository.findAll().stream()
                .map(SubwayStationEntity::toDomain)
                .toList();
    }

    public List<String> findAllStationNames() {
        return subwayStationRepository.findAll().stream()
                .map(SubwayStationEntity::toDomain)
                .map(Place::getName)
                .toList();
    }

    public SubwayStation getByName(final String name) {
        return subwayStationRepository.findByName(name)
                .orElseThrow(() -> new NoSuchElementException("이름이 일치하는 지하철역이 존재하지 않습니다. 역 이름: " + name))
                .toDomain();
    }

    public Optional<SubwayStation> findByName(final String name) {
        if ("총신대입구역".equals(name) || "이수역".equals(name)) {
            Optional<SubwayStation> subwayStationEntity = getSubwayStation("총신대입구(이수)역");
            if (subwayStationEntity.isPresent()) {
                return subwayStationEntity;
            }
        }
        Optional<SubwayStation> subwayStation = getSubwayStation(name);
        if (subwayStation.isPresent()) {
            return subwayStation;
        }
        return Optional.empty();
    }

    @Nullable
    private Optional<SubwayStation> getSubwayStation(String stationName) {
        Optional<SubwayStationEntity> subwayStationEntity = subwayStationRepository.findByName(stationName);
        return subwayStationEntity.map(stationEntity -> Optional.ofNullable(stationEntity.toDomain())).orElse(null);
    }

    public long getCount() {
        return subwayStationRepository.count();
    }

    public void saveAll(final List<SubwayStation> subwayStations) {
        List<SubwayStationEntity> subwayStationEntities = subwayStations.stream()
                .map(SubwayStationEntity::toFromSubwayStation)
                .toList();
        subwayStationRepository.saveAll(subwayStationEntities);
    }

    public List<SubwayStation> generateCandidatePlace(List<SubwayStation> startingStations) {

        List<SubwayStationEntity> stationEntities = startingStations.stream()
                .map(SubwayStationEntity::toFromSubwayStation)
                .toList();

        Coordinate[] coordinateArr = getCoordinates(stationEntities);

        org.springframework.data.geo.Point center = getCenterPoint(coordinateArr);
        Distance distance;
            distance = new Distance(DISTANCE_VALUE, Metrics.KILOMETERS);

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

        return new org.springframework.data.geo.Point(centroid.getX(), centroid.getY());
    }
}
