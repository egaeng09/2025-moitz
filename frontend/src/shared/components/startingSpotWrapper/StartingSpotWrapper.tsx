import { CONDITION_CARD_TEXT } from '@features/meeting/constant/conditionCard';

import { StartingPlace } from '@entities/location/types/Location';
import { LocationRequirement } from '@entities/location/types/LocationRequirement';

import StartingSpotName from '@shared/components/startingSpotName/StartingSpotName';
import { flex, typography } from '@shared/styles/default.styled';

import * as startSpotWrapper from './startingSpotWrapper.styled';

interface StaringSpotWrapperProps {
  startingPlaces: StartingPlace[];
  conditionID: LocationRequirement;
}

function StartingSpotWrapper({
  startingPlaces,
  conditionID,
}: StaringSpotWrapperProps) {
  const entry = CONDITION_CARD_TEXT[conditionID];
  const conditionIdText =
    entry.ID === 'NOT_SELECTED' ? entry.TEXT : `${entry.TEXT} 장소`;

  return (
    <div
      css={[startSpotWrapper.base(), flex({ direction: 'column', gap: 10 })]}
    >
      <div css={[flex({ align: 'center', gap: 10 })]}>
        <span css={[typography.sh1, startSpotWrapper.title()]}>출발지</span>
        <div css={[flex({ wrap: 'wrap', gap: 5 })]}>
          {startingPlaces.map((place, index) => {
            const isLast = startingPlaces.length - 1 === index;
            return (
              <StartingSpotName
                key={place.index}
                place={place}
                isLast={isLast}
              />
            );
          })}
        </div>
      </div>
      <div css={[flex({ align: 'center', gap: 10 })]}>
        <span css={[typography.sh1, startSpotWrapper.title()]}>조건</span>
        <span css={[typography.b2, startSpotWrapper.content()]}>
          {conditionIdText}
        </span>
      </div>
    </div>
  );
}

export default StartingSpotWrapper;
