package com.tsp.new_tsp_admin.api.user;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity;
import com.tsp.new_tsp_admin.api.domain.user.AuthenticationRequest;
import com.tsp.new_tsp_admin.api.domain.user.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

    public void createUser() {
        adminUserEntity = builder()
                .userId("admin02")
                .password("pass1234")
                .name("admin02")
                .email("admin02@tsp.com")
                .role(Role.ROLE_ADMIN)
                .visible("Y")
                .build();
    }

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(springSecurity())
                .alwaysDo(print())
                .build();

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        createUser();
    }

    @Test
    @DisplayName("Admin 회원 조회 테스트")
    public void Admin회원조회() throws Exception {
        mockMvc.perform(get("/api/jpa-user").param("page", "1").param("size", "100"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("로그인 테스트")
    public void 로그인테스트() throws Exception {
        mockMvc.perform(post("/api/jpa-user/login")
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

        adminUserEntity = builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .build();

        mockMvc.perform(post("/api/jpa-user")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(adminUserEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("test"))
                .andExpect(jsonPath("$.password").value("test"))
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.email").value("test@test.com"))
                .andExpect(jsonPath("$.role").value("ROLE_ADMIN"));
    }

    @Test
    @DisplayName("관리자 회원수정 테스트")
    public void 회원수정테스트() throws Exception {
        em.persist(adminUserEntity);

        adminUserEntity = builder()
                .idx(adminUserEntity.getIdx())
                .userId("admin03")
                .password("pass1234")
                .name("admin03")
                .email("admin03@tsp.com")
                .visible("Y")
                .build();

        mockMvc.perform(put("/api/jpa-user/{idx}", adminUserEntity.getIdx())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(adminUserEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("admin03"))
                .andExpect(jsonPath("$.name").value("admin03"));
    }

    @Test
    @DisplayName("관리자 회원탈퇴 테스트")
    public void 회원탈퇴테스트() throws Exception {
        AdminUserEntity adminUserEntity = builder().idx(1).build();

        mockMvc.perform(put("/api/jpa-user")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(adminUserEntity)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("JWT 토큰 발급 테스트")
    public void 토큰발급테스트() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUserId("admin01");
        authenticationRequest.setPassword("pass1234");

        mockMvc.perform(post("/api/jpa-user/refresh")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").isNotEmpty())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }
}