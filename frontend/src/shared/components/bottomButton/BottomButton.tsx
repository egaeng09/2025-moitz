import React from 'react';

import { flex, typography } from '@shared/styles/default.styled';

import * as bottomButton from './bottomButton.styled';

interface BaseBottomButtonProps extends React.ComponentProps<'button'> {
  text: string;
  active: boolean;
}

interface SubmitBottomButtonProps extends BaseBottomButtonProps {
  type: 'submit';
  onClick?: () => void;
}

interface ButtonBottomButtonProps extends BaseBottomButtonProps {
  type: 'button';
  onClick: () => void;
}

type BottomButtonProps = SubmitBottomButtonProps | ButtonBottomButtonProps;

function BottomButton({ ref, type, text, active, onClick }: BottomButtonProps) {
  return (
    <button
      ref={ref}
      type={type}
      onClick={onClick}
      css={[
        flex({ justify: 'center', align: 'center' }),
        bottomButton.base(),
        active && bottomButton.active(),
      ]}
    >
      <span css={typography.h1}>{text}</span>
    </button>
  );
}

export default BottomButton;
