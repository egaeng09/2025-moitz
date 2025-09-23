package com.f12.moitz.domain.subway;

import com.f12.moitz.domain.Point;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@RequiredArgsConstructor
@Getter
@Document(collection = "subway-stations")
public class SubwayStationEntity {

    private final String name;

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private final GeoJsonPoint point;


    public SubwayStation toDomain() {
        return new SubwayStation(name, new Point(point.getX(), point.getY()));
    }

    public static SubwayStationEntity toFromSubwayStation(SubwayStation subwayStation) {
        return new SubwayStationEntity(
                subwayStation.getName(),
                new GeoJsonPoint(
                        subwayStation.getPoint().getX(),
                        subwayStation.getPoint().getY()
                ));
    }
}
