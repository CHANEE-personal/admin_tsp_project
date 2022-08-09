package com.tsp.new_tsp_admin.api.common.service;

import com.tsp.new_tsp_admin.api.common.mapper.CommonCodeMapper;
import com.tsp.new_tsp_admin.api.domain.common.CommonCodeDTO;
import com.tsp.new_tsp_admin.api.domain.common.CommonCodeEntity;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelDTO;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import com.tsp.new_tsp_admin.api.model.service.AdminModelJpaService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.event.EventListener;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import java.util.HashMap;
import java.util.Map;

import static com.tsp.new_tsp_admin.api.common.mapper.CommonCodeMapper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("공통코드 Service Test")
class AdminCommonJpaServiceTest {
    @Mock private AdminCommonJpaService mockAdminCommonJpaService;
    private final AdminCommonJpaService adminCommonJpaService;
    private CommonCodeEntity commonCodeEntity;
    private CommonCodeDTO commonCodeDTO;

    public void createCommonCode() {

        // 공통코드 생성
        commonCodeEntity = CommonCodeEntity.builder()
                .categoryCd(1)
                .categoryNm("남성")
                .cmmType("model")
                .visible("Y")
                .build();

        commonCodeDTO = INSTANCE.toDto(commonCodeEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createCommonCode();
    }

    @Test
    @DisplayName("공통코드 리스트 조회 테스트")
    void 공통코드리스트조회테스트() throws Exception {
        // given
        Map<String, Object> commonMap = new HashMap<>();
        commonMap.put("jpaStartPage", 0);
        commonMap.put("size", 100);

        // then
        assertThat(adminCommonJpaService.commonCodeList(commonMap)).isNotEmpty();
    }
}