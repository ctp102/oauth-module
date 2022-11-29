package com.ctp102.module.oauth.core.domain;

import lombok.Data;

@Data
public class KakaoOauthTokenResponse<T> {

    private T data;

}
