package com.tsp.new_tsp_admin.api.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity;
import com.tsp.new_tsp_admin.api.domain.user.AuthenticationRequest;
import com.tsp.new_tsp_admin.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.event.EventListener;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity.*;
import static com.tsp.new_tsp_admin.api.domain.user.Role.ROLE_ADMIN;
import static com.tsp.new_tsp_admin.common.StringUtil.getString;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs // -> apply(documentationConfiguration(restDocumentation))
@ExtendWith(RestDocumentationExtension.class)
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
class AdminUserJpaControllerTest {
    private final ObjectMapper objectMapper;
    private final WebApplicationContext wac;
    private final EntityManager em;
    private final JwtUtil jwtUtil;

    private AdminUserEntity adminUserEntity;
    private MockMvc mockMvc;
    protected PasswordEncoder passwordEncoder;

    Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return authorities;
    }

    void createUser() {
        passwordEncoder = createDelegatingPasswordEncoder();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("admin04", "pass1234", getAuthorities());
        String token = jwtUtil.doGenerateToken(authenticationToken.getName(), 1000L * 10);

        adminUserEntity = builder()
                .userId("admin04")
                .password("pass1234")
                .name("test")
                .email("test@test.com")
                .role(ROLE_ADMIN)
                .userToken(token)
                .visible("Y")
                .build();

        em.persist(adminUserEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void setup(RestDocumentationContextProvider restDocumentationContextProvider) {
        this.mockMvc = webAppContextSetup(wac)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .alwaysDo(print())
                .build();

        createUser();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 회원 조회 테스트")
    void Admin회원조회() throws Exception {
        mockMvc.perform(get("/api/jpa-user").param("page", "1").param("size", "100")
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Admin 회원 조회 권한 테스트")
    void Admin회원조회권한테스트() throws Exception {
        mockMvc.perform(get("/api/jpa-user").param("page", "1").param("size", "100")
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("로그인 테스트")
    void 로그인테스트() throws Exception {
        mockMvc.perform(post("/api/jpa-user/login")
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(adminUserEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(header().string("loginYn", "Y"))
                .andExpect(header().string("username", "test"))
                .andExpect(header().exists("authorization"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("관리자 회원가입 테스트")
    void 회원가입테스트() throws Exception {
        AdminUserEntity newAdminUserEntity = builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .role(ROLE_ADMIN)
                .visible("Y")
                .build();

        mockMvc.perform(post("/api/jpa-user")
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(newAdminUserEntity)))
                .andDo(print())
                .andDo(document("user/post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("userId").type(STRING).description("아이디"),
                                fieldWithPath("password").type(STRING).description("패스워드"),
                                fieldWithPath("name").type(STRING).description("이름"),
                                fieldWithPath("email").type(STRING).description("이메일")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("userId").type(STRING).description("아이디"),
                                fieldWithPath("password").type(STRING).description("패스워드")
                        )))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.userId").value("test"))
                .andExpect(jsonPath("$.password").value("test"))
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.email").value("test@test.com"))
                .andExpect(jsonPath("$.role").value("ROLE_ADMIN"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("관리자 회원가입 권한 예외 테스트")
    void 회원가입권한테스트() throws Exception {
        adminUserEntity = builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .build();

        mockMvc.perform(post("/api/jpa-user")
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(adminUserEntity)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("관리자 회원수정 테스트")
    void 회원수정테스트() throws Exception {
        AdminUserEntity updateAdminUserEntity = builder()
                .idx(adminUserEntity.getIdx())
                .userId("admin03")
                .password("pass1234")
                .name("admin03")
                .email("admin03@tsp.com")
                .visible("Y")
                .build();

        mockMvc.perform(put("/api/jpa-user/{idx}", updateAdminUserEntity.getIdx())
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(updateAdminUserEntity)))
                .andDo(print())
                .andDo(document("user/put",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("userId").type(STRING).description("아이디"),
                                fieldWithPath("password").type(STRING).description("패스워드"),
                                fieldWithPath("name").type(STRING).description("이름"),
                                fieldWithPath("email").type(STRING).description("이메일")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("userId").type(STRING).description("아이디"),
                                fieldWithPath("password").type(STRING).description("패스워드")
                        )))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.userId").value("admin03"))
                .andExpect(jsonPath("$.name").value("admin03"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("관리자 회원수정 권한 예외 테스트")
    void 회원수정권한테스트() throws Exception {
        AdminUserEntity updateAdminUserEntity = builder()
                .idx(adminUserEntity.getIdx())
                .userId("admin03")
                .password("pass1234")
                .name("admin03")
                .email("admin03@tsp.com")
                .visible("Y")
                .build();

        mockMvc.perform(put("/api/jpa-user/{idx}", updateAdminUserEntity.getIdx())
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(adminUserEntity)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("관리자 회원탈퇴 테스트")
    void 회원탈퇴테스트() throws Exception {
        mockMvc.perform(delete("/api/jpa-user/{idx}", adminUserEntity.getIdx())
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(content().string(getString(adminUserEntity.getIdx())));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("관리자 회원탈퇴 권한 테스트")
    void 회원탈퇴권한테스트() throws Exception {
        mockMvc.perform(put("/api/jpa-user")
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("JWT 토큰 발급 테스트")
    void 토큰발급테스트() throws Exception {
        mockMvc.perform(post("/api/jpa-user/refresh")
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(AuthenticationRequest.builder().userId("admin01").password("pass1234").build())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=utf-8"))
                .andExpect(jsonPath("$.jwt").isNotEmpty())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }
}