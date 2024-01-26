package B612.foodfood.configuration;

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
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {  // 매번 토큰 인증을 위한 OncePerRequestFilter를 상속

    private final String secretKey;

    @Override  // 권한 부여 로직
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // header에서 authorization 꺼내기
        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("authorization: {}",authorization);

        // Token을 보내지 않은 경우 Block됨, Bearer로 보내지 않았다면 Block
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            log.error("Authorization is invalid");
            filterChain.doFilter(request, response);
            return;  // 종료
        }

        // Token 꺼내기
        String token = JwtUtil.extractToken(authorization); // authorization == "Bearer Token"

        // Token Expired 되었는지 Check
        if (!JwtUtil.validateToken(token, secretKey)) {
            filterChain.doFilter(request, response);
            return;  // 종료
        }

        // Token에서 username 꺼내기
        String username = JwtUtil.decodeToken(token, secretKey).get("username", String.class);

        // 권한 부여
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, null, List.of(new SimpleGrantedAuthority("USER")));

        // Detail 넣어주기
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
