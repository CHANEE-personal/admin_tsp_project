package com.tsp.new_tsp_admin.api.portfolio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.Collections;

import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
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

    private AdminPortFolioEntity adminPortFolioEntity;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();

        adminPortFolioEntity = AdminPortFolioEntity.builder()
                .categoryCd(1)
                .title("포트폴리오 테스트")
                .description("포트폴리오 테스트")
                .hashTag("#test")
                .videoUrl("https://youtube.com")
                .visible("Y")
                .build();
    }

    @Test
    @DisplayName("Admin 포트폴리오 조회 테스트")
    public void 포트폴리오조회Api테스트() throws Exception {
        MultiValueMap<String, String> portfolioMap = new LinkedMultiValueMap<>();
        portfolioMap.put("jpaStartPage", Collections.singletonList("1"));
        portfolioMap.put("size", Collections.singletonList("3"));
        mockMvc.perform(get("/api/jpa-portfolio/lists").params(portfolioMap))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.portfolioList.length()", greaterThan(0)));
    }

    @Test
    @DisplayName("Admin 포트폴리오 상세 조회 테스트")
    public void 포트폴리오상세조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/jpa-portfolio/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idx").value("1"));
    }

    @Test
    @Transactional
    @DisplayName("Admin 포트폴리오 등록 테스트")
    public void 포트폴리오등록Api테스트() throws Exception {
        final String jsonStr = objectMapper.writeValueAsString(adminPortFolioEntity);

        mockMvc.perform(post("/api/jpa-portfolio")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonStr))
                .andDo(print())
                .andExpect(status().isOk());
    }
}