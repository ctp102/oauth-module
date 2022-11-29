package com.ctp102.module.oauth.core;

import com.ctp102.module.oauth.config.properties.GoogleOauthConfig;
import com.ctp102.module.oauth.core.domain.KakaoOauthTokenDomain;
import com.ctp102.module.oauth.core.domain.KakaoOauthTokenResponse;
import com.ctp102.module.oauth.core.form.KakaoOauthTokenForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Slf4j
public class GoogleRestClient {

    private final GoogleOauthConfig oauthConfig;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public GoogleRestClient(GoogleOauthConfig oauthConfig, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.oauthConfig = oauthConfig;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public KakaoOauthTokenResponse<KakaoOauthTokenDomain> getOauthInfo(KakaoOauthTokenForm kakaoOauthTokenForm) {
        kakaoOauthTokenForm.setClient_id(oauthConfig.getClientId());

        return post(oauthConfig.getEndPoint() + "/oauth/token", kakaoOauthTokenForm, KakaoOauthTokenDomain.class); // 액세스 토큰 받기
    }

    private <T> KakaoOauthTokenResponse<T> post(String apiUri, KakaoOauthTokenForm kakaoOauthTokenForm, Class<T> toValueType) {
        MultiValueMap<String, Object> paramMap = getMultiValueMap(kakaoOauthTokenForm);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = getHttpEntity(paramMap);

        log.debug("[Kakao-OAuth-API] [POST] apiUri = {}, params = {}", apiUri, paramMap);

        Map restResult = new LinkedHashMap();
        try {
            ResponseEntity<Map> responseEntity = restTemplate.postForEntity(apiUri, httpEntity, Map.class);
            restResult = responseEntity.getBody();
        } catch (HttpClientErrorException httpClientErrorException) {
            log.error("[Kakao-OAuth-API] HttpClientErrorException = ", httpClientErrorException);
            try {
                restResult = objectMapper.readValue(httpClientErrorException.getResponseBodyAsString(), Map.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            log.error("[Kakao-OAuth-API] Exception = ", e);
        }

        log.info("[Kakao-OAuth-API] [POST] apiUri = {}, params = {}, result = {}", apiUri, paramMap, restResult);

        return convertDataList(restResult, toValueType);
    }

    private HttpEntity<MultiValueMap<String, Object>> getHttpEntity(MultiValueMap<String, Object> multiValueMap) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return new HttpEntity<>(multiValueMap, headers);
    }

    private MultiValueMap<String, Object> getMultiValueMap(Object object) {
        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();

        Map<String, Object> map = objectMapper.convertValue(object, Map.class);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            multiValueMap.add(entry.getKey(), entry.getValue());
        }
        return multiValueMap;
    }

    private <T> KakaoOauthTokenResponse<T> convertDataList(Map<String, Object> restResult, Class<T> toValueType) {
        if (restResult != null) {
            KakaoOauthTokenResponse<T> kakaoOauthTokenResponse = new KakaoOauthTokenResponse<>();
            kakaoOauthTokenResponse.setData(objectMapper.convertValue(restResult, toValueType));
            return kakaoOauthTokenResponse;
        }
        return null;
    }
}
