package com.ctp102.module.oauth.oauth.service;

import com.ctp102.module.oauth.oauth.NaverRestClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NaverOauthService {

    private final NaverRestClient naverRestClient;

}
