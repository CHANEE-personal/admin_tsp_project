package com.tsp.new_tsp_admin.api.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

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
//                        String newAccessToken = jwtUtil.generateToken();
                    }
                }
            }
        }
    }

    public void setAuthentication(String token) {
        Authentication authentication = jwtUtil.getAuthentication(token);
        // SecurityContext에 Authentication 객체 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
