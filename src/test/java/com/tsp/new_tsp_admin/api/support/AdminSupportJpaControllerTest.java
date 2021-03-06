package com.tsp.new_tsp_admin.api.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsp.new_tsp_admin.api.domain.support.AdminSupportEntity;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
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
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static com.tsp.new_tsp_admin.api.domain.support.AdminSupportEntity.builder;
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
class AdminSupportJpaControllerTest {
	@Autowired private MockMvc mockMvc;
	@Autowired private ObjectMapper objectMapper;
	@Autowired private WebApplicationContext wac;
	@Autowired private EntityManager em;
	@Autowired private JwtUtil jwtUtil;

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
				.supportName("?????????")
				.supportMessage("????????? ??????")
				.supportHeight(170)
				.supportPhone("010-9466-2702")
				.supportSize3("31-24-31")
				.supportInstagram("https://instagram.com")
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
	@DisplayName("Admin ?????? ?????? ?????? ?????????")
	void ??????????????????Api?????????() throws Exception {
		MultiValueMap<String, String> supportMap = new LinkedMultiValueMap<>();
		supportMap.add("jpaStartPage", "1");
		supportMap.add("size", "3");
		mockMvc.perform(get("/api/jpa-support/lists").params(supportMap)
				.header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = "USER")
	@DisplayName("Admin ?????? ?????? ?????? ?????? ?????????")
	void ??????????????????Api???????????????() throws Exception {
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
	@DisplayName("Admin ?????? ?????? ?????? ?????? ?????????")
	void ????????????????????????Api?????????() throws Exception {
		mockMvc.perform(get("/api/jpa-support/1")
				.header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	@DisplayName("Admin ?????? ?????? ?????? ?????????")
	void ??????????????????Api?????????() throws Exception {
		em.persist(adminSupportEntity);

		adminSupportEntity = builder()
				.idx(adminSupportEntity.getIdx())
				.supportName("?????????")
				.supportMessage("?????????")
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
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.supportName").value("?????????"))
				.andExpect(jsonPath("$.supportMessage").value("?????????"));
	}

	@Test
	@WithMockUser(roles = "USER")
	@DisplayName("Admin ?????? ?????? ?????? ?????? ?????????")
	void ??????????????????Api???????????????() throws Exception {
		em.persist(adminSupportEntity);

		adminSupportEntity = builder()
				.idx(adminSupportEntity.getIdx())
				.supportName("?????????")
				.supportMessage("?????????")
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
	@DisplayName("Admin ???????????? ?????? ?????????")
	void ??????????????????Api?????????() throws Exception {
		em.persist(adminSupportEntity);

		mockMvc.perform(delete("/api/jpa-support/{idx}", adminSupportEntity.getIdx())
				.header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(StringUtil.getString(adminSupportEntity.getIdx())));;
	}

	@Test
	@WithMockUser(roles = "USER")
	@DisplayName("Admin ???????????? ?????? ?????? ?????????")
	void ??????????????????Api???????????????() throws Exception {
		em.persist(adminSupportEntity);

		mockMvc.perform(delete("/api/jpa-support/{idx}", adminSupportEntity.getIdx())
				.header("Authorization", "Bearer " + adminUserEntity.getUserToken()))
				.andDo(print())
				.andExpect(status().isForbidden());
	}
}