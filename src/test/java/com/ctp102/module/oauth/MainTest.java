package com.ctp102.module.oauth;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * 막 사용하는 곳
 */
@Slf4j
public class MainTest extends CommonTest{

    @Test
    void uriComponentTest() {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.set("name", "kevin");
        multiValueMap.set("age", String.valueOf(100));

        URI uri = UriComponentsBuilder.fromUriString("http://localhost:8080")
                .path("/a/b/c")
                .queryParams(multiValueMap)
                .build()
                .toUri();

        printJson(uri);
    }

}
