/* eslint-disable no-undef */
import React from 'react';
import { createRoot, type Root } from 'react-dom/client';

/* ===========================================
 * CustomOverlay
 *   - React 컴포넌트를 네이버 지도 위에 표시하는 오버레이
 * =========================================== */
type CustomOverlayProps = {
  position: naver.maps.LatLng;
  naverMap: naver.maps.Map;
  content: React.ReactNode;
  zIndex?: number;
};

/* ===========================================
 * CustomOverlay 인스턴스 타입 정의
 * =========================================== */
export type CustomOverlayInstance = {
  setMap: (map: naver.maps.Map | null) => void;
  onRemove: () => void;
};

/* ===========================================
 * createCustomOverlay - 동적 클래스 생성 함수
 * window.naver.maps가 로드된 후에 클래스를 생성
 * =========================================== */
export const createCustomOverlay = (
  props: CustomOverlayProps,
): CustomOverlayInstance => {
  const { position, naverMap, content, zIndex = 100 } = props;

  // 네이버 지도 API가 로드되지 않았으면 에러
  if (!window.naver?.maps?.OverlayView) {
    throw new Error('네이버 지도 API가 로드되지 않았습니다.');
  }

  // 동적으로 클래스 생성
  class CustomOverlay extends window.naver.maps.OverlayView {
    private position: naver.maps.LatLng;
    private container: HTMLDivElement;
    private reactRoot: Root;

    constructor() {
      super();

      this.position = position;

      this.container = document.createElement('div');
      this.container.style.position = 'absolute';
      this.container.style.zIndex = String(zIndex);

      this.reactRoot = createRoot(this.container);
      this.reactRoot.render(content);

      this.setMap(naverMap);
    }

    onAdd() {
      const { overlayLayer } = this.getPanes();
      overlayLayer.appendChild(this.container);
    }

    draw() {
      const projection = this.getProjection();
      const pixel = projection.fromCoordToOffset(
        this.position as unknown as naver.maps.Coord,
      );
      this.container.style.left = `${pixel.x}px`;
      this.container.style.top = `${pixel.y}px`;
    }

    onRemove() {
      setTimeout(() => {
        this.reactRoot.unmount();
        this.container.remove();
      }, 0);
    }
  }

  return new CustomOverlay();
};

// 기존 호환성을 위한 기본 export (deprecated)
export default createCustomOverlay;
