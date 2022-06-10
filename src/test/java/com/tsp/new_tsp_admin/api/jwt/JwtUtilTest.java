package com.tsp.new_tsp_admin.api.jwt;

import com.tsp.new_tsp_admin.api.domain.user.AuthenticationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestPropertySource;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JwtUtilTest {

    private final MockHttpServletRequest request = new MockHttpServletRequest();
    private final MockHttpServletResponse response = new MockHttpServletResponse();

    @Value("${spring.jwt.secret}")
    private String SECRET_KEY;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    private final AuthenticationRequest authenticationRequest = new AuthenticationRequest();
    private UserDetails userDetails;

    @BeforeEach
    public void setUp() {
        authenticationRequest.setUserId("admin02");
        authenticationRequest.setPassword("pass1234");
        userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUserId());
    }

    @Test
    @DisplayName("토큰 만료 테스트")
    public void isTokenExpiredTest() {
        String token = jwtUtil.generateToken(userDetails);
        assertFalse(jwtUtil.isTokenExpired(token));
    }

    @Test
    @DisplayName("토큰 생성 테스트")
    public void generateTokenTest() {
        assertNotNull(jwtUtil.generateToken(userDetails));
    }

    @Test
    @DisplayName("리프레시 토큰 생성 테스트")
    public void generateRefreshTokenTest() {
        assertNotNull(jwtUtil.generateToken(userDetails));
    }

    @Test
    public void extractAllClaimsTest() {
        String token = jwtUtil.generateToken(userDetails);
        assertNotNull(jwtUtil.extractAllClaims(token));
    }

    @Test
    @DisplayName("토큰 유효성 테스트")
    public void validateTokenTest() {
        String token = jwtUtil.generateToken(userDetails);
        assertTrue(jwtUtil.validateToken(token, userDetails));
    }

    @Test
    @DisplayName("엑세스 토큰 헤더 설정 테스트")
    public void setHeaderAccessTokenTest() throws Exception {
        String token = jwtUtil.generateToken(userDetails);
        jwtUtil.setHeaderAccessToken(response, token);
        assertNotNull(response.getHeader("authorization"));
        assertThat("bearer " + token).isEqualTo(response.getHeader("authorization"));
    }
}