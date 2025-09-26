import {
  RecommendedLocation,
  StartingPlace,
} from '@entities/location/types/Location';
import { LocationRequirement } from '@entities/location/types/LocationRequirement';

import MeetingWrapper from '@shared/components/meetingWrapper/MeetingWrapper';

import SpotItemList from '../spotItemList/SpotItemList';

interface BottomSheetListProps {
  startingPlaces: StartingPlace[];
  recommendedLocations: RecommendedLocation[];
  conditionID: LocationRequirement;
  onSpotClick: (spot: RecommendedLocation) => void;
}

function BottomSheetList({
  startingPlaces,
  recommendedLocations,
  conditionID,
  onSpotClick,
}: BottomSheetListProps) {
  return (
    <>
      <MeetingWrapper
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
