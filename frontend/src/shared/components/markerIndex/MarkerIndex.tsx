import { flex, typography, shadow } from '@shared/styles/default.styled';

import IconUser from '@icons/icon-marker-user.svg';

import * as marker from './markerIndex.styled';

interface MarkerIndexProps {
  index: number | string;
  type: 'recommended' | 'starting';
  label?: string;
  hasStroke?: boolean;
  hasShadow?: boolean;
}

function MarkerIndex({
  index,
  type,
  label = '',
  hasStroke = false,
  hasShadow = false,
}: MarkerIndexProps) {
  const isRecommended = type === 'recommended';
  const circle_type = isRecommended
    ? marker.circle_recommended
    : marker.circle_starting;
  const label_type = isRecommended
    ? marker.label_recommended
    : marker.label_starting;

  return (
    <div
      css={[
        flex({
          direction: 'column',
          justify: 'center',
          align: 'center',
          gap: 4,
        }),
      ]}
    >
      <div
        css={[
          flex({ justify: 'center', align: 'center' }),
          marker.circle_base(),
          circle_type(),
          hasStroke && marker.stroke(),
          hasShadow && shadow.map,
        ]}
      >
        {isRecommended ? (
          <span css={[typography.h3, marker.circle_font()]}>{index}</span>
        ) : (
          <img src={IconUser} alt="icon-marker-user" draggable={false} />
        )}
      </div>
      {label && (
        <p css={[typography.sh2, marker.label_base(), label_type()]}>{label}</p>
      )}
    </div>
  );
}

export default MarkerIndex;
