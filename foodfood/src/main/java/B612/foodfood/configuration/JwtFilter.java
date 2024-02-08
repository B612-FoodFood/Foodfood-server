package B612.foodfood.configuration;

import B612.foodfood.domain.RefreshToken;
import B612.foodfood.exception.AppException;
import B612.foodfood.exception.ErrorCode;
import B612.foodfood.repository.RefreshTokenRepository;
import B612.foodfood.service.MemberService;
import B612.foodfood.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.*;

/**
 * HttpServletResponse는 헤더에 담겨서 반환된다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {  // 매번 토큰 인증을 위한 OncePerRequestFilter를 상속

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override  // 권한 부여 로직
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        final String authorization = request.getHeader(AUTHORIZATION); // header 에서 authorization 꺼내기
        String beforeExtractedRefreshToken = request.getHeader("RefreshToken");  // header 에서 Bearer refreshToken
        log.info("authorization: {}", authorization);
        log.info("refresh token: {}", beforeExtractedRefreshToken);

        String accessToken;
        String refreshToken;


        /**
         Token 을 보내지 않은 경우 Block 됨, Bearer 로 보내지 않았다면 Block
         */
        if (!jwtUtil.validateAuthorization(authorization) || !jwtUtil.validateAuthorization(beforeExtractedRefreshToken)) {
            log.error("Authorization is invalid");
            filterChain.doFilter(request, response);
            return;  // 종료
        } else { // authorization이 정상적이라면 access token 추출
            accessToken = jwtUtil.extractToken(authorization);
            refreshToken = jwtUtil.extractToken(beforeExtractedRefreshToken);
        }

        /**
         authorization 내부의 access token이 validate한 경우

         --> access token으로 접근 시도
         */
        if (jwtUtil.validateToken(accessToken)) {
            // Token에서 username 꺼내기
            String username = jwtUtil.decodeToken(accessToken).get("username", String.class);

            // 권한 부여
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, null, List.of(new SimpleGrantedAuthority("USER")));  /**나중에 Role 타입 admin, user를 케이스마다 제공하도록 수정해야함**/

            // Detail 넣어주기
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            // access token 재발급
            String newAccessToken = jwtUtil.createAccessToken(username);
            response.setHeader("Authorization", "Bearer " + newAccessToken);

            log.info("new access token: {}",newAccessToken);

            filterChain.doFilter(request, response);
        }

        /**
         access token 이 validate 하지 않은 경우
         1.잘못된 access token
         2.만료된 access token

         --> refresh token 으로 접근 시도
         */
        else if (jwtUtil.validateToken(refreshToken)) {
            // refresh token 검색. 없는 경우 에러 발생
            RefreshToken refreshTokenEntity = refreshTokenRepository.findByValue(refreshToken)
                    .orElseThrow(() -> new AppException(ErrorCode.NO_DATA_EXISTED, "로그인이 필요합니다."));

            String newRefreshToken = jwtUtil.createRefreshToken();
            String newAccessToken = jwtUtil.createAccessToken(refreshTokenEntity.getUsername());


            refreshTokenEntity.updateValue(newRefreshToken);
            refreshTokenRepository.save(refreshTokenEntity);  // refresh token의 value를 새로 업뎃 후 db에 저장

            // 권한 부여
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(refreshTokenEntity.getUsername(), null, List.of(new SimpleGrantedAuthority("USER"))); /**나중에 Role 타입 admin, user를 케이스마다 제공하도록 수정해야함**/

            // Detail 넣어주기
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            // 새로운 Authorization(Bearer 'access token')
            response.setHeader(AUTHORIZATION, "Bearer " + newAccessToken);
            // 새로운 refresh token
            response.setHeader("RefreshToken", "Bearer " + newRefreshToken);

            filterChain.doFilter(request, response);
        }


        // Token 꺼내기
        String token = jwtUtil.extractToken(authorization); // authorization == "Bearer Token"

        // Token Expired 되었는지 Check
        if (!jwtUtil.validateToken(token)) {
            filterChain.doFilter(request, response);
            return;  // 종료
        }


    }
}
