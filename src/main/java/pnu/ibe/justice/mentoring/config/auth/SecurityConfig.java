package pnu.ibe.justice.mentoring.config.auth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import pnu.ibe.justice.mentoring.config.auth.CustomOAuth2UserService;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 공통 설정
        http.csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.disable())
                        .cacheControl(cache -> cache.disable()));

        // /admin/** 경로에 대한 Form Login 설정
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/admin/**").authenticated() // /admin/** 인증 필요
                )
                .formLogin(form -> form
                        .loginPage("/custom-login") // 사용자 지정 로그인 페이지
                        .loginProcessingUrl("/login") // 로그인 처리 URL
                        .defaultSuccessUrl("/admin") // 로그인 성공 후 이동
                        .failureUrl("/custom-login?error=true") // 로그인 실패 시 이동
                        .permitAll()
                );

        // 그 외 경로에 대한 OAuth2 Login 설정
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/error", "/notice/**", "/lectureList/**", "/introduce/**",
                                "/peopleList/**", "/logout/*", "/favicon.ico", "/lib/**", "/css/**", "/js/**",
                                "/images/**", "/scss/**").permitAll() // 공개 경로
                        .anyRequest().authenticated() // 그 외 모든 요청 인증 필요
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/login") // OAuth2 로그인 페이지
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler((request, response, authentication) -> {
                            response.sendRedirect("/"); // 로그인 성공 후 이동
                        })
                        .failureHandler((request, response, exception) -> {
                            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            response.sendRedirect("/login?error=true"); // 로그인 실패 시 이동
                        })
                );

        // 로그아웃 설정
        http.logout(logout -> logout
                .logoutSuccessUrl("/") // 로그아웃 성공 후 이동
                .permitAll()
        );

        return http.build();
    }
}
