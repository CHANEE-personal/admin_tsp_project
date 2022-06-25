package com.tsp.new_tsp_admin.api.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity;
import com.tsp.new_tsp_admin.api.domain.user.AuthenticationRequest;
import com.tsp.new_tsp_admin.api.domain.user.Role;
import com.tsp.new_tsp_admin.api.jwt.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AdminUserJpaControllerTest {

    private AdminUserEntity adminUserEntity;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private EntityManager em;

    @Autowired
    private JwtUtil jwtUtil;


    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return authorities;
    }

    public void createUser() {
        passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("admin04", "pass1234", getAuthorities());
        String token = jwtUtil.doGenerateToken(authenticationToken.getName(), 1000L * 10);

        adminUserEntity = builder()
                .userId("admin04")
                .password("pass1234")
                .name("test")
                .email("test@test.com")
                .role(Role.ROLE_ADMIN)
                .userToken(token)
                .visible("Y")
                .build();

        em.persist(adminUserEntity);
    }

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(springSecurity())
                .alwaysDo(print())
                .build();

        createUser();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 회원 조회 테스트")
    public void Admin회원조회() throws Exception {
        mockMvc.perform(get("/api/jpa-user").param("page", "1").param("size", "100")
                .header("authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Admin 회원 조회 권한 테스트")
    public void Admin회원조회권한테스트() throws Exception {
        mockMvc.perform(get("/api/jpa-user").param("page", "1").param("size", "100")
                .header("authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("로그인 테스트")
    public void 로그인테스트() throws Exception {
        mockMvc.perform(post("/api/jpa-user/login")
                .header("authorization", "Bearer " + adminUserEntity.getUserToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(adminUserEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("loginYn", "Y"))
                .andExpect(header().string("username", "test"))
                .andExpect(header().exists("authorization"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("관리자 회원가입 테스트")
    public void 회원가입테스트() throws Exception {
        AdminUserEntity newAdminUserEntity = builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .role(Role.ROLE_ADMIN)
                .visible("Y")
                .build();

        mockMvc.perform(post("/api/jpa-user")
                .header("authorization", "Bearer " + adminUserEntity.getUserToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(newAdminUserEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("test"))
                .andExpect(jsonPath("$.password").value("test"))
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.email").value("test@test.com"))
                .andExpect(jsonPath("$.role").value("ROLE_ADMIN"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("관리자 회원가입 권한 예외 테스트")
    public void 회원가입권한테스트() throws Exception {
        adminUserEntity = builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .build();

        mockMvc.perform(post("/api/jpa-user")
                .header("authorization", "Bearer " + adminUserEntity.getUserToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(adminUserEntity)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("관리자 회원수정 테스트")
    public void 회원수정테스트() throws Exception {
        AdminUserEntity updateAdminUserEntity = builder()
                .idx(adminUserEntity.getIdx())
                .userId("admin03")
                .password("pass1234")
                .name("admin03")
                .email("admin03@tsp.com")
                .visible("Y")
                .build();

        mockMvc.perform(put("/api/jpa-user/{idx}", updateAdminUserEntity.getIdx())
                .header("authorization", "Bearer " + adminUserEntity.getUserToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(updateAdminUserEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("admin03"))
                .andExpect(jsonPath("$.name").value("admin03"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("관리자 회원수정 권한 예외 테스트")
    public void 회원수정권한테스트() throws Exception {
        AdminUserEntity updateAdminUserEntity = builder()
                .idx(adminUserEntity.getIdx())
                .userId("admin03")
                .password("pass1234")
                .name("admin03")
                .email("admin03@tsp.com")
                .visible("Y")
                .build();

        mockMvc.perform(put("/api/jpa-user/{idx}", updateAdminUserEntity.getIdx())
                .header("authorization", "Bearer " + adminUserEntity.getUserToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(adminUserEntity)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("관리자 회원탈퇴 테스트")
    public void 회원탈퇴테스트() throws Exception {
        AdminUserEntity deleteAdminUserEntity = builder().idx(adminUserEntity.getIdx()).build();

        mockMvc.perform(put("/api/jpa-user")
                .header("authorization", "Bearer " + adminUserEntity.getUserToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(deleteAdminUserEntity)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("관리자 회원탈퇴 권한 테스트")
    public void 회원탈퇴권한테스트() throws Exception {
        AdminUserEntity deleteAdminUserEntity = builder().idx(adminUserEntity.getIdx()).build();

        mockMvc.perform(put("/api/jpa-user")
                .header("authorization", "Bearer " + adminUserEntity.getUserToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(deleteAdminUserEntity)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("JWT 토큰 발급 테스트")
    public void 토큰발급테스트() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUserId("admin01");
        authenticationRequest.setPassword("pass1234");

        mockMvc.perform(post("/api/jpa-user/refresh")
                .header("authorization", "Bearer " + adminUserEntity.getUserToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").isNotEmpty())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }
}