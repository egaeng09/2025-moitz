package com.f12.moitz.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.f12.moitz.domain.repository.SubwayStationRepository;
import com.f12.moitz.domain.subway.SubwayStation;
import com.f12.moitz.infrastructure.persistence.SubwayStationEntity;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

@ExtendWith(MockitoExtension.class)
class SubwayStationServiceTest {

    @Mock
    private SubwayStationRepository subwayStationRepository;

    @InjectMocks
    private SubwayStationService subwayStationService;

    @DisplayName("'이수역' 또는 '총신대입구역'으로 지하철 역 검색 시 총신대입구(이수)역을 반환한다.")
    @Test
    void convertName() {
        // Given
        final String expectedName = "총신대입구(이수)역";
        final SubwayStationEntity expectedStation = new SubwayStationEntity(expectedName, new GeoJsonPoint(125, 34));

        Mockito.when(subwayStationRepository.findByName("총신대입구(이수)역")).thenReturn(Optional.of(expectedStation));

        // When
        final Optional<SubwayStation> station1 = subwayStationService.findByName("이수역");
        final Optional<SubwayStation> station2 = subwayStationService.findByName("총신대입구역");

        // Then
        assertThat(station1).contains(expectedStation.toSubwayStation());
        assertThat(station1.get().getName()).isEqualTo(expectedName);
        assertThat(station2).contains(expectedStation.toSubwayStation());
        assertThat(station2.get().getName()).isEqualTo(expectedName);
    }

}
