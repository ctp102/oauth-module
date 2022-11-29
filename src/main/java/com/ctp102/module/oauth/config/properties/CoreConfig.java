package com.ctp102.module.oauth.config.properties;

import com.ctp102.module.oauth.core.KakaoRestClient;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class CoreConfig {

    @Bean(name = "objectMapper")
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return objectMapper;
    }

    @Bean(name = "restTemplate")
    public RestTemplate restTemplate(RestTemplateConfig restTemplateConfig) {
        // 1. ClientHttpRequestFactory requestFactory

        // 2. MessageConverters
        MappingJackson2HttpMessageConverter jsonHttpMessageConverter = new MappingJackson2HttpMessageConverter(objectMapper());
        jsonHttpMessageConverter.setSupportedMediaTypes(MediaType.parseMediaTypes("application/json"));
        jsonHttpMessageConverter.setPrefixJson(false);

        MappingJackson2HttpMessageConverter textJavascriptHttpMessageConverter = new MappingJackson2HttpMessageConverter(objectMapper());
        textJavascriptHttpMessageConverter.setSupportedMediaTypes(MediaType.parseMediaTypes("test/javascript"));
        textJavascriptHttpMessageConverter.setPrefixJson(false);

        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        stringConverter.setWriteAcceptCharset(false); // SPR-7316

        ByteArrayHttpMessageConverter byteArrayHttpMessageConverter = new ByteArrayHttpMessageConverter();

        FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
        formHttpMessageConverter.setCharset(StandardCharsets.UTF_8);

        List<HttpMessageConverter<?>> httpMessageConverters = new ArrayList<>();
        httpMessageConverters.add(jsonHttpMessageConverter);
        httpMessageConverters.add(textJavascriptHttpMessageConverter);
        httpMessageConverters.add(stringConverter);
        httpMessageConverters.add(byteArrayHttpMessageConverter);
        httpMessageConverters.add(formHttpMessageConverter);

        // 3. RestTemplate
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setMessageConverters(httpMessageConverters);
        return restTemplate;
    }

    @Bean
    public KakaoRestClient kakaoRestClient(KakaoOauthConfig kakaoOauthConfig, RestTemplate restTemplate, ObjectMapper objectMapper) {
        return new KakaoRestClient(kakaoOauthConfig, restTemplate, objectMapper);
    }

}
