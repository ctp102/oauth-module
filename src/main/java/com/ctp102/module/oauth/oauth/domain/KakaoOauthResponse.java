package com.ctp102.module.oauth.oauth.domain;

import lombok.Data;

@Data
public class KakaoOauthResponse<T> {

    private T data;

}
