import { css } from '@emotion/react';

export const base = () => css`
  width: 100%;
  height: 100%;
  background: linear-gradient(
    135deg,
    #e2e8f0 0%,
    #f1f5f9 25%,
    #e2e8f0 50%,
    #f1f5f9 75%,
    #e2e8f0 100%
  );
  background-size: 400% 400%;
  border-radius: 8px;
  position: relative;
  overflow: hidden;

  /* 기본 회색 배경 */
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: #f8fafc;
    border-radius: inherit;
    z-index: 1;
  }

  /* 대각선 그림자 효과 */
  &::after {
    content: '';
    position: absolute;
    top: -50%;
    left: -50%;
    width: 200%;
    height: 200%;
    background: linear-gradient(
      45deg,
      transparent 30%,
      rgba(255, 255, 255, 0.8) 50%,
      transparent 70%
    );
    transform: rotate(45deg);
    z-index: 2;
    animation: shimmer 1.8s infinite ease-in-out;
  }

  @keyframes shimmer {
    0% {
      transform: translateX(-100%) translateY(-100%) rotate(45deg);
    }
    100% {
      transform: translateX(100%) translateY(100%) rotate(45deg);
    }
  }
`;
