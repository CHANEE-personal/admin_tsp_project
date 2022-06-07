package com.tsp.new_tsp_admin.api.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsp.new_tsp_admin.api.domain.common.CommonCodeEntity;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.util.Collections;
import java.util.List;

import static com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity.builder;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
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

    @Autowired
    private EntityManager em;

    private AdminModelEntity adminModelEntity;

    public void createAdminModel() {
        adminModelEntity = builder()
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
                .status("draft")
                .visible("Y")
                .build();
    }

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();

        createAdminModel();
    }

    @Test
    @DisplayName("Admin 모델 조회 테스트")
    public void 모델조회Api테스트() throws Exception {
        MultiValueMap<String, String> modelMap = new LinkedMultiValueMap<>();
        modelMap.put("jpaStartPage", Collections.singletonList("1"));
        modelMap.put("size", Collections.singletonList("3"));
        mockMvc.perform(get("/api/jpa-model/lists/1").params(modelMap))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Admin 모델 조회 예외 테스트")
    public void 모델조회Api예외테스트() throws Exception {
        mockMvc.perform(get("/api/jpa-model/lists/-1"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString().equals("모델 categoryCd는 1~3 사이 값만 입력할 수 있습니다.");
    }

    @Test
    @DisplayName("Admin 모델 상세 조회 테스트")
    public void 모델상세조회Api테스트() throws Exception {
        mockMvc.perform(get("/api/jpa-model/2/143"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idx").value("143"))
                .andExpect(jsonPath("$.categoryCd").value("2"))
                .andExpect(jsonPath("$.modelFirstName").value("kim"))
                .andExpect(jsonPath("$.modelSecondName").value("ye yeong"))
                .andExpect(jsonPath("$.modelKorFirstName").value("김"))
                .andExpect(jsonPath("$.modelKorSecondName").value("예영"))
                .andExpect(jsonPath("$.height").value("173"))
                .andExpect(jsonPath("$.size3").value("31-24-34"))
                .andExpect(jsonPath("$.shoes").value("240"));
    }
    @Test
    @DisplayName("Admin 모델 상세 조회 예외 테스트")
    public void 모델상세조회Api예외테스트() throws Exception {
        mockMvc.perform(get("/api/jpa-model/-1/1"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString().equals("모델 categoryCd는 1~3 사이 값만 입력할 수 있습니다.");
    }

    @Test
    @Transactional
    @DisplayName("Admin 모델 등록 테스트")
    public void 모델등록Api테스트() throws Exception {
        mockMvc.perform(post("/api/jpa-model")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(adminModelEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryCd").value(1))
                .andExpect(jsonPath("$.categoryAge").value("2"))
                .andExpect(jsonPath("$.modelKorFirstName").value("조"))
                .andExpect(jsonPath("$.modelKorSecondName").value("찬희"))
                .andExpect(jsonPath("$.modelKorName").value("조찬희"))
                .andExpect(jsonPath("$.modelFirstName").value("CHO"))
                .andExpect(jsonPath("$.modelSecondName").value("CHANHEE"))
                .andExpect(jsonPath("$.modelDescription").value("chaneeCho"))
                .andExpect(jsonPath("$.height").value("170"))
                .andExpect(jsonPath("$.size3").value("34-24-34"))
                .andExpect(jsonPath("$.shoes").value("270"));
    }

    @Test
    @Transactional
    @DisplayName("CreatedBy, CreationTimestamp 테스트")
    public void CreatedByAndCreationTimestamp테스트() throws Exception {
        AdminUserEntity adminUserEntity = AdminUserEntity.builder()
                .userId("admin02")
                .password("pass1234")
                .visible("Y")
                .build();

        mockMvc.perform(post("/api/jpa-user/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(adminUserEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loginYn").value("Y"))
                .andExpect(jsonPath("$.token").isNotEmpty());

        mockMvc.perform(post("/api/jpa-model")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(adminModelEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryCd").value(1))
                .andExpect(jsonPath("$.categoryAge").value("2"))
                .andExpect(jsonPath("$.modelKorFirstName").value("조"))
                .andExpect(jsonPath("$.modelKorSecondName").value("찬희"))
                .andExpect(jsonPath("$.modelKorName").value("조찬희"))
                .andExpect(jsonPath("$.modelFirstName").value("CHO"))
                .andExpect(jsonPath("$.modelSecondName").value("CHANHEE"))
                .andExpect(jsonPath("$.modelDescription").value("chaneeCho"))
                .andExpect(jsonPath("$.height").value("170"))
                .andExpect(jsonPath("$.size3").value("34-24-34"))
                .andExpect(jsonPath("$.shoes").value("270"))
                .andExpect(jsonPath("$.creator").isNotEmpty())
                .andExpect(jsonPath("$.createTime").isNotEmpty());
    }

    @Test
    @Transactional
    @DisplayName("Admin 모델 등록 예외 테스트")
    public void 모델등록Api예외테스트() throws Exception {
        AdminModelEntity exAdminModelEntity = builder()
                .categoryCd(-1)
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
                .status("draft")
                .visible("Y")
                .build();

        mockMvc.perform(post("/api/jpa-model")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(exAdminModelEntity)))
                .andDo(print())
                .andExpect(jsonPath("$.code").value("ERROR_MODEL"))
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("모델 등록 에러"));
    }

    @Test
    @DisplayName("Admin 모델 수정 테스트")
    public void 모델수정Api테스트() throws Exception {
        em.persist(adminModelEntity);

        adminModelEntity = builder()
                .idx(adminModelEntity.getIdx())
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
                .status("active")
                .build();

        mockMvc.perform(put("/api/jpa-model/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(adminModelEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modelKorFirstName").value("test"))
                .andExpect(jsonPath("$.modelKorSecondName").value("test"))
                .andExpect(jsonPath("$.modelKorName").value("test"));
    }

    @Test
    @Transactional
    @DisplayName("LastModifiedBy, UpdateTimestamp 테스트")
    public void LastModifiedByAndUpdateTimestamp테스트() throws Exception {

        em.persist(adminModelEntity);

        AdminUserEntity adminUserEntity = AdminUserEntity.builder()
                .userId("admin02")
                .password("pass1234")
                .visible("Y")
                .build();

        mockMvc.perform(post("/api/jpa-user/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(adminUserEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loginYn").value("Y"))
                .andExpect(jsonPath("$.token").isNotEmpty());

        adminModelEntity = builder()
                .idx(adminModelEntity.getIdx())
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
                .status("active")
                .build();

        mockMvc.perform(put("/api/jpa-model/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(adminModelEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modelKorFirstName").value("test"))
                .andExpect(jsonPath("$.modelKorSecondName").value("test"))
                .andExpect(jsonPath("$.modelKorName").value("test"));
    }

    @Test
    @DisplayName("Admin 모델 수정 예외 테스트")
    public void 모델수정Api예외테스트() throws Exception {
        em.persist(adminModelEntity);

        adminModelEntity = builder()
                .idx(adminModelEntity.getIdx())
                .categoryCd(-1)
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
                .status("active")
                .build();

        mockMvc.perform(put("/api/jpa-model/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(adminModelEntity)))
                .andDo(print())
                .andExpect(jsonPath("$.code").value("ERROR_UPDATE_MODEL"))
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("모델 수정 에러"));
    }

    @Test
    @DisplayName("Admin 모델 삭제 테스트")
    public void 모델삭제Api테스트() throws Exception {
        em.persist(adminModelEntity);

        mockMvc.perform(delete("/api/jpa-model/"+adminModelEntity.getIdx())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(adminModelEntity)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Admin 모델 이미지 등록 테스트")
    public void 모델이미지등록Api테스트() throws Exception {

        List<MultipartFile> imageFiles = List.of(
                new MockMultipartFile("0522045010647","0522045010647.png",
                        "image/png" , new FileInputStream("src/main/resources/static/images/0522045010647.png")),
                new MockMultipartFile("0522045010772","0522045010772.png" ,
                        "image/png" , new FileInputStream("src/main/resources/static/images/0522045010772.png"))
        );

        mockMvc.perform(multipart("/api/jpa-model/1/images")
                        .file("images", imageFiles.get(0).getBytes())
                        .file("images", imageFiles.get(1).getBytes())
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 모델공통코드조회테스트() throws Exception {
        CommonCodeEntity commonCodeEntity = CommonCodeEntity.builder()
                .categoryCd(1).visible("Y").cmmType("model").build();

        mockMvc.perform(get("/api/jpa-model/common")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(commonCodeEntity)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}