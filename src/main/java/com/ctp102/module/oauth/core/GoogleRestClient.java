package com.ctp102.module.oauth.core;

import com.ctp102.module.oauth.config.properties.GoogleOauthConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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

}
