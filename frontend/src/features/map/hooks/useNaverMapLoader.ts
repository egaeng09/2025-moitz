import { useState, useEffect } from 'react';

import { loadNaverMapScript } from '../lib/loadNaverMapScript';

/**
 * 네이버 지도 API 로딩 상태를 관리하는 훅
 * 상위 컴포넌트에서 로딩 상태를 통합 관리할 수 있도록 분리
 */
interface NaverMapLoaderReturns {
  isScriptLoaded: boolean;
  isLoading: boolean;
  errorMessage: string | null;
}

export const useNaverMapLoader = (): NaverMapLoaderReturns => {
  const [isScriptLoaded, setIsScriptLoaded] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  useEffect(() => {
    const initializeScript = async () => {
      try {
        setIsLoading(true);
        setErrorMessage(null);
        await loadNaverMapScript();
        setIsScriptLoaded(true);
      } catch (err) {
        setErrorMessage(
          err instanceof Error ? err.message : '지도 로딩에 실패했습니다.',
        );
      } finally {
        setIsLoading(false);
      }
    };

    initializeScript();
  }, []);

  return {
    isScriptLoaded,
    isLoading,
    errorMessage,
  };
};
