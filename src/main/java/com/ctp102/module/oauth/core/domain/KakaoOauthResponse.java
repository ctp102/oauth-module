package com.ctp102.module.oauth.core.domain;

import lombok.Data;

@Data
public class KakaoOauthResponse<T> {

    private T data;

}
