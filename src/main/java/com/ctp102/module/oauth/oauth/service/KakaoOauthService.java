package com.ctp102.module.oauth.oauth.service;

import com.ctp102.module.oauth.oauth.KakaoRestClient;
import com.ctp102.module.oauth.oauth.domain.KakaoOauthResponse;
import com.ctp102.module.oauth.oauth.domain.KakaoOauthTokenDomain;
import com.ctp102.module.oauth.oauth.domain.KakaoUserDomain;
import com.ctp102.module.oauth.oauth.form.KakaoOauthTokenForm;
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
        KakaoOauthResponse<KakaoOauthTokenDomain> oauthTokenResponse = kakaoRestClient.getOauthInfo(kakaoOauthTokenForm);
        return oauthTokenResponse.getData();
    }

    public KakaoUserDomain getUser(String accessToken) {
        KakaoOauthResponse<KakaoUserDomain> kakaoUserDomain = kakaoRestClient.getUser(accessToken);
        return kakaoUserDomain.getData();
    }
}
