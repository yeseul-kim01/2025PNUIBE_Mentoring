package pnu.ibe.justice.mentoring.config.auth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import pnu.ibe.justice.mentoring.model.Role;


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrfConfig -> csrfConfig.disable())
                .headers(headerConfig -> headerConfig.frameOptions(frameOptionsConfig -> frameOptionsConfig.disable())
                        .cacheControl(cacheControl -> cacheControl.disable()) // 캐시 비활성화
                )
                .authorizeHttpRequests(authorizeRequest -> authorizeRequest
                        .requestMatchers("/admin/**").authenticated() // /admin 접근 시 인증 필요
                        .requestMatchers("/", "/error", "/login/*", "/notice/**", "/lectureList/**", "/introduce/**",
                                "/peopleList/**", "/logout/*", "/favicon.ico", "/lib/**", "/css/**", "/js/**",
                                "/images/**", "/scss/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/custom-login")
                        .loginProcessingUrl("/login") // 로그인 처리 경로 (HTML action과 일치)// 커스텀 로그인 페이지 경로
                        .defaultSuccessUrl("/admin") // 로그인 성공 후 이동 경로
                        .failureUrl("/login?error=true") // 로그인 실패 시 이동 경로
                        .permitAll()
                )
                .logout(logoutConfig -> logoutConfig
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true) // 세션 무효화
                        .deleteCookies("JSESSIONID") // 쿠키 삭제
                        .permitAll()
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/login") // OAuth2 로그인도 동일한 /login 경로 사용
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(customOAuth2UserService))
                        .successHandler((request, response, authentication) -> {
                            response.sendRedirect("/");
                        })
                        .failureHandler((request, response, exception) -> {
                            System.out.println(exception.toString());
                            System.out.println(exception.getMessage());
                            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            response.sendRedirect("/");
                        })
                );

        return http.build();
    }


}