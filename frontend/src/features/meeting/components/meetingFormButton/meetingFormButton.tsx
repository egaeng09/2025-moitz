import { useEffect, useRef } from 'react';

import BottomButton from '@shared/components/bottomButton/BottomButton';

function MeetingFormBottomButton({ active }: { active: boolean }) {
  const buttonRef = useRef<HTMLButtonElement>(null);

  useEffect(() => {
    if (active && buttonRef.current) {
      buttonRef.current.focus({ preventScroll: true });
      buttonRef.current.scrollIntoView({ behavior: 'smooth', block: 'center' });
    }
  }, [active]);

  return (
    <BottomButton
      ref={buttonRef}
      type="submit"
      text="모임 지역 찾기"
      active={active}
    />
  );
}

export default MeetingFormBottomButton;
