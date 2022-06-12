package com.tsp.new_tsp_admin.api.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsp.new_tsp_admin.api.domain.support.AdminSupportEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.Collections;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static com.tsp.new_tsp_admin.api.domain.support.AdminSupportEntity.builder;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AdminSupportJpaControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private EntityManager em;

	private AdminSupportEntity adminSupportEntity;

	public void createAdminSupport() {
		adminSupportEntity = builder()
				.supportName("조찬희")
				.supportMessage("조찬희 지원")
				.supportHeight(170)
				.supportPhone("010-9466-2702")
				.supportSize3("31-24-31")
				.supportInstagram("https://instagram.com")
				.build();
	}

	@BeforeEach
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
				.addFilter(new CharacterEncodingFilter("UTF-8", true))
				.alwaysDo(print())
				.build();

		createAdminSupport();
	}

	@Test
	@DisplayName("Admin 지원 모델 조회 테스트")
	public void 지원모델조회Api테스트() throws Exception {
		MultiValueMap<String, String> supportMap = new LinkedMultiValueMap<>();
		supportMap.put("jpaStartPage", Collections.singletonList("1"));
		supportMap.put("size", Collections.singletonList("3"));
		mockMvc.perform(get("/api/jpa-support/lists").params(supportMap))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Disabled
	@DisplayName("Admin 지원 모델 상세 조회 테스트")
	public void 지원모델상세조회Api테스트() throws Exception {
		mockMvc.perform(get("/api/jpa-support/1"))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	@DisplayName("Admin 지원 모델 수정 테스트")
	public void 지원모델수정Api테스트() throws Exception {
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
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(adminSupportEntity)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.supportName").value("테스트"))
				.andExpect(jsonPath("$.supportMessage").value("테스트"));
	}

	@Test
	@DisplayName("Admin 지원모델 삭제 테스트")
	public void 지원모델삭제Api테스트() throws Exception {
		em.persist(adminSupportEntity);

		mockMvc.perform(delete("/api/jpa-support/{idx}", adminSupportEntity.getIdx())
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(objectMapper.writeValueAsString(adminSupportEntity)))
				.andDo(print())
				.andExpect(status().isOk());
	}
}