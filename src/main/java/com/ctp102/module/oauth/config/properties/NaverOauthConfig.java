package com.ctp102.module.oauth.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("oauth.naver")
@Configuration
@Getter
@Setter
public class NaverOauthConfig {

    private String endPoint;
    private String clientId;
    private String clientSecret;

}
