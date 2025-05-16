package com.goodjunseon.user_api.global.common;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {

    // 필터 단계에서 확인되는 에러는 실제 에러코드 형태여야함.
    FAIL(false, 400, "요청에 실패하였습니다."),
    EXPIRED_TOKEN(false, 500, "만료된 토큰입니다."),
    NOT_FOUND(false, 404, "페이지를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(false, 500, "서버 내부 오류입니다."),


    // 1000: 성공~
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    // 2000: 유저 오류
    NOT_FOUND_MEMBER(false, 2000, "존재하지 않는 유저입니다."),
    // 추후 추가 예정



    // 8000: 공통 에러 (validation)
    INVALID_MEMBER_EMAIL(false, 8000, "요청 조건이 충족되지 않습니다.");

    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
