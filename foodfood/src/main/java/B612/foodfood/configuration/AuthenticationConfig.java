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

    @Value("${jwt.token.secret}")
    private String secretKey;

    private final ObjectMapper objectMapper;

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/members/join",
                                "/api/v1/members/login",
                                "/login/oauth2/**","/").permitAll()  // 인증 불필요 설정
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .logout((logoutConfig) ->
                        logoutConfig.logoutSuccessUrl("/")
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtFilter(secretKey), UsernamePasswordAuthenticationFilter.class)  // 필터 추가
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(new JwtAuthenticationEntryPoint(objectMapper)))  // 커스텀 인증 예외 추가
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.accessDeniedHandler(new JwtAccessDeniedHandler(objectMapper)));  // 커스텀 인가 예외 추가


        return http.build();
    }

}
