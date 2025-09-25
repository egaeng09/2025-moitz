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
  const mapRef = useCustomOverlays({
    startingLocations,
    recommendedLocations,
    selectedLocation,
    changeSelectedLocation,
  });

  return <div ref={mapRef} css={map.container()} />;
}

export default Map;
