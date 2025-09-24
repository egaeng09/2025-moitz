package com.f12.moitz.domain.subway;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum SubwayLine {

    SEOUL_METRO_LINE1("1호선"),
    SEOUL_METRO_LINE2("2호선"),
    SEOUL_METRO_LINE3("3호선"),
    SEOUL_METRO_LINE4("4호선"),
    SEOUL_METRO_LINE5("5호선"),
    SEOUL_METRO_LINE6("6호선"),
    SEOUL_METRO_LINE7("7호선"),
    SEOUL_METRO_LINE8("8호선"),
    SEOUL_METRO_LINE9("9호선"),
    GTX_A("GTX-A"),
    AIRPORT_RAILROAD("공항철도"),
    GYEONGUI_JOUNGANG("경의중앙선"),
    EVERLINE("용인에버라인"),
    GYEONGCHUN("경춘선"),
    SIN_BUNDANG("신분당선"),
    UIJEONGBU_LRT("의정부경전철"),
    GYEONGGANG("경강선"),
    WOOE_SINSEUL_LRT("우이신설선"),
    SEOHAE_LINE("서해선"),
    GIMPO_LRT("김포골드라인"),
    SUIN_BUNDANG("수인분당선"),
    SILLIM_LRT("신림선"),
    INCHEON1("인천1호선"),
    INCHEON2("인천2호선");

    private final String title;

    SubwayLine(final String title) {
        this.title = title;
    }

    public static SubwayLine fromTitle(final String title) {
        return Arrays.stream(values())
                .filter(subwayLine -> subwayLine.title.equals(title))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(title + "에 해당하는 지하철 노선 정보가 존재하지 않습니다."));
    }

}
