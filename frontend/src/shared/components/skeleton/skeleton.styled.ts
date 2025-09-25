import { css } from '@emotion/react';

export const base = () => css`
  width: 100%;
  height: 100%;
  background: linear-gradient(
    90deg,
    #e2e8f0 0%,
    #f1f5f9 25%,
    #ffffff 50%,
    #f1f5f9 75%,
    #e2e8f0 100%
  );
  background-size: 300% 100%;
  border-radius: 8px;
  position: relative;
  overflow: hidden;
  animation: gradientShift 2.5s infinite ease-in-out;

  @keyframes gradientShift {
    0% {
      background-position: -100% 0;
    }
    50% {
      background-position: 100% 0;
    }
    100% {
      background-position: -100% 0;
    }
  }
`;
