package com.goodjunseon.user_api.global.dto;

import com.goodjunseon.user_api.global.response.ErrorType.ErrorType;
import com.goodjunseon.user_api.global.response.SuccessType.SuccessType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "공통 API 응답")
public class ApiRes<T> {

    @Schema(description = "응답코드", example = "M001")
    private final String code;

    @Schema(description = "응답 메시지", example = "회원 정보 조회 성공")
    private final String message;

    @Schema(description = "응답 데이터", nullable = true)
    private final T data;

    public static <T> ApiRes<T> success(SuccessType code, T data) {
        return new ApiRes<>(code.getCode(), code.getMessage(), data);
    }

    public static <T> ApiRes<T> fail(ErrorType code) {
        return new ApiRes<>(code.getCode(), code.getMessage(), null);
    }



    // 생성자
    public ApiRes(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

}
