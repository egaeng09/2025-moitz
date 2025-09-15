import { getMeetingStorage } from '@entities/location/model/meetingStorage';
import {
  RecommendedLocation,
  StartingPlace,
} from '@entities/location/types/Location';

import StartingSpotWrapper from '@shared/components/startingSpotWrapper/StartingSpotWrapper';

import SpotItemList from '../spotItemList/SpotItemList';

interface BottomSheetListProps {
  startingPlaces: StartingPlace[];
  recommendedLocations: RecommendedLocation[];
  onSpotClick: (spot: RecommendedLocation) => void;
}

function BottomSheetList({
  startingPlaces,
  recommendedLocations,
  onSpotClick,
}: BottomSheetListProps) {
  const { conditionID } = getMeetingStorage();

  return (
    <>
      <StartingSpotWrapper
        startingPlaces={startingPlaces}
        conditionID={conditionID}
      />
      <SpotItemList
        recommendedLocations={recommendedLocations}
        onSpotClick={onSpotClick}
      />
    </>
  );
}

export default BottomSheetList;
