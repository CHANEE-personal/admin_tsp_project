package com.tsp.new_tsp_admin.api.jwt;

import com.tsp.new_tsp_admin.api.user.service.repository.AdminUserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final AdminUserJpaRepository adminUserJpaRepository;

    private final JwtUtil jwtUtil;

    private final MyUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtUtil.resolveAccessToken(request);
        String refreshToken = jwtUtil.resolveRefreshToken(request);

        // 유효한 토큰인지 검사
        if (accessToken != null) {
            if (jwtUtil.isTokenExpired(accessToken)) {
                this.setAuthentication(accessToken);
            } else {
                if (refreshToken != null) {
                    boolean validateRefreshToken = jwtUtil.isTokenExpired(refreshToken);
                    boolean isRefreshToken = jwtUtil.isTokenExpired(refreshToken);

                    if(validateRefreshToken && isRefreshToken) {
                        String userId = adminUserJpaRepository.findOneUserByToken(accessToken);
                        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
                        String newAccessToken = jwtUtil.generateToken(userDetails);
                        jwtUtil.setHeaderAccessToken(response, newAccessToken);
                        this.setAuthentication(newAccessToken);
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    public void setAuthentication(String token) {
        Authentication authentication = jwtUtil.getAuthentication(token);
        // SecurityContext에 Authentication 객체 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
