package com.ctp102.module.oauth.jwt;

import com.ctp102.module.oauth.CommonTest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class JwtTest extends CommonTest {

    @Autowired
    private JwtUtils jwtUtils;

    @Test
    void createJwtTest() {
        String jwt = jwtUtils.createJwt(String.valueOf(100));
        printJson(jwt);
    }

    @Test
    void validateJwt() {
        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMDAiLCJpYXQiOjE2NzEwNDA4MzksImV4cCI6MTY3MTA0NDQzOX0.7oLKJui4TTtPBUHDPogQV_UX_JNy1nnkx9m-YEP-P8Q";
        // signature:

        Jws<Claims> claims = jwtUtils.getClaims(jwt);

        printJson(claims);
        printJson(claims.getBody().get("jti"));
        printJson(claims.getBody().getId());
    }


}
