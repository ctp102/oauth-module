package com.ctp102.module.oauth.oauth.form;

import lombok.Data;

/**
 * https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#request-token-request
 */
@Data
public class KakaoOauthTokenForm {

    private String grant_type = "authorization_code";
    private String client_id; // rest api key
    private String redirect_uri;
    private String code; // kakao Authorize 후 받은 인가 코드
    private String client_secret; // not required

}
