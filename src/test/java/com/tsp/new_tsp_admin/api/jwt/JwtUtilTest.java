package com.tsp.new_tsp_admin.api.jwt;

import com.tsp.new_tsp_admin.api.domain.user.AuthenticationRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JwtUtilTest {

    @Value("${spring.jwt.secret}")
    private String SECRET_KEY;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void token생성테스트() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUserId("admin01");
        authenticationRequest.setPassword("pass1234");
        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUserId());

        Assertions.assertNotNull(jwtUtil.generateToken(userDetails));
    }
}