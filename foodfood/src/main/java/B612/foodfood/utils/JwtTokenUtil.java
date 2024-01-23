package B612.foodfood.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtTokenUtil {
    /**
     * 토큰 발행 메서드
     */
    public static String createToken(String login_id, String key, long expireTimeMs) {
        Claims claims = Jwts.claims();  // 일종의 map
        claims.put("login_id", login_id); // token에다가 login_id를 넣어줌

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs))
                .signWith(SignatureAlgorithm.HS256, key)  // HS256으로 암호화
                .compact();
    }
}
