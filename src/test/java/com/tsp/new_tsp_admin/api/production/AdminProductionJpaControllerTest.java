package com.tsp.new_tsp_admin.api.production;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsp.new_tsp_admin.api.domain.production.AdminProductionEntity;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity;
import com.tsp.new_tsp_admin.api.jwt.JwtUtil;
import com.tsp.new_tsp_admin.common.StringUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.tsp.new_tsp_admin.api.domain.production.AdminProductionEntity.builder;
import static com.tsp.new_tsp_admin.api.domain.user.Role.ROLE_ADMIN;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@AutoConfigureRestDocs // -> apply(documentationConfiguration(restDocumentation))
@ExtendWith({RestDocumentationExtension.class})
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = NONE)
class AdminProductionJpaControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private WebApplicationContext wac;
    @Autowired private EntityManager em;
    @Autowired private JwtUtil jwtUtil;

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
                .role(ROLE_ADMIN)
                .userToken(jwtUtil.doGenerateToken(authenticationToken.getName(), 1000L * 10))
                .visible("Y")
                .build();

        em.persist(adminUserEntity);

        adminProductionEntity = builder()
                .title("???????????? ?????????")
                .description("???????????? ?????????")
                .visible("Y")
                .build();
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

        createAdminProduction();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin ???????????? ?????? ?????????")
    void ??????????????????Api?????????() throws Exception {
        mockMvc.perform(get("/api/jpa-production/lists")
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productionList.length()", greaterThan(0)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin ???????????? ?????? ?????? ?????????")
    void ????????????????????????Api?????????() throws Exception {
        // ?????? ?????????
        LinkedMultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("jpaStartPage", "1");
        paramMap.add("size", "3");
        paramMap.add("searchType", "0");
        paramMap.add("searchKeyword", "??????");

        mockMvc.perform(get("/api/jpa-production/lists").queryParams(paramMap)
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Admin ???????????? ?????? ?????? ?????????")
    void ??????????????????Api???????????????() throws Exception {
        mockMvc.perform(get("/api/jpa-production/lists")
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin ???????????? ?????? ?????? ?????????")
    void ????????????????????????Api?????????() throws Exception {
        mockMvc.perform(get("/api/jpa-production/1")
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idx").value("1"))
                .andExpect(jsonPath("$.title").value("?????????1"))
                .andExpect(jsonPath("$.description").value("?????????1"));;
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Admin ???????????? ?????? ?????? ?????? ?????????")
    void ????????????????????????Api???????????????() throws Exception {
        mockMvc.perform(get("/api/jpa-production/1")
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin ???????????? ?????? ?????????")
    void ??????????????????Api?????????() throws Exception {
        mockMvc.perform(post("/api/jpa-production")
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(adminProductionEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("???????????? ?????????"))
                .andExpect(jsonPath("$.description").value("???????????? ?????????"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Admin ???????????? ?????? ?????? ?????????")
    void ??????????????????Api???????????????() throws Exception {
        mockMvc.perform(post("/api/jpa-production")
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(adminProductionEntity)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin ???????????? ?????? ?????????")
    void ??????????????????Api?????????() throws Exception {
        em.persist(adminProductionEntity);

        adminProductionEntity = builder().idx(adminProductionEntity.getIdx()).title("?????????1").description("?????????1").build();

        mockMvc.perform(put("/api/jpa-production/{idx}", adminProductionEntity.getIdx())
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(adminProductionEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("?????????1"))
                .andExpect(jsonPath("$.description").value("?????????1"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Admin ???????????? ?????? ?????? ?????????")
    void ??????????????????Api???????????????() throws Exception {
        em.persist(adminProductionEntity);

        adminProductionEntity = builder().idx(adminProductionEntity.getIdx()).title("?????????1").description("?????????1").build();

        mockMvc.perform(put("/api/jpa-production/{idx}", adminProductionEntity.getIdx())
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(adminProductionEntity)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin ???????????? ?????? ?????????")
    void ??????????????????Api?????????() throws Exception {
        em.persist(adminProductionEntity);

        mockMvc.perform(delete("/api/jpa-production/{idx}", adminProductionEntity.getIdx())
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(StringUtil.getString(adminProductionEntity.getIdx())));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Admin ???????????? ?????? ?????? ?????????")
    void ??????????????????Api???????????????() throws Exception {
        em.persist(adminProductionEntity);

        mockMvc.perform(delete("/api/jpa-production/{idx}", adminProductionEntity.getIdx())
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}