const NAVER_MAP_API_KEY = process.env.NAVER_MAP_API_KEY;

export const loadNaverMapScript = (): Promise<void> => {
  return new Promise((resolve, reject) => {
    // 이미 로드된 경우 즉시 반환
    if (window.naver?.maps) {
      resolve();
      return;
    }

    // 이미 스크립트가 로딩 중인 경우 중복 방지
    const existingScript = document.querySelector(
      'script[src*="oapi.map.naver.com"]',
    ) as HTMLScriptElement;

    if (existingScript) {
      existingScript.addEventListener('load', () => resolve());
      existingScript.addEventListener('error', () =>
        reject(new Error('네이버 지도 API 로드 실패')),
      );
      return;
    }

    const script = document.createElement('script');
    script.src = `https://oapi.map.naver.com/openapi/v3/maps.js?ncpKeyId=${NAVER_MAP_API_KEY}`;
    script.async = true;

    script.onload = () => {
      // 스크립트 로드 후 naver 객체가 실제로 사용 가능한지 확인
      if (window.naver?.maps) {
        resolve();
      } else {
        reject(new Error('네이버 지도 API 객체 생성 실패'));
      }
    };

    script.onerror = () => reject(new Error('네이버 지도 API 로드 실패'));
    document.head.appendChild(script);
  });
};
