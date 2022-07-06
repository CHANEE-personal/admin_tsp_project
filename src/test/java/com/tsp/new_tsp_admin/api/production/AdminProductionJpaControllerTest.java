package com.tsp.new_tsp_admin.api.production;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsp.new_tsp_admin.api.domain.production.AdminProductionEntity;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity;
import com.tsp.new_tsp_admin.api.domain.user.Role;
import com.tsp.new_tsp_admin.api.jwt.JwtUtil;
import com.tsp.new_tsp_admin.common.StringUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.tsp.new_tsp_admin.api.domain.production.AdminProductionEntity.builder;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AdminProductionJpaControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private EntityManager em;
    @Autowired
    private JwtUtil jwtUtil;

    private AdminProductionEntity adminProductionEntity;
    private AdminUserEntity adminUserEntity;

    Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return authorities;
    }

    void createAdminProduction() {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("admin04", "pass1234", getAuthorities());

        adminUserEntity = AdminUserEntity.builder()
                .userId("admin04")
                .password("pass1234")
                .name("test")
                .email("test@test.com")
                .role(Role.ROLE_ADMIN)
                .userToken(jwtUtil.doGenerateToken(authenticationToken.getName(), 1000L * 10))
                .visible("Y")
                .build();

        em.persist(adminUserEntity);

        adminProductionEntity = builder()
                .title("프로덕션 테스트")
                .description("프로덕션 테스트")
                .visible("Y")
                .build();
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(springSecurity())
                .alwaysDo(print())
                .build();

        createAdminProduction();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 프로덕션 조회 테스트")
    void 프로덕션조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/jpa-production/lists")
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productionList.length()", greaterThan(0)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 프로덕션 검색 조회 테스트")
    void 프로덕션검색조회Api테스트() throws Exception {
        // 검색 테스트
        LinkedMultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("jpaStartPage", "1");
        paramMap.add("size", "3");
        paramMap.add("searchType", "0");
        paramMap.add("searchKeyword", "하하");

        mockMvc.perform(get("/api/jpa-production/lists").queryParams(paramMap)
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Admin 프로덕션 조회 권한 테스트")
    void 프로덕션조회Api권한테스트() throws Exception {
        mockMvc.perform(get("/api/jpa-production/lists")
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 프로덕션 상세 조회 테스트")
    void 프로덕션상세조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/jpa-production/1")
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idx").value("1"))
                .andExpect(jsonPath("$.title").value("테스트1"))
                .andExpect(jsonPath("$.description").value("테스트1"));;
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Admin 프로덕션 상세 조회 권한 테스트")
    void 프로덕션상세조회Api권한테스트() throws Exception {
        mockMvc.perform(get("/api/jpa-production/1")
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 프로덕션 등록 테스트")
    void 프로덕션등록Api테스트() throws Exception {
        mockMvc.perform(post("/api/jpa-production")
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(adminProductionEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("프로덕션 테스트"))
                .andExpect(jsonPath("$.description").value("프로덕션 테스트"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Admin 프로덕션 등록 권한 테스트")
    void 프로덕션등록Api권한테스트() throws Exception {
        mockMvc.perform(post("/api/jpa-production")
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(adminProductionEntity)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 프로덕션 수정 테스트")
    void 프로덕션수정Api테스트() throws Exception {
        em.persist(adminProductionEntity);

        adminProductionEntity = builder().idx(adminProductionEntity.getIdx()).title("테스트1").description("테스트1").build();

        mockMvc.perform(put("/api/jpa-production/{idx}", adminProductionEntity.getIdx())
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(adminProductionEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("테스트1"))
                .andExpect(jsonPath("$.description").value("테스트1"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Admin 프로덕션 수정 권한 테스트")
    void 프로덕션수정Api권한테스트() throws Exception {
        em.persist(adminProductionEntity);

        adminProductionEntity = builder().idx(adminProductionEntity.getIdx()).title("테스트1").description("테스트1").build();

        mockMvc.perform(put("/api/jpa-production/{idx}", adminProductionEntity.getIdx())
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(adminProductionEntity)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 프로덕션 삭제 테스트")
    void 프로덕션삭제Api테스트() throws Exception {
        em.persist(adminProductionEntity);

        mockMvc.perform(delete("/api/jpa-production/{idx}", adminProductionEntity.getIdx())
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(StringUtil.getString(adminProductionEntity.getIdx())));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Admin 프로덕션 삭제 권한 테스트")
    void 프로덕션삭제Api권한테스트() throws Exception {
        em.persist(adminProductionEntity);

        mockMvc.perform(delete("/api/jpa-production/{idx}", adminProductionEntity.getIdx())
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}