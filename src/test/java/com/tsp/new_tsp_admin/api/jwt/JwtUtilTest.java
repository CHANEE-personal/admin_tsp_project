package com.tsp.new_tsp_admin.api.jwt;

import com.tsp.new_tsp_admin.api.domain.user.AuthenticationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.event.EventListener;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = NONE)
class JwtUtilTest {
    private final MockHttpServletResponse response = new MockHttpServletResponse();
    @Value("${spring.jwt.secret}")
    private String SECRET_KEY;
    @Autowired private MyUserDetailsService userDetailsService;
    @Autowired private JwtUtil jwtUtil;

    private final AuthenticationRequest authenticationRequest = new AuthenticationRequest();
    private UserDetails userDetails;

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void setUp() {
        authenticationRequest.setUserId("admin02");
        authenticationRequest.setPassword("pass1234");
        userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUserId());
    }

    @Test
    @DisplayName("토큰 만료 테스트")
    void isTokenExpiredTest() {
        String token = jwtUtil.generateToken(userDetails);
        assertFalse(jwtUtil.isTokenExpired(token));
    }

    @Test
    @DisplayName("토큰 생성 테스트")
    void generateTokenTest() {
        assertNotNull(jwtUtil.generateToken(userDetails));
    }

    @Test
    @DisplayName("리프레시 토큰 생성 테스트")
    void generateRefreshTokenTest() {
        assertNotNull(jwtUtil.generateToken(userDetails));
    }

    @Test
    void extractAllClaimsTest() {
        String token = jwtUtil.generateToken(userDetails);
        assertNotNull(jwtUtil.extractAllClaims(token));
    }

    @Test
    @DisplayName("토큰 유효성 테스트")
    void validateTokenTest() {
        String token = jwtUtil.generateToken(userDetails);
        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    @DisplayName("엑세스 토큰 헤더 설정 테스트")
    void setHeaderAccessTokenTest() {
        String token = jwtUtil.generateToken(userDetails);
        jwtUtil.setHeaderAccessToken(response, token);
        assertNotNull(response.getHeader("Authorization"));
        assertThat("Bearer " + token).isEqualTo(response.getHeader("Authorization"));
    }

    @Test
    @DisplayName("엑세스 리프레시 토큰 헤더 설정 테스트")
    void setHeaderRefreshTokenTest() {
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);
        jwtUtil.setHeaderRefreshToken(response, refreshToken);
        assertNotNull(response.getHeader("refreshToken"));
        assertThat("Bearer " + refreshToken).isEqualTo(response.getHeader("refreshToken"));
    }
}