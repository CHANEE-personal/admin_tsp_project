package com.tsp.new_tsp_admin.api.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsp.new_tsp_admin.api.domain.common.CommonCodeEntity;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity.builder;
import static com.tsp.new_tsp_admin.api.domain.user.Role.ROLE_ADMIN;
import static java.util.List.of;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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
class AdminModelJpaControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private WebApplicationContext wac;
    @Autowired private EntityManager em;
    @Autowired private JwtUtil jwtUtil;

    private AdminModelEntity adminModelEntity;
    private AdminUserEntity adminUserEntity;

    Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return authorities;
    }

    void createAdminModel() {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("admin04", "pass1234", getAuthorities());
        String token = jwtUtil.doGenerateToken(authenticationToken.getName(), 1000L * 10);

        adminUserEntity = AdminUserEntity.builder()
                .userId("admin04")
                .password("pass1234")
                .name("test")
                .email("test@test.com")
                .role(ROLE_ADMIN)
                .userToken(token)
                .visible("Y")
                .build();

        em.persist(adminUserEntity);

        adminModelEntity = builder()
                .categoryCd(1)
                .categoryAge("2")
                .modelKorFirstName("???")
                .modelKorSecondName("??????")
                .modelKorName("?????????")
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
    @EventListener(ApplicationReadyEvent.class)
    public void setup(RestDocumentationContextProvider restDocumentationContextProvider) {
        this.mockMvc = webAppContextSetup(wac)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .alwaysDo(print())
                .build();

        createAdminModel();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin ?????? ?????? ?????????")
    void ????????????Api?????????() throws Exception {
        MultiValueMap<String, String> modelMap = new LinkedMultiValueMap<>();
        modelMap.add("jpaStartPage", "1");
        modelMap.add("size", "3");
        mockMvc.perform(get("/api/jpa-model/lists/1").queryParams(modelMap)
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin ?????? ?????? ?????? ?????????")
    void ??????????????????Api?????????() throws Exception {
        // ?????? ?????????
        LinkedMultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("jpaStartPage", "1");
        paramMap.add("size", "3");
        paramMap.add("searchType", "0");
        paramMap.add("searchKeyword", "?????????");

        mockMvc.perform(get("/api/jpa-model/lists/2").queryParams(paramMap)
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin ?????? ?????? ?????? ?????????")
    void ????????????Api???????????????() throws Exception {
        mockMvc.perform(get("/api/jpa-model/lists/-1")
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString().equals("?????? categoryCd??? 1~3 ?????? ?????? ????????? ??? ????????????.");
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Admin ?????? ?????? ?????? ?????????")
    void ????????????Api???????????????() throws Exception {
        mockMvc.perform(get("/api/jpa-model/lists/-1")
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin ?????? ?????? ?????? ?????????")
    void ??????????????????Api?????????() throws Exception {
        mockMvc.perform(get("/api/jpa-model/2/143")
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idx").value("143"))
                .andExpect(jsonPath("$.categoryCd").value("2"))
                .andExpect(jsonPath("$.modelFirstName").value("kim"))
                .andExpect(jsonPath("$.modelSecondName").value("ye yeong"))
                .andExpect(jsonPath("$.modelKorFirstName").value("???"))
                .andExpect(jsonPath("$.modelKorSecondName").value("??????"))
                .andExpect(jsonPath("$.height").value("173"))
                .andExpect(jsonPath("$.size3").value("31-24-34"))
                .andExpect(jsonPath("$.shoes").value("240"));
    }
    @Test
    @DisplayName("Admin ?????? ?????? ?????? ?????? ?????????")
    void ??????????????????Api???????????????() throws Exception {
        mockMvc.perform(get("/api/jpa-model/-1/1")
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString().equals("?????? categoryCd??? 1~3 ?????? ?????? ????????? ??? ????????????.");
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Admin ?????? ?????? ?????? ?????? ?????????")
    void ??????????????????Api???????????????() throws Exception {
        mockMvc.perform(get("/api/jpa-model/2/143")
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin ?????? ?????? ?????????")
    void ????????????Api?????????() throws Exception {
        mockMvc.perform(post("/api/jpa-model")
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(adminModelEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryCd").value(1))
                .andExpect(jsonPath("$.categoryAge").value("2"))
                .andExpect(jsonPath("$.modelKorFirstName").value("???"))
                .andExpect(jsonPath("$.modelKorSecondName").value("??????"))
                .andExpect(jsonPath("$.modelKorName").value("?????????"))
                .andExpect(jsonPath("$.modelFirstName").value("CHO"))
                .andExpect(jsonPath("$.modelSecondName").value("CHANHEE"))
                .andExpect(jsonPath("$.modelDescription").value("chaneeCho"))
                .andExpect(jsonPath("$.height").value("170"))
                .andExpect(jsonPath("$.size3").value("34-24-34"))
                .andExpect(jsonPath("$.shoes").value("270"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("CreatedBy, CreationTimestamp ?????????")
    void CreatedByAndCreationTimestamp?????????() throws Exception {
        AdminUserEntity adminUserEntity = AdminUserEntity.builder()
                .userId("admin02")
                .password("pass1234")
                .visible("Y")
                .build();

        mockMvc.perform(post("/api/jpa-user/login")
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(adminUserEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loginYn").value("Y"))
                .andExpect(jsonPath("$.token").isNotEmpty());

        mockMvc.perform(post("/api/jpa-model")
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(adminModelEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryCd").value(1))
                .andExpect(jsonPath("$.categoryAge").value("2"))
                .andExpect(jsonPath("$.modelKorFirstName").value("???"))
                .andExpect(jsonPath("$.modelKorSecondName").value("??????"))
                .andExpect(jsonPath("$.modelKorName").value("?????????"))
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
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin ?????? ?????? ?????? ?????????")
    void ????????????Api???????????????() throws Exception {
        AdminModelEntity exAdminModelEntity = builder()
                .categoryCd(-1)
                .categoryAge("2")
                .modelKorFirstName("???")
                .modelKorSecondName("??????")
                .modelKorName("?????????")
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
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(exAdminModelEntity)))
                .andDo(print())
                .andExpect(jsonPath("$.code").value("ERROR_MODEL"))
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("?????? ?????? ??????"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Admin ?????? ?????? ?????? ?????????")
    void ????????????Api???????????????() throws Exception {
        mockMvc.perform(post("/api/jpa-model")
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(adminModelEntity)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin ?????? ?????? ?????????")
    void ????????????Api?????????() throws Exception {
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

        mockMvc.perform(put("/api/jpa-model/{idx}", adminModelEntity.getIdx())
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(adminModelEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modelKorFirstName").value("test"))
                .andExpect(jsonPath("$.modelKorSecondName").value("test"))
                .andExpect(jsonPath("$.modelKorName").value("test"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("LastModifiedBy, UpdateTimestamp ?????????")
    void LastModifiedByAndUpdateTimestamp?????????() throws Exception {
        em.persist(adminModelEntity);

        AdminUserEntity adminUserEntity = AdminUserEntity.builder()
                .userId("admin02")
                .password("pass1234")
                .visible("Y")
                .build();

        mockMvc.perform(post("/api/jpa-user/login")
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                .contentType(APPLICATION_JSON_VALUE)
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
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(adminModelEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modelKorFirstName").value("test"))
                .andExpect(jsonPath("$.modelKorSecondName").value("test"))
                .andExpect(jsonPath("$.modelKorName").value("test"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin ?????? ?????? ?????? ?????????")
    void ????????????Api???????????????() throws Exception {
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
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(adminModelEntity)))
                .andDo(print())
                .andExpect(jsonPath("$.code").value("ERROR_UPDATE_MODEL"))
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("?????? ?????? ??????"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Admin ?????? ?????? ?????? ?????????")
    void ????????????Api???????????????() throws Exception {
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

        mockMvc.perform(put("/api/jpa-model/{idx}", adminModelEntity.getIdx())
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(adminModelEntity)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin ?????? ?????? ?????????")
    void ????????????Api?????????() throws Exception {
        em.persist(adminModelEntity);

        mockMvc.perform(delete("/api/jpa-model/{idx}", adminModelEntity.getIdx())
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(StringUtil.getString(adminModelEntity.getIdx())));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Admin ?????? ?????? ?????? ?????????")
    void ????????????Api???????????????() throws Exception {
        em.persist(adminModelEntity);

        mockMvc.perform(delete("/api/jpa-model/{idx}", adminModelEntity.getIdx())
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin ?????? ????????? ?????? ?????????")
    void ?????????????????????Api?????????() throws Exception {
        List<MultipartFile> imageFiles = of(
                new MockMultipartFile("0522045010647","0522045010647.png",
                        "image/png" , new FileInputStream("src/main/resources/static/images/0522045010647.png")),
                new MockMultipartFile("0522045010772","0522045010772.png" ,
                        "image/png" , new FileInputStream("src/main/resources/static/images/0522045010772.png"))
        );

        mockMvc.perform(multipart("/api/jpa-model/1/images")
                        .file("images", imageFiles.get(0).getBytes())
                        .file("images", imageFiles.get(1).getBytes())
                .contentType(MULTIPART_FORM_DATA_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Y"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin ?????? ????????? ?????? ?????????")
    void ?????????????????????Api?????????() throws Exception {
        CommonImageEntity commonImageEntity = CommonImageEntity.builder()
                .imageType("main")
                .fileName("test.jpg")
                .fileMask("test.jpg")
                .filePath("/test/test.jpg")
                .typeIdx(1)
                .typeName("model")
                .build();

        em.persist(commonImageEntity);

        mockMvc.perform(delete("/api/jpa-model/{idx}/images", commonImageEntity.getIdx())
                        .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                        .contentType(APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(StringUtil.getString(commonImageEntity.getIdx(),"")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void ?????????????????????????????????() throws Exception {
        CommonCodeEntity commonCodeEntity = CommonCodeEntity.builder()
                .categoryCd(1).visible("Y").cmmType("model").build();

        mockMvc.perform(get("/api/jpa-model/common")
                .header("Authorization", "Bearer " + adminUserEntity.getUserToken())
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(commonCodeEntity)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}