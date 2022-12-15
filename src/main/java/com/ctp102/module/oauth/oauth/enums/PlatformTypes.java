package com.ctp102.module.oauth.oauth.enums;

import lombok.Getter;

public enum PlatformTypes {

    KAKAO("kakao"),
    NAVER("naver"),
    GOOGLE("google")
    ;

    @Getter
    private final String name;

    PlatformTypes(String name) {
        this.name = name;
    }

}
