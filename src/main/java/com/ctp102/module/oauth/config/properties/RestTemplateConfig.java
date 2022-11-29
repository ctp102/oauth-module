package com.ctp102.module.oauth.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("rest.template")
public class RestTemplateConfig {

    private ConnectionManager connectionManager;
    private ClientHttpRequest clientHttpRequest;

    @Getter
    @Setter
    public static class ConnectionManager {
        private int defaultMaxPerRoute;
        private int maxTotal;
    }

    @Getter
    @Setter
    public static class ClientHttpRequest {
        private int evictIdleConnection;
        private int connectionTimeout;
        private int readTimeout;
    }

}
