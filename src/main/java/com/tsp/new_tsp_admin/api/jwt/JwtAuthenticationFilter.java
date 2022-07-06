package com.tsp.new_tsp_admin.api.jwt;

import com.tsp.new_tsp_admin.api.user.service.repository.AdminUserJpaRepository;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.lang.Boolean.TRUE;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private AdminUserJpaRepository adminUserJpaRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String accessToken = jwtUtil.resolveAccessToken(request);
            String refreshToken = jwtUtil.resolveRefreshToken(request);

            // 유효한 토큰인지 검사
            if (accessToken != null) {
                String userId = adminUserJpaRepository.findOneUserByToken(accessToken);
                UserDetails userDetails = userDetailsService.loadUserByUsername(userId);

                if (TRUE.equals(jwtUtil.validateToken(accessToken))) {
                    this.setAuthentication(accessToken);
                } else {
                    if (refreshToken != null) {
                        boolean validateRefreshToken = jwtUtil.validateToken(refreshToken);

                        if (validateRefreshToken) {
                            String newAccessToken = jwtUtil.generateToken(userDetails);
                            jwtUtil.setHeaderAccessToken(response, newAccessToken);
                            this.setAuthentication(newAccessToken);
                        }
                    }
                }
            }
        } catch (ExpiredJwtException e) {
            log.info("Security exception for user {} - {}", e.getClaims().getSubject(), e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            log.debug("Exception " + e.getMessage(), e);
        }
        filterChain.doFilter(request, response);
    }

    public void setAuthentication(String token) {
        Authentication authentication = jwtUtil.getAuthentication(token);
        // SecurityContext에 Authentication 객체 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
