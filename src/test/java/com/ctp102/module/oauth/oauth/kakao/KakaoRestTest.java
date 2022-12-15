package com.ctp102.module.oauth.oauth.kakao;

import com.ctp102.module.oauth.CommonTest;
import com.ctp102.module.oauth.oauth.KakaoRestClient;
import com.ctp102.module.oauth.oauth.service.KakaoOauthService;
import groovy.util.logging.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@Slf4j
public class KakaoRestTest extends CommonTest {

    @Autowired
    private KakaoOauthService kakaoOauthService;

    @Autowired
    private KakaoRestClient kakaoRestClient;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    void getOauthKakaoRedirectUri() {
        String redirectUri = "http://localhost:8080/oauth/kakao/callback";
        String oAuthRedirectUri = kakaoOauthService.getOauthRedirectUri(redirectUri);
        printJson(oAuthRedirectUri);
    }

    @Test
    void getKakaoUser() {
        String accessToken = "j_oADPFKC1rL5SN8fyXiu_-6m7hlXmiLI4gJg1l6Cj11mwAAAYTDEhi4";
        System.out.println(kakaoOauthService.getUser(accessToken));
    }

    @Test
    void getUserInfo() {
//        String apiUri = "https://kapi.kakao.com/v2/user/me";
        String accessToken = "j_oADPFKC1rL5SN8fyXiu_-6m7hlXmiLI4gJg1l6Cj11mwAAAYTDEhi4";

        URI apiUri = UriComponentsBuilder
                .fromUriString("https://kapi.kakao.com")
                .path("/v2/user/me")
                .build()
                .toUri();

        RequestEntity<Void> requestEntity = RequestEntity
                .get(apiUri)
                .headers(getHttpHeaders(accessToken))
                .build();

        ResponseEntity<Map> resultMap = restTemplate.getForEntity(apiUri, Map.class);
    }

    private HttpHeaders getHttpHeaders(String accessToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", "Bearer " + accessToken);
        return httpHeaders;
    }

}
