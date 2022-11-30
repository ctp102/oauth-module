package com.ctp102.module.oauth.core;

import com.ctp102.module.oauth.config.properties.NaverOauthConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class NaverRestClient {

    private final NaverOauthConfig oauthConfig;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public NaverRestClient(NaverOauthConfig oauthConfig, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.oauthConfig = oauthConfig;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

}
