package com.ctp102.module.oauth.core;

import com.ctp102.module.oauth.config.properties.KakaoOauthConfig;
import com.ctp102.module.oauth.core.domain.KakaoOauthTokenDomain;
import com.ctp102.module.oauth.core.domain.KakaoOauthResponse;
import com.ctp102.module.oauth.core.domain.KakaoUserDomain;
import com.ctp102.module.oauth.core.form.KakaoOauthTokenForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Getter
public class KakaoRestClient {

    private final KakaoOauthConfig oauthConfig;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public KakaoRestClient(KakaoOauthConfig oauthConfig, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.oauthConfig = oauthConfig;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public KakaoOauthResponse<KakaoOauthTokenDomain> getOauthInfo(KakaoOauthTokenForm kakaoOauthTokenForm) {
        kakaoOauthTokenForm.setClient_id(oauthConfig.getClientId());
        kakaoOauthTokenForm.setClient_secret(oauthConfig.getClientSecret());

        return post(oauthConfig.getEndPoint() + "/oauth/token", KakaoOauthTokenDomain.class, kakaoOauthTokenForm); // 액세스 토큰 받기
    }

    public KakaoOauthResponse<KakaoUserDomain> getUser(String accessToken) {
        return get("https://kapi.kakao.com/v2/user/me", KakaoUserDomain.class, ImmutableMap.of("accessToken", accessToken));
    }

    /**
     * Object... 로 받은 이유는 Map, Form, Domain 모두 확장성 있게 처리하기 위함
     */
    private <T> KakaoOauthResponse<T> get(String apiUri, Class<T> toValueType, Object... params) {
        Map<String, Object> paramMap = getParamMap(apiUri, params);
        String fullUri = getRestUrlTemplate(apiUri, paramMap);

        Map restResultMap = new HashMap();
        try {
            ResponseEntity<Map> responseEntity = restTemplate.exchange(fullUri, HttpMethod.GET, new HttpEntity(getHttpHeaders(paramMap)), Map.class, paramMap);
            restResultMap = responseEntity.getBody();
        } catch (HttpClientErrorException hce) {
            log.error("[Kakao-OAuth-API] HttpClientErrorException = ", hce);
            try {
                restResultMap = objectMapper.readValue(hce.getResponseBodyAsString(), Map.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            log.error("[Kakao-OAuth-API] Exception = ", e);
        }

        log.info("[Kakao-OAuth-API] [GET] apiUri = {}, params = {}, result = {}", apiUri, paramMap, restResultMap);

        return convertDataList(restResultMap, toValueType);
    }

    private <T> KakaoOauthResponse<T> post(String apiUri, Class<T> toValueType, KakaoOauthTokenForm kakaoOauthTokenForm) {
        MultiValueMap<String, Object> paramMap = getMultiValueMap(kakaoOauthTokenForm);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = getHttpEntity(paramMap);

        log.debug("[Kakao-OAuth-API] [POST] apiUri = {}, params = {}", apiUri, paramMap);

        Map restResult = new LinkedHashMap();
        try {
            ResponseEntity<Map> responseEntity = restTemplate.postForEntity(apiUri, httpEntity, Map.class);
            restResult = responseEntity.getBody();
        } catch (HttpClientErrorException hce) {
            log.error("[Kakao-OAuth-API] HttpClientErrorException = ", hce);
            try {
                restResult = objectMapper.readValue(hce.getResponseBodyAsString(), Map.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            log.error("[Kakao-OAuth-API] Exception = ", e);
        }

        log.info("[Kakao-OAuth-API] [POST] apiUri = {}, params = {}, result = {}", apiUri, paramMap, restResult);

        return convertDataList(restResult, toValueType);
    }

    private Map<String, Object> getParamMap(String apiUri, Object[] params) {
        Map<String, Object> paramMap = new HashMap<>();

        if (params == null) return paramMap;

        for (Object param : params) {
            if (param == null) {
                continue;
            }

            if (param instanceof Map) {
                // noinspection unchecked
                paramMap.putAll((Map)param);
            } else {
                setParamMap(apiUri, paramMap, param); // param: Form or Domain
            }
        }
        return paramMap;
    }

    private void setParamMap(String apiUri, Map<String, Object> paramMap, Object object) {
        // noinspection unchecked
        Map<String, Object> map = objectMapper.convertValue(object, Map.class);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key   = entry.getKey();
            Object value = entry.getValue();
            if (value != null && !apiUri.contains(key + "=")) {
                paramMap.put(key, value);
            }
        }
    }

    private HttpHeaders getHttpHeaders(Map<String, Object> paramMap) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + paramMap.get("accessToken"));
        return httpHeaders;
    }

    private String getRestUrlTemplate(String apiUri, Map<String, Object> paramMap) {
        StringBuilder fullUri = new StringBuilder(apiUri);
        if (paramMap == null) {
            return fullUri.toString();
        }

        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
            String key   = entry.getKey();
            Object value = entry.getValue();

            if (value != null && !fullUri.toString().contains("{" + key + "}") && !fullUri.toString().contains(key + "=")) {
                String separtor = apiUri.contains("?") ? "&" : "?";
                fullUri.append(separtor).append(key).append("=").append("{").append(key).append("}"); // ex) https://kapi.kakao.com/v2/user/me?accessKey={accessKey}
            }
        }
        return fullUri.toString();
    }
    
    private HttpEntity<MultiValueMap<String, Object>> getHttpEntity(MultiValueMap<String, Object> multiValueMap) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return new HttpEntity<>(multiValueMap, headers);
    }

    /**
     * Post 전송 전용
     */
    private MultiValueMap<String, Object> getMultiValueMap(Object object) {
        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();

        // TODO: 2022-11-29 readValue와 차이점 숙지 
        Map<String, Object> map = objectMapper.convertValue(object, Map.class);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            multiValueMap.add(entry.getKey(), entry.getValue());
        }
        return multiValueMap;
    }

    private <T> KakaoOauthResponse<T> convertDataList(Map<String, Object> restResult, Class<T> toValueType) {
        if (restResult != null) {
            KakaoOauthResponse<T> kakaoOauthResponse = new KakaoOauthResponse<>();
            kakaoOauthResponse.setData(objectMapper.convertValue(restResult, toValueType));
            return kakaoOauthResponse;
        }
        return null;
    }

}
