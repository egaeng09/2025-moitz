package com.f12.moitz.domain.repository;

import com.f12.moitz.domain.subway.SubwayEdge;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SubwayEdgeRepository extends MongoRepository<SubwayEdge, String> {

}
