package pnu.ibe.justice.mentoring.config.auth;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class CustomAuthenticationFilter extends OncePerRequestFilter {

    private final AntPathRequestMatcher adminPathMatcher = new AntPathRequestMatcher("/admin/**");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestUri = request.getRequestURI();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // /admin/** 경로는 formLogin 인증 처리
        if (adminPathMatcher.matches(request)) {
            if (authentication == null || !authentication.isAuthenticated()) {
                response.sendRedirect("/custom-login"); // formLogin 경로로 리다이렉트
                return;
            }
        } else {
            // 나머지 경로는 OAuth2 인증 처리
            if (authentication == null || !authentication.isAuthenticated()) {
                response.sendRedirect("/login"); // OAuth2 로그인 경로로 리다이렉트
                return;
            }
        }

        // 인증이 통과되면 다음 필터로 이동
        filterChain.doFilter(request, response);
    }
}
