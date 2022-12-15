package com.ctp102.module.oauth;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

        String uriStr = UriComponentsBuilder.fromUriString("http://localhost:8080")
                .path("/a/b/c")
                .queryParams(multiValueMap)
                .build()
                .toUriString();

        printJson(uri);
        printJson(uriStr);
    }

    @Test
    void unmodifiableTest() {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");

        List<String> unmodifiableList = Collections.unmodifiableList(list);
        List<String> immutableList = Collections.unmodifiableList(new ArrayList<String>(list));

        try {
            unmodifiableList.add("d");
            System.out.println("[unmodifiableList] cannot be reached here");
        } catch (UnsupportedOperationException e) {
            System.out.println("Cannot modify unmodifiable list");
        }

        try {
            immutableList.add("d");
            System.out.println("[immutableList] cannot be reached here");
        } catch (UnsupportedOperationException e) {
            System.out.println("Cannot modify immutableList list");
        }

        list.add("e");

        log.info("unmodifiableList.get(3) = {}", unmodifiableList.get(3));
        log.info("immutableList.get(3) = {}", immutableList.get(3)); // Index 3 out of bounds for length 3 발생(원본 객체에 값을 추가해도 immutable이기 때문에)
    }

}
