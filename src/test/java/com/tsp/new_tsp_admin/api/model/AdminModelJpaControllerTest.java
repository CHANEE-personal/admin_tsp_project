package com.tsp.new_tsp_admin.api.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

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

//    static Stream<Arguments> modelMap() {
//        Map<String, Object> modelMap = new HashMap<>();
//        return Stream.of(
//                Arguments.of(modelMap.put("jpaStartPage", 1)),
//                Arguments.of(modelMap.put("size", 3))
//        );
//    }

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

//    @ParameterizedTest
//    @MethodSource("modelMap")
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
}