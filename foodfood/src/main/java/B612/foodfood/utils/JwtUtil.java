package B612.foodfood.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {
    /**
     * 토큰 만료 여부 체크 메서드
     */
    public static boolean isExpired(String token, String secretKey) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)  // seceret key로 디코딩한 token
                .getBody().getExpiration() //token의 만료일자가
                .before(new Date());  // 현재 이전인가?
    }

    /**
     * 토큰 발행 메서드
     */
    public static String createToken(String username, String key, long expireTimeMs) {
        Claims claims = Jwts.claims();  // 일종의 map
        claims.put("username", username); // token에다가 username을 넣어줌

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs))
                .signWith(SignatureAlgorithm.HS256, key)  // HS256으로 암호화
                .compact();
    }


    /**
     * 토큰을 디코드하여 페이로드 추출 메서드
     */
    public static Claims decodeToken(String token, String key) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
    }
}
