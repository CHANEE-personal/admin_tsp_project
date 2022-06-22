package com.tsp.new_tsp_admin.api.production;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsp.new_tsp_admin.api.domain.production.AdminProductionEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static com.tsp.new_tsp_admin.api.domain.production.AdminProductionEntity.builder;
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

    private AdminProductionEntity adminProductionEntity;

    public void createAdminProduction() {
        adminProductionEntity = builder()
                .title("프로덕션 테스트")
                .description("프로덕션 테스트")
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

        createAdminProduction();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 프로덕션 조회 테스트")
    public void 프로덕션조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/jpa-production/lists"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productionList.length()", greaterThan(0)));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Admin 프로덕션 조회 권한 테스트")
    public void 프로덕션조회Api권한테스트() throws Exception {
        mockMvc.perform(get("/api/jpa-production/lists"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 프로덕션 상세 조회 테스트")
    public void 프로덕션상세조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/jpa-production/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idx").value("1"))
                .andExpect(jsonPath("$.title").value("테스트1"))
                .andExpect(jsonPath("$.description").value("테스트1"));;
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Admin 프로덕션 상세 조회 권한 테스트")
    public void 프로덕션상세조회Api권한테스트() throws Exception {
        mockMvc.perform(get("/api/jpa-production/1"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 프로덕션 등록 테스트")
    public void 프로덕션등록Api테스트() throws Exception {
        mockMvc.perform(post("/api/jpa-production")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(adminProductionEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("프로덕션 테스트"))
                .andExpect(jsonPath("$.description").value("프로덕션 테스트"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Admin 프로덕션 등록 권한 테스트")
    public void 프로덕션등록Api권한테스트() throws Exception {
        mockMvc.perform(post("/api/jpa-production")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(adminProductionEntity)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 프로덕션 수정 테스트")
    public void 프로덕션수정Api테스트() throws Exception {
        em.persist(adminProductionEntity);

        adminProductionEntity = builder().idx(adminProductionEntity.getIdx()).title("테스트1").description("테스트1").build();

        mockMvc.perform(put("/api/jpa-production/{idx}", adminProductionEntity.getIdx())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(adminProductionEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("테스트1"))
                .andExpect(jsonPath("$.description").value("테스트1"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Admin 프로덕션 수정 권한 테스트")
    public void 프로덕션수정Api권한테스트() throws Exception {
        em.persist(adminProductionEntity);

        adminProductionEntity = builder().idx(adminProductionEntity.getIdx()).title("테스트1").description("테스트1").build();

        mockMvc.perform(put("/api/jpa-production/{idx}", adminProductionEntity.getIdx())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(adminProductionEntity)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin 프로덕션 삭제 테스트")
    public void 프로덕션삭제Api테스트() throws Exception {
        em.persist(adminProductionEntity);

        mockMvc.perform(delete("/api/jpa-production/{idx}", adminProductionEntity.getIdx())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(adminProductionEntity)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Admin 프로덕션 삭제 권한 테스트")
    public void 프로덕션삭제Api권한테스트() throws Exception {
        em.persist(adminProductionEntity);

        mockMvc.perform(delete("/api/jpa-production/{idx}", adminProductionEntity.getIdx())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(adminProductionEntity)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}