package com.ctp102.module.oauth.core.service;

import com.ctp102.module.oauth.core.KakaoRestClient;
import com.ctp102.module.oauth.core.domain.KakaoOauthTokenDomain;
import com.ctp102.module.oauth.core.domain.KakaoOauthTokenResponse;
import com.ctp102.module.oauth.core.form.KakaoOauthTokenForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
@RequiredArgsConstructor
public class KakaoOauthService {

    private final KakaoRestClient kakaoRestClient;

    /**
     * 인가 코드 받기
     * https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#request-code
     *
     * @param redirectUri
     * @return
     */
    public String getOauthRedirectUri(String redirectUri) {
        return UriComponentsBuilder.fromUriString(kakaoRestClient.getOauthConfig().getEndPoint() + "/oauth/authorize")
                .queryParam("client_id", kakaoRestClient.getOauthConfig().getClientId())
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", "profile_nickname,profile_image,account_email,gender,age_range,birthday,friends,story_permalink,talk_message,openid")
                .toUriString();
    }

    public KakaoOauthTokenDomain getOauthInfo(KakaoOauthTokenForm kakaoOauthTokenForm) {
        KakaoOauthTokenResponse<KakaoOauthTokenDomain> oauthTokenResponse = kakaoRestClient.getOauthInfo(kakaoOauthTokenForm);
        return oauthTokenResponse.getData();
    }
}