package com.f12.moitz.application.adapter;

import com.f12.moitz.application.SubwayEdgeService;
import com.f12.moitz.application.SubwayStationService;
import com.f12.moitz.application.port.AsyncRouteFinder;
import com.f12.moitz.application.port.dto.StartEndPair;
import com.f12.moitz.domain.Path;
import com.f12.moitz.domain.Route;
import com.f12.moitz.domain.subway.SubwayEdges;
import com.f12.moitz.domain.subway.SubwayPath;
import com.f12.moitz.domain.subway.SubwayStation;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class SubwayRouteFinderAsyncAdapter implements AsyncRouteFinder {

    private final SubwayStationService subwayStationService;
    private final SubwayEdges subwayEdges;

    public SubwayRouteFinderAsyncAdapter(
            @Autowired final SubwayStationService subwayStationService,
            @Autowired final SubwayEdgeService subwayEdgeService
    ) {
        this.subwayStationService = subwayStationService;
        this.subwayEdges = subwayEdgeService.getSubwayEdges();
    }

    @Async("asyncTaskExecutor")
    @Override
    public CompletableFuture<List<Route>> findRoutesAsync(final List<StartEndPair> placePairs) {
        return CompletableFuture.completedFuture(findRoutes(placePairs));
    }

    @Override
    public List<Route> findRoutes(final List<StartEndPair> placePairs) {
        return Flux.fromIterable(placePairs)
                .flatMapSequential(pair -> {
                            final SubwayStation startStation = subwayStationService.getByName(pair.start().getName());
                            final SubwayStation endStation = subwayStationService.getByName(pair.end().getName());
                            return Mono.delay(Duration.ofMillis(50))
                                    .then(convertPath(
                                            subwayEdges.findShortestTimePath(startStation, endStation)
                                    ));
                        },
                        5)
                .map(Route::new)
                .collectList()
                .block();
    }

    private Mono<List<Path>> convertPath(final List<SubwayPath> subwayPaths) {
        return Mono.just(subwayPaths.stream()
                .map(subwayPath -> new Path(
                        subwayPath.from(),
                        subwayPath.to(),
                        subwayPath.travelMethod(),
                        subwayPath.totalTime(),
                        subwayPath.line()
                ))
                .toList());
    }

}
