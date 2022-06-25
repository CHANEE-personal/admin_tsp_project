package com.tsp.new_tsp_admin.api.portfolio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioEntity;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity;
import com.tsp.new_tsp_admin.api.domain.user.Role;
import com.tsp.new_tsp_admin.api.jwt.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity.builder;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
class AdminPortfolioJpaControllerTest {

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

    private AdminPortFolioEntity adminPortFolioEntity;
    private AdminUserEntity adminUserEntity;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return authorities;
    }

    public void createAdminPortfolio() {
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

        adminPortFolioEntity = AdminPortFolioEntity.builder()
                .categoryCd(1)
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .hashTag("#test")
                .videoUrl("https://youtube.com")
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

        createAdminPortfolio();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 포트폴리오 조회 테스트")
    public void 포트폴리오조회Api테스트() throws Exception {
        MultiValueMap<String, String> portfolioMap = new LinkedMultiValueMap<>();
        portfolioMap.put("jpaStartPage", Collections.singletonList("1"));
        portfolioMap.put("size", Collections.singletonList("3"));
        mockMvc.perform(get("/api/jpa-portfolio/lists").params(portfolioMap)
                .header("authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.portfolioList.length()", greaterThan(0)));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Admin 포트폴리오 조회 권한 테스트")
    public void 포트폴리오조회Api권한테스트() throws Exception {
        MultiValueMap<String, String> portfolioMap = new LinkedMultiValueMap<>();
        portfolioMap.put("jpaStartPage", Collections.singletonList("1"));
        portfolioMap.put("size", Collections.singletonList("3"));
        mockMvc.perform(get("/api/jpa-portfolio/lists").params(portfolioMap)
                .header("authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 포트폴리오 상세 조회 테스트")
    public void 포트폴리오상세조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/jpa-portfolio/1")
                .header("authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idx").value("1"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Admin 포트폴리오 상세 조회 권한 테스트")
    public void 포트폴리오상세조회Api권한테스트() throws Exception {
        mockMvc.perform(get("/api/jpa-portfolio/1")
                .header("authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 포트폴리오 등록 테스트")
    public void 포트폴리오등록Api테스트() throws Exception {
        mockMvc.perform(post("/api/jpa-portfolio")
                .header("authorization", "Bearer " + adminUserEntity.getUserToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(adminPortFolioEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryCd").value(1))
                .andExpect(jsonPath("$.title").value("포트폴리오 테스트"))
                .andExpect(jsonPath("$.description").value("포트폴리오 테스트"))
                .andExpect(jsonPath("$.hashTag").value("#test"))
                .andExpect(jsonPath("$.videoUrl").value("https://youtube.com"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Admin 포트폴리오 등록 권한 테스트")
    public void 포트폴리오등록Api권한테스트() throws Exception {
        mockMvc.perform(post("/api/jpa-portfolio")
                .header("authorization", "Bearer " + adminUserEntity.getUserToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(adminPortFolioEntity)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 포트폴리오 수정 테스트")
    public void 포트폴리오수정Api테스트() throws Exception {
        em.persist(adminPortFolioEntity);

        adminPortFolioEntity = AdminPortFolioEntity.builder()
                .idx(adminPortFolioEntity.getIdx())
                .categoryCd(1)
                .title("포트폴리오 테스트1111")
                .description("포트폴리오 테스트1111")
                .hashTag("#test111")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        mockMvc.perform(put("/api/jpa-portfolio/{idx}", adminPortFolioEntity.getIdx())
                .header("authorization", "Bearer " + adminUserEntity.getUserToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(adminPortFolioEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryCd").value(1))
                .andExpect(jsonPath("$.title").value("포트폴리오 테스트1111"))
                .andExpect(jsonPath("$.description").value("포트폴리오 테스트1111"))
                .andExpect(jsonPath("$.hashTag").value("#test111"))
                .andExpect(jsonPath("$.videoUrl").value("https://youtube.com"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Admin 포트폴리오 수정 권한 테스트")
    public void 포트폴리오수정Api권한테스트() throws Exception {
        em.persist(adminPortFolioEntity);

        adminPortFolioEntity = AdminPortFolioEntity.builder()
                .idx(adminPortFolioEntity.getIdx())
                .categoryCd(1)
                .title("포트폴리오 테스트1111")
                .description("포트폴리오 테스트1111")
                .hashTag("#test111")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();

        mockMvc.perform(put("/api/jpa-portfolio/{idx}", adminPortFolioEntity.getIdx())
                .header("authorization", "Bearer " + adminUserEntity.getUserToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(adminPortFolioEntity)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 포트폴리오 삭제 테스트")
    public void 포트폴리오삭제Api테스트() throws Exception {
        em.persist(adminPortFolioEntity);

        mockMvc.perform(delete("/api/jpa-portfolio/{idx}", adminPortFolioEntity.getIdx())
                .header("authorization", "Bearer " + adminUserEntity.getUserToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(adminPortFolioEntity)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Admin 포트폴리오 삭제 권한 테스트")
    public void 포트폴리오삭제Api권한테스트() throws Exception {
        em.persist(adminPortFolioEntity);

        mockMvc.perform(delete("/api/jpa-portfolio/{idx}", adminPortFolioEntity.getIdx())
                .header("authorization", "Bearer " + adminUserEntity.getUserToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(adminPortFolioEntity)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}