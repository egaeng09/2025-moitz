package com.f12.moitz.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.f12.moitz.domain.Point;
import com.f12.moitz.domain.repository.SubwayStationRepository;
import com.f12.moitz.domain.subway.SubwayStation;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SubwayStationServiceTest {

    @Mock
    private SubwayStationRepository subwayStationRepository;

    @InjectMocks
    private SubwayStationService subwayStationService;

    @DisplayName("'이수'가 포함된 이름으로 지하철 역 검색 시 총신대입구역을 반환한다.")
    @Test
    void convertName() {
        // Given
        final String expectedName = "총신대입구역";
        final SubwayStation expectedStation = new SubwayStation(expectedName, new Point(125, 34));

        Mockito.when(subwayStationRepository.findByName("이수")).thenReturn(Optional.of(expectedStation));
        Mockito.when(subwayStationRepository.findByName("이수역")).thenReturn(Optional.of(expectedStation));

        // When
        String actualName1 = subwayStationService.getByName("이수").getName();
        String actualName2 = subwayStationService.getByName("이수역").getName();

        // Then
        assertThat(actualName1).isEqualTo(expectedName);
        assertThat(actualName2).isEqualTo(expectedName);
    }

}
