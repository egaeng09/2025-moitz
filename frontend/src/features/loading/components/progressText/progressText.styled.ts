import { css } from '@emotion/react';

import { colorToken } from '@shared/styles/tokens';

export const text = () => css`
  width: 100%;
  min-height: 24px;
  text-align: center;
  color: ${colorToken.gray[8]};
`;
