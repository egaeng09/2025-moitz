import { useMemo } from 'react';

import { useCustomOverlays } from '@features/map/hooks/useCustomOverlays';
import { SelectedLocation } from '@features/recommendation/types/SelectedLocation';

import {
  RecommendedLocation,
  StartingPlace,
} from '@entities/location/types/Location';

import * as map from './map.styled';

interface MapProps {
  startingLocations: StartingPlace[];
  recommendedLocations: RecommendedLocation[];
  selectedLocation: SelectedLocation;
  changeSelectedLocation: (location: SelectedLocation) => void;
}

function Map({
  startingLocations,
  recommendedLocations,
  selectedLocation,
  changeSelectedLocation,
}: MapProps) {
  const emptyProps = useMemo(
    () => ({
      startingLocations: [],
      recommendedLocations: [],
      selectedLocation: null,
      changeSelectedLocation: () => {},
    }),
    [],
  );

  // 스크립트가 로드된 후에만 지도 훅 사용
  const mapRef = useCustomOverlays(
    isScriptLoaded
      ? {
          startingLocations,
          recommendedLocations,
          selectedLocation,
          changeSelectedLocation,
        }
      : emptyProps,
  );


  return <div ref={mapRef} css={map.container()} />;
}

export default Map;
