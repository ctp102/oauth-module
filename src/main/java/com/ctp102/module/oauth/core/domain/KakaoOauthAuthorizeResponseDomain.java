package com.ctp102.module.oauth.core.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class KakaoOauthAuthorizeResponseDomain {

    private String code; // 사용자가 [동의하고 계속하기] 선택, 로그인 진행하는 경우
    private String state; // 요청 시 전달한 state 값과 동일한 값

    private String error; // 인증 실패 시 반환되는 에러 코드
    private String errorDescription; // 인증 실패 시 반환되는 에러 메시지

}
