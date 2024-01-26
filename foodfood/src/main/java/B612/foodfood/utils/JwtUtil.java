package B612.foodfood.utils;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class JwtUtil {
    /**
     * token 추출 메서드
     */
    public static String extractToken(String authorization) {
        String token = authorization.split(" ")[1];
        return token;
    }
    /**
     * 토큰의 유효 및 만료 확인 메서드
     */
    public static boolean validateToken(String token,String secretKey) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (SignatureException| SecurityException | MalformedJwtException e) {
            log.error(e.getClass().getName());
            log.error("Invalid JWT signature: "+ e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            log.error(e.getClass().getName());
            log.error("Unsupported JWT token: "+e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            log.error(e.getClass().getName());
            log.error("JWT token is invalid: "+ e.getMessage());
            return false;
        } catch (ExpiredJwtException e) {
            log.error(e.getClass().getName());
            log.error("token is expired: "+e.getMessage());
            return false;
        }
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
