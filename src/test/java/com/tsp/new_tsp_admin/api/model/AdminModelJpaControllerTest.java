package com.tsp.new_tsp_admin.api.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.transaction.Transactional;
import java.util.Collections;

import static com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity.builder;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AdminModelJpaControllerTest {

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
    @DisplayName("Admin 모델 조회 테스트")
    public void 모델조회Api테스트() throws Exception {
        MultiValueMap<String, String> modelMap = new LinkedMultiValueMap<>();
        modelMap.put("jpaStartPage", Collections.singletonList("1"));
        modelMap.put("size", Collections.singletonList("3"));
        mockMvc.perform(get("/api/jpa-model/lists/1").params(modelMap))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modelList.length()", greaterThan(0)));
    }

    @Test
    @DisplayName("Admin 모델 상세 조회 테스트")
    public void 모델상세조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/jpa-model/1/3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idx").value("3"))
                .andExpect(jsonPath("$.categoryCd").value("1"))
                .andExpect(jsonPath("$.modelFirstName").value("CHO"))
                .andExpect(jsonPath("$.modelSecondName").value("CHAN HEE"))
                .andExpect(jsonPath("$.modelKorFirstName").value("조"))
                .andExpect(jsonPath("$.modelKorSecondName").value("찬희"))
                .andExpect(jsonPath("$.height").value("170"))
                .andExpect(jsonPath("$.size3").value("34-24-34"))
                .andExpect(jsonPath("$.shoes").value("270"));;
    }

    @Test
    @Transactional
    @DisplayName("Admin 모델 등록 테스트")
    public void 모델등록Api테스트() throws Exception {

        AdminModelEntity adminModelEntity = builder()
                .categoryCd(1)
                .categoryAge("2")
                .modelKorFirstName("조")
                .modelKorSecondName("찬희")
                .modelKorName("조찬희")
                .modelFirstName("CHO")
                .modelSecondName("CHANHEE")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .modelMainYn("Y")
                .height("170")
                .size3("34-24-34")
                .shoes("270")
                .visible("Y")
                .build();

        final String jsonStr = objectMapper.writeValueAsString(adminModelEntity);

        mockMvc.perform(post("/api/jpa-model")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonStr))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Admin 모델 수정 테스트")
    public void 모델수정Api테스트() throws Exception {

        AdminModelEntity adminModelEntity = builder()
                .categoryCd(1)
                .categoryAge("2")
                .modelKorFirstName("조")
                .modelKorSecondName("찬희")
                .modelKorName("조찬희")
                .modelFirstName("CHO")
                .modelSecondName("CHANHEE")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .modelMainYn("Y")
                .height("170")
                .size3("34-24-34")
                .shoes("270")
                .visible("Y")
                .build();

        final String jsonStr = objectMapper.writeValueAsString(adminModelEntity);

        mockMvc.perform(post("/api/jpa-model")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonStr))
                .andDo(print())
                .andExpect(status().isOk());

        adminModelEntity = builder().idx(1)
                .categoryCd(1)
                .categoryAge("2")
                .modelKorFirstName("test")
                .modelKorSecondName("test")
                .modelKorName("test")
                .modelFirstName("test")
                .modelSecondName("test")
                .modelEngName("test")
                .modelDescription("test")
                .modelMainYn("Y")
                .height("170")
                .size3("34-24-34")
                .shoes("270")
                .visible("Y")
                .build();

        final String updateStr = objectMapper.writeValueAsString(adminModelEntity);

        mockMvc.perform(put("/api/jpa-model/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(updateStr))
                .andDo(print())
                .andExpect(status().isOk());
    }
}