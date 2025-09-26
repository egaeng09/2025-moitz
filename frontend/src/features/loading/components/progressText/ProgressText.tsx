import { useEffect, useState } from 'react';

import { flex, typography } from '@shared/styles/default.styled';

import * as progressText from './progressText.styled';

interface ProgressTextProps {
  text: string[];
}

function ProgressText({ text }: ProgressTextProps) {
  const [textIndex, setTextIndex] = useState(0);

  useEffect(() => {
    const interval = setInterval(() => {
      setTextIndex((prev) => (prev + 1) % text.length);
    }, 600);

    return () => clearInterval(interval);
  }, []);

  return (
    <span
      css={[
        flex({ align: 'center', justify: 'center' }),
        typography.b2,
        progressText.text(),
      ]}
    >
      {text[textIndex]}
    </span>
  );
}

export default ProgressText;
