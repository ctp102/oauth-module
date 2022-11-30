package com.ctp102.module.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CommonTest {

    @Autowired
    private ObjectMapper objectMapper;

    protected void printJson(Object o) {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        System.out.println("=================================================================================================================");

        try {
            System.out.println(objectMapper.writeValueAsString(o));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
