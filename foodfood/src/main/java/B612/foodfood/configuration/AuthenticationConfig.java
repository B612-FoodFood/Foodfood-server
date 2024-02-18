package B612.foodfood.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity  // Spring Security 활성화
@RequiredArgsConstructor
public class AuthenticationConfig {


    private final ObjectMapper objectMapper;
    private final JwtFilter jwtFilter;

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable); // CSRF 공격을 방지하기 위해 서버에서 발생한 요청인지 확인하는 기능을 비활성화.
        http.authorizeHttpRequests(authorize ->
                        authorize.requestMatchers(
                                        "/api/v1/members/join/**",
                                        "/api/v1/members/login",
                                        "/login/oauth2/**",
                                        "/")
                                .permitAll()  // 인증 불필요 설정
                                .requestMatchers("/admin/**").hasRole("ADMIN") // ADMIN 권한이 있는 사용자만 접근 가능
                                .anyRequest().authenticated())  // 나머지 모든 요청은 인증 필요
                .logout((logoutConfig) ->
                        logoutConfig.logoutSuccessUrl("/")) // 로그아웃시 redirect될 페이지
                .sessionManagement(sessionManagement ->

                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // jwt를 사용할 것이기 때문에 session을 상태없이 유지하도록 설정함
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)  // 필터 추가

                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(new JwtAuthenticationEntryPoint(objectMapper)))  // 커스텀 인증 예외 추가 (인증되지 않은 예외에 응답 처리)
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.accessDeniedHandler(new JwtAccessDeniedHandler(objectMapper)));  // 커스텀 인가 예외 추가 (권한 없는 요청에 응답 처리)


        return http.build();
    }

}
