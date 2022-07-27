package com.tsp.new_tsp_admin.api.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsp.new_tsp_admin.api.domain.support.AdminSupportEntity;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity;
import com.tsp.new_tsp_admin.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.tsp.new_tsp_admin.api.domain.user.Role.ROLE_ADMIN;
import static com.tsp.new_tsp_admin.common.StringUtil.getString;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static com.tsp.new_tsp_admin.api.domain.support.AdminSupportEntity.builder;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@AutoConfigureRestDocs // -> apply(documentationConfiguration(restDocumentation))
@ExtendWith({RestDocumentationExtension.class})
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
class AdminSupportJpaControllerTest {
	private MockMvc mockMvc;
	private final ObjectMapper objectMapper;
	private final WebApplicationContext wac;
	private final EntityManager em;
	private final JwtUtil jwtUtil;

	private AdminSupportEntity adminSupportEntity;
	private AdminUserEntity adminUserEntity;

	Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		return authorities;
	}

	void createAdminSupport() {
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

		adminSupportEntity = builder()
				.supportName("조찬희")
				.supportMessage("조찬희 지원")
				.supportHeight(170)
				.supportPhone("010-9466-2702")
				.supportSize3("31-24-31")
				.supportInstagram("https://instagram.com")
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

		createAdminSupport();
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	@DisplayName("Admin 지원 모델 조회 테스트")
	void 지원모델조회Api테스트() throws Exception {
		MultiValueMap<String, String> supportMap = new LinkedMultiValueMap<>();
		supportMap.add("jpaStartPage", "1");
		supportMap.add("size", "3");
		mockMvc.perform(get("/api/jpa-support/lists").params(supportMap)
				.header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=utf-8"));
	}

	@Test
	@WithMockUser(roles = "USER")
	@DisplayName("Admin 지원 모델 조회 권한 테스트")
	void 지원모델조회Api권한테스트() throws Exception {
		MultiValueMap<String, String> supportMap = new LinkedMultiValueMap<>();
		supportMap.add("jpaStartPage", "1");
		supportMap.add("size", "3");
		mockMvc.perform(get("/api/jpa-support/lists").params(supportMap)
				.header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
				.andDo(print())
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	@DisplayName("Admin 지원 모델 상세 조회 테스트")
	void 지원모델상세조회Api테스트() throws Exception {
		mockMvc.perform(get("/api/jpa-support/1")
				.header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=utf-8"));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	@DisplayName("Admin 지원 모델 수정 테스트")
	void 지원모델수정Api테스트() throws Exception {
		em.persist(adminSupportEntity);

		adminSupportEntity = builder()
				.idx(adminSupportEntity.getIdx())
				.supportName("테스트")
				.supportMessage("테스트")
				.supportHeight(170)
				.supportPhone("010-9466-2702")
				.supportSize3("31-24-31")
				.supportInstagram("https://instagram.com")
				.visible("Y")
				.build();

		mockMvc.perform(put("/api/jpa-support/{idx}", adminSupportEntity.getIdx())
				.header("Authorization", "Bearer " + adminUserEntity.getUserToken())
				.contentType(APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(adminSupportEntity)))
				.andDo(print())
				.andDo(document("support/put",
						preprocessRequest(prettyPrint()),
						preprocessResponse(prettyPrint()),
						relaxedRequestFields(
								fieldWithPath("supportName").type(STRING).description("지원자 이름"),
								fieldWithPath("supportMessage").type(STRING).description("지원자 상세"),
								fieldWithPath("supportHeight").type(NUMBER).description("지원자 키"),
								fieldWithPath("supportPhone").type(STRING).description("지원자 휴대폰 번호"),
								fieldWithPath("supportSize3").type(STRING).description("지원자 사이즈"),
								fieldWithPath("supportInstagram").type(STRING).description("지원자 인스타그램"),
								fieldWithPath("visible").type(STRING).description("노출 여부")
						),
						relaxedResponseFields(
								fieldWithPath("supportName").type(STRING).description("지원자 이름"),
								fieldWithPath("supportMessage").type(STRING).description("지원자 상세"),
								fieldWithPath("supportHeight").type(NUMBER).description("지원자 키"),
								fieldWithPath("supportPhone").type(STRING).description("지원자 휴대폰 번호"),
								fieldWithPath("supportSize3").type(STRING).description("지원자 사이즈"),
								fieldWithPath("supportInstagram").type(STRING).description("지원자 인스타그램"),
								fieldWithPath("visible").type(STRING).description("노출 여부")
						)))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=utf-8"))
				.andExpect(jsonPath("$.supportName").value("테스트"))
				.andExpect(jsonPath("$.supportMessage").value("테스트"));
	}

	@Test
	@WithMockUser(roles = "USER")
	@DisplayName("Admin 지원 모델 수정 권한 테스트")
	void 지원모델수정Api권한테스트() throws Exception {
		em.persist(adminSupportEntity);

		adminSupportEntity = builder()
				.idx(adminSupportEntity.getIdx())
				.supportName("테스트")
				.supportMessage("테스트")
				.supportHeight(170)
				.supportPhone("010-9466-2702")
				.supportSize3("31-24-31")
				.supportInstagram("https://instagram.com")
				.build();

		mockMvc.perform(put("/api/jpa-support/{idx}", adminSupportEntity.getIdx())
				.header("Authorization", "Bearer " + adminUserEntity.getUserToken())
				.contentType(APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(adminSupportEntity)))
				.andDo(print())
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	@DisplayName("Admin 지원모델 삭제 테스트")
	void 지원모델삭제Api테스트() throws Exception {
		em.persist(adminSupportEntity);

		mockMvc.perform(delete("/api/jpa-support/{idx}", adminSupportEntity.getIdx())
				.header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=utf-8"))
				.andExpect(content().string(getString(adminSupportEntity.getIdx())));;
	}

	@Test
	@WithMockUser(roles = "USER")
	@DisplayName("Admin 지원모델 삭제 권한 테스트")
	void 지원모델삭제Api권한테스트() throws Exception {
		em.persist(adminSupportEntity);

		mockMvc.perform(delete("/api/jpa-support/{idx}", adminSupportEntity.getIdx())
				.header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
				.andDo(print())
				.andExpect(status().isForbidden());
	}
}