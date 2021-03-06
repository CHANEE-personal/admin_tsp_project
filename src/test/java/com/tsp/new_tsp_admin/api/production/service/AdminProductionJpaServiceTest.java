package com.tsp.new_tsp_admin.api.production.service;

import com.tsp.new_tsp_admin.api.domain.production.AdminProductionDTO;
import com.tsp.new_tsp_admin.api.domain.production.AdminProductionEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.event.EventListener;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import java.util.HashMap;
import java.util.Map;

import static com.tsp.new_tsp_admin.api.domain.production.AdminProductionEntity.builder;
import static com.tsp.new_tsp_admin.api.production.mapper.ProductionMapper.INSTANCE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("프로덕션 Service Test")
class AdminProductionJpaServiceTest {
    AdminProductionEntity adminProductionEntity;
    AdminProductionDTO adminProductionDTO;
    @Autowired private AdminProductionJpaService adminProductionJpaService;
    @Mock private AdminProductionJpaService mockAdminProductionJpaService;

    void createProduction() {
        adminProductionEntity = builder()
                .title("프로덕션 테스트")
                .description("프로덕션 테스트")
                .visible("Y")
                .build();

        adminProductionDTO = INSTANCE.toDto(adminProductionEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createProduction();
    }

    @Test
    @DisplayName("프로덕션 리스트 조회 테스트")
    void 프로덕션리스트조회테스트() throws Exception {
        // given
        Map<String, Object> productionMap = new HashMap<>();
        productionMap.put("jpaStartPage", 1);
        productionMap.put("size", 3);

        assertThat(adminProductionJpaService.findProductionsList(productionMap)).isNotEmpty();
    }

    @Test
    @DisplayName("프로덕션 상세 조회 테스트")
    void 프로덕션상세조회테스트() throws Exception {
        // given
        adminProductionEntity = builder().idx(119).build();

        assertThat(adminProductionJpaService.findOneProduction(adminProductionEntity).getTitle()).isEqualTo("하하");
    }

    @Test
    @DisplayName("프로덕션 등록 테스트")
    void 프로덕션등록테스트() throws Exception {
        // given
        adminProductionJpaService.insertProduction(adminProductionEntity);

        // when
        when(mockAdminProductionJpaService.findOneProduction(adminProductionEntity)).thenReturn(adminProductionDTO);

        // then
        assertThat(mockAdminProductionJpaService.findOneProduction(adminProductionEntity).getTitle()).isEqualTo("프로덕션 테스트");
        assertThat(mockAdminProductionJpaService.findOneProduction(adminProductionEntity).getDescription()).isEqualTo("프로덕션 테스트");
    }

    @Test
    @DisplayName("프로덕션 수정 테스트")
    void 프로덕션수정테스트() throws Exception {
        // given
        Integer idx = adminProductionJpaService.insertProduction(adminProductionEntity).getIdx();

        adminProductionEntity = builder()
                .idx(idx)
                .title("프로덕션 테스트1")
                .description("프로덕션 테스트1")
                .visible("Y")
                .build();

        AdminProductionDTO adminProductionDTO = INSTANCE.toDto(adminProductionEntity);

        adminProductionJpaService.updateProduction(adminProductionEntity);

        // when
        when(mockAdminProductionJpaService.findOneProduction(adminProductionEntity)).thenReturn(adminProductionDTO);

        // then
        assertThat(mockAdminProductionJpaService.findOneProduction(adminProductionEntity).getTitle()).isEqualTo("프로덕션 테스트1");
        assertThat(mockAdminProductionJpaService.findOneProduction(adminProductionEntity).getDescription()).isEqualTo("프로덕션 테스트1");
    }

    @Test
    @DisplayName("프로덕션 삭제 테스트")
    void 프로덕션삭제테스트() throws Exception {
        Integer idx = adminProductionJpaService.insertProduction(adminProductionEntity).getIdx();

        assertThat(adminProductionJpaService.deleteProduction(idx)).isNotNull();
    }
}