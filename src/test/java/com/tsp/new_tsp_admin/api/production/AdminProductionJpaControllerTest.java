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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static com.tsp.new_tsp_admin.api.domain.production.AdminProductionEntity.builder;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
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

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("Admin 프로덕션 조회 테스트")
    public void 프로덕션조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/jpa-production/lists"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productionList.length()", greaterThan(0)));
    }

    @Test
    @DisplayName("Admin 프로덕션 상세 조회 테스트")
    public void 프로덕션상세조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/jpa-production/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idx").value("1"))
                .andExpect(jsonPath("$.title").value("프로덕션 테스트"))
                .andExpect(jsonPath("$.description").value("프로덕션 테스트"));;
    }

    @Test
    @DisplayName("Admin 프로덕션 등록 테스트")
    public void 프로덕션등록Api테스트() throws Exception {
        AdminProductionEntity adminProductionEntity = builder()
                .title("프로덕션 테스트")
                .description("프로덕션 테스트")
                .visible("Y")
                .build();

        final String jsonStr = objectMapper.writeValueAsString(adminProductionEntity);

        mockMvc.perform(post("/api/jpa-production")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonStr))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Admin 프로덕션 수정 테스트")
    public void 프로덕션수정Api테스트() throws Exception {
        AdminProductionEntity adminProductionEntity = builder()
                .idx(1)
                .title("프로덕션 테스트")
                .description("프로덕션 테스트")
                .visible("Y")
                .build();

        final String jsonStr = objectMapper.writeValueAsString(adminProductionEntity);

        mockMvc.perform(post("/api/jpa-production")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonStr))
                .andDo(print())
                .andExpect(status().isOk());

        adminProductionEntity = builder().idx(1).title("테스트1").description("테스트1").build();

        final String updateStr = objectMapper.writeValueAsString(adminProductionEntity);

        mockMvc.perform(put("/api/jpa-production/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(updateStr))
                .andDo(print())
                .andExpect(status().isOk());
    }
}