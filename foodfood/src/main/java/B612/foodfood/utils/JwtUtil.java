package B612.foodfood.utils;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${jwt.token.secret}")
    private String secretKey;

    @Value("${jwt.token.expireAccessTokenTimeMs}")
    private long expireTimeMs;  // 엑세스 토큰 만료 시간: 1시간

    @Value("${jwt.token.expireRefreshTokenTimeMs}")
    private long refreshTokenExpireTimeMs;  // 리프레시 토큰 만료 시간: 7일

    /**
     * token 추출 메서드
     *
     * @param authorization string
     */
    public String extractToken(String authorization) {
        String token = authorization.split(" ")[1];
        return token;
    }

    public boolean validateAuthorization(String authorization) {
        return authorization != null && authorization.startsWith("Bearer ");
    }

    /**
     * 토큰의 유효 및 만료 확인 메서드
     *
     * @param token string
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (SignatureException | SecurityException | MalformedJwtException e) {
            log.error(e.getClass().getName());
            log.error("Invalid JWT signature: " + e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            log.error(e.getClass().getName());
            log.error("Unsupported JWT token: " + e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            log.error(e.getClass().getName());
            log.error("JWT token is invalid: " + e.getMessage());
            return false;
        } catch (ExpiredJwtException e) {
            log.error(e.getClass().getName());
            log.error("token is expired: " + e.getMessage());
            return false;
        }
    }

    /**
     * access token 발행 메서드
     *
     * @param username string
     * @return
     */
    public String createAccessToken(String username) {
        Claims claims = Jwts.claims();  // 일종의 map, 페이로드에 들어갈 정보들을 담는 역할

        // 정보 담기
        claims.setSubject("access_token")  // token 제목 설정
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs))
                .put("username", username); // token에다가 username을 넣어줌

        return Jwts.builder()
                .setClaims(claims)  // token에 정보를 담아줌
                .signWith(SignatureAlgorithm.HS256, secretKey)  // HS256으로 암호화
                .compact();
    }

    /**
     * refresh token 생성 메서드
     */
    public String createRefreshToken() {
        return Jwts.builder()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpireTimeMs))
                .signWith(SignatureAlgorithm.HS256, secretKey)  // HS256으로 암호화
                .compact();
    }


    /**
     * 토큰을 디코드하여 페이로드 추출 메서드
     *
     * @param token String
     */
    public Claims decodeToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }
}
