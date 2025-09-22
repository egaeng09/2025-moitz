package com.f12.moitz.application;

import com.f12.moitz.domain.repository.SubwayStationRepository;
import com.f12.moitz.domain.subway.SubwayStation;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubwayStationService {

    private final SubwayStationRepository subwayStationRepository;

    public List<SubwayStation> findAll() {
        return subwayStationRepository.findAll();
    }

    public SubwayStation findByName(final String name) {
        return subwayStationRepository.findByName(name)
                .orElseThrow(() -> new NoSuchElementException("이름이 일치하는 지하철역이 존재하지 않습니다. 역 이름: " + name));
    }

    public List<SubwayStation> findByNames(final List<String> names) {
        return names.stream()
                .map(this::findByName)
                .toList();
    }

    public long getCount() {
        return subwayStationRepository.count();
    }

    public void saveAll(final List<SubwayStation> subwayStations) {
        subwayStationRepository.saveAll(subwayStations);
    }
}
