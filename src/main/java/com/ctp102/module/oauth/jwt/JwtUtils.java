package com.ctp102.module.oauth.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

@Configuration
@Slf4j
public class JwtUtils {

    private static final String SECRET_KEY = "secret";
    private static final long ONE_HOUR_MILLIS = 1000L * 60 * 60;

    /**
     * Jwt 생성
     *
     * @param key the key
     * @return string
     */
    public String createJwt(String key) {
        Claims claims = Jwts.claims().setId(key); // jti

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] secretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(secretBytes, signatureAlgorithm.getJcaName());

        long nowMillis = System.currentTimeMillis();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(nowMillis))
                .setExpiration(new Date(nowMillis + ONE_HOUR_MILLIS))
                .signWith(signatureAlgorithm, signingKey)
                .compact();
    }

    /**
     * Gets claims.
     *
     * @param jwt the jwt
     * @return claims
     */
    public Jws<Claims> getClaims(String jwt) {
        try {
            // 암호화 키로 복호화한다.
            // 즉 암호화 키가 다르면 에러가 발생한다.
            return Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY)) // todo: 사이닝한 키가 달라도 복호화가 왜 되지???!
                    .parseClaimsJws(jwt);
        } catch (SignatureException e) {
            return null;
        }
    }


    /**
     * Validate token boolean.
     *
     * @param claims the claims
     * @return the boolean
     */
    public boolean validateToken(Jws<Claims> claims) {
        // 토큰 만료 시간이 현재 시간을 지났는지 검증
        return !claims.getBody()
                .getExpiration()
                .before(new Date());
    }

}
