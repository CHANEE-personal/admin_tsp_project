package com.tsp.new_tsp_admin.api.production.service;

import com.tsp.new_tsp_admin.api.domain.production.AdminProductionDTO;
import com.tsp.new_tsp_admin.api.domain.production.AdminProductionEntity;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_admin.api.domain.production.AdminProductionEntity.builder;
import static com.tsp.new_tsp_admin.api.production.mapper.ProductionMapper.INSTANCE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("프로덕션 Service Test")
class AdminProductionJpaServiceTest {
    @Mock private AdminProductionJpaService mockAdminProductionJpaService;
    private final AdminProductionJpaService adminProductionJpaService;

    private AdminProductionEntity adminProductionEntity;
    private AdminProductionDTO adminProductionDTO;

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

        // then
        assertThat(adminProductionJpaService.findProductionsList(productionMap)).isNotEmpty();
    }

    @Test
    @DisplayName("프로덕션 리스트 조회 BDD 테스트")
    void 프로덕션리스트조회BDD테스트() throws Exception {
        // given
        Map<String, Object> productionMap = new HashMap<>();
        productionMap.put("jpaStartPage", 1);
        productionMap.put("size", 3);

        List<AdminProductionDTO> returnProductionList = new ArrayList<>();

        returnProductionList.add(AdminProductionDTO.builder().idx(1).title("프로덕션테스트").description("프로덕션테스트").visible("Y").build());
        returnProductionList.add(AdminProductionDTO.builder().idx(2).title("productionTest").description("productionTest").visible("Y").build());

        // when
        when(mockAdminProductionJpaService.findProductionsList(productionMap)).thenReturn(returnProductionList);
        List<AdminProductionDTO> productionList = mockAdminProductionJpaService.findProductionsList(productionMap);

        // then
        assertAll(
                () -> assertThat(productionList).isNotEmpty(),
                () -> assertThat(productionList).hasSize(2)
        );

        assertThat(productionList.get(0).getIdx()).isEqualTo(returnProductionList.get(0).getIdx());
        assertThat(productionList.get(0).getTitle()).isEqualTo(returnProductionList.get(0).getTitle());
        assertThat(productionList.get(0).getDescription()).isEqualTo(returnProductionList.get(0).getDescription());
        assertThat(productionList.get(0).getVisible()).isEqualTo(returnProductionList.get(0).getVisible());

        // verify
        verify(mockAdminProductionJpaService, times(1)).findProductionsList(productionMap);
        verify(mockAdminProductionJpaService, atLeastOnce()).findProductionsList(productionMap);
        verifyNoMoreInteractions(mockAdminProductionJpaService);
    }

    @Test
    @DisplayName("프로덕션 상세 조회 테스트")
    void 프로덕션상세조회테스트() throws Exception {
        // given
        adminProductionEntity = builder().idx(119).build();

        // then
        assertThat(adminProductionJpaService.findOneProduction(adminProductionEntity).getTitle()).isEqualTo("하하");
    }

    @Test
    @DisplayName("프로덕션상세조회BDD테스트")
    void 프로덕션상세조회BDD테스트() throws Exception {
        // when
        when(mockAdminProductionJpaService.findOneProduction(adminProductionEntity)).thenReturn(adminProductionDTO);
        AdminProductionDTO productionInfo = mockAdminProductionJpaService.findOneProduction(adminProductionEntity);

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(adminProductionDTO.getIdx());
        assertThat(productionInfo.getTitle()).isEqualTo(adminProductionDTO.getTitle());
        assertThat(productionInfo.getDescription()).isEqualTo(adminProductionDTO.getDescription());
        assertThat(productionInfo.getVisible()).isEqualTo(adminProductionDTO.getVisible());

        // verify
        verify(mockAdminProductionJpaService, times(1)).findOneProduction(adminProductionEntity);
        verify(mockAdminProductionJpaService, atLeastOnce()).findOneProduction(adminProductionEntity);
        verifyNoMoreInteractions(mockAdminProductionJpaService);
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

        // verify
        verify(mockAdminProductionJpaService, times(2)).findOneProduction(adminProductionEntity);
        verify(mockAdminProductionJpaService, atLeastOnce()).findOneProduction(adminProductionEntity);
        verifyNoMoreInteractions(mockAdminProductionJpaService);
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

        // verify
        verify(mockAdminProductionJpaService, times(2)).findOneProduction(adminProductionEntity);
        verify(mockAdminProductionJpaService, atLeastOnce()).findOneProduction(adminProductionEntity);
        verifyNoMoreInteractions(mockAdminProductionJpaService);
    }

    @Test
    @DisplayName("프로덕션 삭제 테스트")
    void 프로덕션삭제테스트() throws Exception {
        // given
        Integer idx = adminProductionJpaService.insertProduction(adminProductionEntity).getIdx();

        // then
        assertThat(adminProductionJpaService.deleteProduction(idx)).isNotNull();
    }
}