package com.ctp102.module.oauth.oauth.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#request-token-response
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoOauthTokenDomain {

    // required
    private String tokenType = "bearer";
    private String accessToken;
    private String refreshToken;
    private long expiresIn; // 액세스 토큰과 ID 토큰의 만료 시간(초)
    private long refreshTokenExpiresIn; // 리프레시 토큰 만료 시간(초)

    // not required
    private String idToken; // OpenID Connect 확장 기능을 통해 발급되는 ID 토큰, Base64 인코딩 된 사용자 인증 정보 포함, 만료시간은 액세스토큰과 동일(https://developers.kakao.com/docs/latest/ko/kakaologin/common#oidc)
    private String scope; // 인증된 사용자의 정보 조회 권한 범위(범위가 여러 개일 경우, 공백으로 구분)

}
