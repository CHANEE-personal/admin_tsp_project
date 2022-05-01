package com.tsp.new_tsp_admin.api.User;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsp.new_tsp_admin.api.domain.User.AdminUserEntity;
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

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AdminUserJpaControllerTest {

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
    @DisplayName("로그인 테스트")
    public void 로그인테스트() throws Exception {

        AdminUserEntity adminUserEntity = AdminUserEntity.builder()
                        .userId("admin")
                        .password("pass1234")
                        .build();

        final String jsonStr = objectMapper.writeValueAsString(adminUserEntity);

        mockMvc.perform(post("/api/jpa-user/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonStr))
                .andDo(print())
                .andExpect(status().isOk());

    }
}