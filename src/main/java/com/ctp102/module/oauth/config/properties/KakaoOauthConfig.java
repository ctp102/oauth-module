package com.ctp102.module.oauth.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("oauth.kakao")
@Configuration
@Getter
@Setter
public class KakaoOauthConfig {

    private String endPoint;
    private String clientId;
    private String clientSecret;

}
