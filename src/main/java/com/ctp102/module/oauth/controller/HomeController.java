package com.ctp102.module.oauth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Enumeration;
import java.util.Locale;

@Controller
@Slf4j
public class HomeController {

    @GetMapping("/")
    public String getHome() {
        return "index";
    }

    @GetMapping("/test")
    @ResponseBody
    public String getTest(HttpServletRequest request, HttpServletResponse response) {

        // 1. locale
        Enumeration<Locale> locales = request.getLocales();
        log.info("locales = {}", locales);

        // 2. attributeNames
        Enumeration<String> attributeNames = request.getAttributeNames();
        if (attributeNames.hasMoreElements()) {
            log.info("{}", attributeNames.nextElement());
        }
        log.info("attributeNames = {}", attributeNames);

        // 3. headers
        log.info("{}", request.getHeader("accept"));
        return "test";
    }

}
