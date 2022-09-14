package com.tsp.new_tsp_admin.api.production.service;

import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentDTO;
import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentEntity;
import com.tsp.new_tsp_admin.api.domain.production.AdminProductionDTO;
import com.tsp.new_tsp_admin.api.domain.production.AdminProductionEntity;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
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

import static com.tsp.new_tsp_admin.api.production.mapper.ProductionMapper.INSTANCE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
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
    private AdminCommentEntity adminCommentEntity;
    private AdminCommentDTO adminCommentDTO;

    void createProduction() {
        adminProductionEntity = AdminProductionEntity.builder()
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
    @DisplayName("프로덕션 리스트 조회 Mockito 테스트")
    void 프로덕션리스트조회Mockito테스트() throws Exception {
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

        InOrder inOrder = inOrder(mockAdminProductionJpaService);
        inOrder.verify(mockAdminProductionJpaService).findProductionsList(productionMap);
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
        given(mockAdminProductionJpaService.findProductionsList(productionMap)).willReturn(returnProductionList);
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
        then(mockAdminProductionJpaService).should(times(1)).findProductionsList(productionMap);
        then(mockAdminProductionJpaService).should(atLeastOnce()).findProductionsList(productionMap);
        then(mockAdminProductionJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("프로덕션 상세 조회 테스트")
    void 프로덕션상세조회테스트() throws Exception {
        // given
        adminProductionEntity = AdminProductionEntity.builder().idx(119).build();

        // then
        assertThat(adminProductionJpaService.findOneProduction(adminProductionEntity).getTitle()).isEqualTo("하하");
    }

    @Test
    @DisplayName("프로덕션상세조회Mockito테스트")
    void 프로덕션상세조회Mockito테스트() throws Exception {
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

        InOrder inOrder = inOrder(mockAdminProductionJpaService);
        inOrder.verify(mockAdminProductionJpaService).findOneProduction(adminProductionEntity);
    }

    @Test
    @DisplayName("프로덕션상세조회BDD테스트")
    void 프로덕션상세조회BDD테스트() throws Exception {
        // when
        given(mockAdminProductionJpaService.findOneProduction(adminProductionEntity)).willReturn(adminProductionDTO);
        AdminProductionDTO productionInfo = mockAdminProductionJpaService.findOneProduction(adminProductionEntity);

        // then
        assertThat(productionInfo.getIdx()).isEqualTo(adminProductionDTO.getIdx());
        assertThat(productionInfo.getTitle()).isEqualTo(adminProductionDTO.getTitle());
        assertThat(productionInfo.getDescription()).isEqualTo(adminProductionDTO.getDescription());
        assertThat(productionInfo.getVisible()).isEqualTo(adminProductionDTO.getVisible());

        // verify
        then(mockAdminProductionJpaService).should(times(1)).findOneProduction(adminProductionEntity);
        then(mockAdminProductionJpaService).should(atLeastOnce()).findOneProduction(adminProductionEntity);
        then(mockAdminProductionJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 프로덕션 상세 조회 Mockito 테스트")
    void 이전프로덕션상세조회Mockito테스트() throws Exception {
        // given
        adminProductionEntity = AdminProductionEntity.builder().idx(118).build();

        // when
        adminProductionDTO = adminProductionJpaService.findOneProduction(adminProductionEntity);

        when(mockAdminProductionJpaService.findPrevOneProduction(adminProductionEntity)).thenReturn(adminProductionDTO);
        AdminProductionDTO productionInfo = mockAdminProductionJpaService.findPrevOneProduction(adminProductionEntity);

        assertThat(productionInfo.getIdx()).isEqualTo(117);
        // verify
        verify(mockAdminProductionJpaService, times(1)).findPrevOneProduction(adminProductionEntity);
        verify(mockAdminProductionJpaService, atLeastOnce()).findPrevOneProduction(adminProductionEntity);
        verifyNoMoreInteractions(mockAdminProductionJpaService);

        InOrder inOrder = inOrder(mockAdminProductionJpaService);
        inOrder.verify(mockAdminProductionJpaService).findPrevOneProduction(adminProductionEntity);
    }

    @Test
    @DisplayName("이전 프로덕션 상세 조회 BDD 테스트")
    void 이전프로덕션상세조회BDD테스트() throws Exception {
        // given
        adminProductionEntity = AdminProductionEntity.builder().idx(118).build();

        // when
        adminProductionDTO = adminProductionJpaService.findOneProduction(adminProductionEntity);

        given(mockAdminProductionJpaService.findPrevOneProduction(adminProductionEntity)).willReturn(adminProductionDTO);
        AdminProductionDTO productionInfo = mockAdminProductionJpaService.findPrevOneProduction(adminProductionEntity);

        assertThat(productionInfo.getIdx()).isEqualTo(117);

        // verify
        then(mockAdminProductionJpaService).should(times(1)).findPrevOneProduction(adminProductionEntity);
        then(mockAdminProductionJpaService).should(atLeastOnce()).findPrevOneProduction(adminProductionEntity);
        then(mockAdminProductionJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 프로덕션 상세 조회 Mockito 테스트")
    void 다음프로덕션상세조회Mockito테스트() throws Exception {
        // given
        adminProductionEntity = AdminProductionEntity.builder().idx(118).build();

        // when
        adminProductionDTO = adminProductionJpaService.findOneProduction(adminProductionEntity);

        when(mockAdminProductionJpaService.findNextOneProduction(adminProductionEntity)).thenReturn(adminProductionDTO);
        AdminProductionDTO productionInfo = mockAdminProductionJpaService.findNextOneProduction(adminProductionEntity);

        assertThat(productionInfo.getIdx()).isEqualTo(119);
        // verify
        verify(mockAdminProductionJpaService, times(1)).findNextOneProduction(adminProductionEntity);
        verify(mockAdminProductionJpaService, atLeastOnce()).findNextOneProduction(adminProductionEntity);
        verifyNoMoreInteractions(mockAdminProductionJpaService);

        InOrder inOrder = inOrder(mockAdminProductionJpaService);
        inOrder.verify(mockAdminProductionJpaService).findNextOneProduction(adminProductionEntity);
    }

    @Test
    @DisplayName("다음 프로덕션 상세 조회 BDD 테스트")
    void 다음프로덕션상세조회BDD테스트() throws Exception {
        // given
        adminProductionEntity = AdminProductionEntity.builder().idx(118).build();

        // when
        adminProductionDTO = adminProductionJpaService.findOneProduction(adminProductionEntity);

        given(mockAdminProductionJpaService.findNextOneProduction(adminProductionEntity)).willReturn(adminProductionDTO);
        AdminProductionDTO productionInfo = mockAdminProductionJpaService.findNextOneProduction(adminProductionEntity);

        assertThat(productionInfo.getIdx()).isEqualTo(119);

        // verify
        then(mockAdminProductionJpaService).should(times(1)).findNextOneProduction(adminProductionEntity);
        then(mockAdminProductionJpaService).should(atLeastOnce()).findNextOneProduction(adminProductionEntity);
        then(mockAdminProductionJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("프로덕션 등록 Mockito 테스트")
    void 프로덕션등록Mockito테스트() throws Exception {
        // given
        adminProductionJpaService.insertProduction(adminProductionEntity);

        // when
        when(mockAdminProductionJpaService.findOneProduction(adminProductionEntity)).thenReturn(adminProductionDTO);
        AdminProductionDTO productionInfo = mockAdminProductionJpaService.findOneProduction(adminProductionEntity);

        // then
        assertThat(productionInfo.getTitle()).isEqualTo("프로덕션 테스트");
        assertThat(productionInfo.getDescription()).isEqualTo("프로덕션 테스트");

        // verify
        verify(mockAdminProductionJpaService, times(1)).findOneProduction(adminProductionEntity);
        verify(mockAdminProductionJpaService, atLeastOnce()).findOneProduction(adminProductionEntity);
        verifyNoMoreInteractions(mockAdminProductionJpaService);

        InOrder inOrder = inOrder(mockAdminProductionJpaService);
        inOrder.verify(mockAdminProductionJpaService).findOneProduction(adminProductionEntity);
    }

    @Test
    @DisplayName("프로덕션 등록 BDD 테스트")
    void 프로덕션등록BDD테스트() throws Exception {
        // given
        adminProductionJpaService.insertProduction(adminProductionEntity);

        // when
        given(mockAdminProductionJpaService.findOneProduction(adminProductionEntity)).willReturn(adminProductionDTO);
        AdminProductionDTO productionInfo = mockAdminProductionJpaService.findOneProduction(adminProductionEntity);

        // then
        assertThat(productionInfo.getTitle()).isEqualTo("프로덕션 테스트");
        assertThat(productionInfo.getDescription()).isEqualTo("프로덕션 테스트");

        // verify
        then(mockAdminProductionJpaService).should(times(1)).findOneProduction(adminProductionEntity);
        then(mockAdminProductionJpaService).should(atLeastOnce()).findOneProduction(adminProductionEntity);
        then(mockAdminProductionJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("프로덕션 수정 Mockito 테스트")
    void 프로덕션수정Mockito테스트() throws Exception {
        // given
        Integer idx = adminProductionJpaService.insertProduction(adminProductionEntity).getIdx();

        adminProductionEntity = AdminProductionEntity.builder()
                .idx(idx)
                .title("프로덕션 테스트1")
                .description("프로덕션 테스트1")
                .visible("Y")
                .build();

        AdminProductionDTO adminProductionDTO = INSTANCE.toDto(adminProductionEntity);

        adminProductionJpaService.updateProduction(adminProductionEntity);

        // when
        when(mockAdminProductionJpaService.findOneProduction(adminProductionEntity)).thenReturn(adminProductionDTO);
        AdminProductionDTO productionInfo = mockAdminProductionJpaService.findOneProduction(adminProductionEntity);

        // then
        assertThat(productionInfo.getTitle()).isEqualTo("프로덕션 테스트1");
        assertThat(productionInfo.getDescription()).isEqualTo("프로덕션 테스트1");

        // verify
        verify(mockAdminProductionJpaService, times(1)).findOneProduction(adminProductionEntity);
        verify(mockAdminProductionJpaService, atLeastOnce()).findOneProduction(adminProductionEntity);
        verifyNoMoreInteractions(mockAdminProductionJpaService);

        InOrder inOrder = inOrder(mockAdminProductionJpaService);
        inOrder.verify(mockAdminProductionJpaService).findOneProduction(adminProductionEntity);
    }

    @Test
    @DisplayName("프로덕션 수정 BDD 테스트")
    void 프로덕션수정BDD테스트() throws Exception {
        // given
        Integer idx = adminProductionJpaService.insertProduction(adminProductionEntity).getIdx();

        adminProductionEntity = AdminProductionEntity.builder()
                .idx(idx)
                .title("프로덕션 테스트1")
                .description("프로덕션 테스트1")
                .visible("Y")
                .build();

        AdminProductionDTO adminProductionDTO = INSTANCE.toDto(adminProductionEntity);

        adminProductionJpaService.updateProduction(adminProductionEntity);

        // when
        given(mockAdminProductionJpaService.findOneProduction(adminProductionEntity)).willReturn(adminProductionDTO);
        AdminProductionDTO productionInfo = mockAdminProductionJpaService.findOneProduction(adminProductionEntity);

        // then
        assertThat(productionInfo.getTitle()).isEqualTo("프로덕션 테스트1");
        assertThat(productionInfo.getDescription()).isEqualTo("프로덕션 테스트1");

        // verify
        then(mockAdminProductionJpaService).should(times(1)).findOneProduction(adminProductionEntity);
        then(mockAdminProductionJpaService).should(atLeastOnce()).findOneProduction(adminProductionEntity);
        then(mockAdminProductionJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("프로덕션 삭제 테스트")
    void 프로덕션삭제테스트() throws Exception {
        // given
        Integer idx = adminProductionJpaService.insertProduction(adminProductionEntity).getIdx();

        // then
        assertThat(adminProductionJpaService.deleteProduction(idx)).isNotNull();
    }

    @Test
    @DisplayName("프로덕션 어드민 코멘트 조회 Mockito 테스트")
    void 프로덕션어드민코멘트조회Mockito테스트() throws Exception {
        adminProductionEntity = AdminProductionEntity.builder()
                .title("프로덕션 테스트")
                .description("프로덕션 테스트")
                .visible("Y")
                .build();

        Integer productionIdx = adminProductionJpaService.insertProduction(adminProductionEntity).getIdx();

        adminCommentEntity = AdminCommentEntity.builder()
                .comment("코멘트 테스트")
                .commentType("production")
                .commentTypeIdx(productionIdx)
                .visible("Y")
                .build();

        List<AdminCommentDTO> adminCommentList = new ArrayList<>();
        adminCommentList.add(AdminCommentDTO.builder()
                .comment("코멘트 테스트")
                .commentType("production")
                .commentTypeIdx(productionIdx)
                .visible("Y")
                .build());

        when(mockAdminProductionJpaService.findProductionAdminComment(adminProductionEntity)).thenReturn(adminCommentList);
        List<AdminCommentDTO> newAdminCommentList = mockAdminProductionJpaService.findProductionAdminComment(adminProductionEntity);

        assertThat(newAdminCommentList.get(0).getCommentType()).isEqualTo("production");
        assertThat(newAdminCommentList.get(0).getCommentTypeIdx()).isEqualTo(adminProductionEntity.getIdx());

        // verify
        verify(mockAdminProductionJpaService, times(1)).findProductionAdminComment(adminProductionEntity);
        verify(mockAdminProductionJpaService, atLeastOnce()).findProductionAdminComment(adminProductionEntity);
        verifyNoMoreInteractions(mockAdminProductionJpaService);

        InOrder inOrder = inOrder(mockAdminProductionJpaService);
        inOrder.verify(mockAdminProductionJpaService).findProductionAdminComment(adminProductionEntity);
    }

    @Test
    @DisplayName("프로덕션 어드민 코멘트 조회 BDD 테스트")
    void 프로덕션어드민코멘트조회BDD테스트() throws Exception {
        adminProductionEntity = AdminProductionEntity.builder()
                .title("프로덕션 테스트")
                .description("프로덕션 테스트")
                .visible("Y")
                .build();

        Integer productionIdx = adminProductionJpaService.insertProduction(adminProductionEntity).getIdx();

        adminCommentEntity = AdminCommentEntity.builder()
                .comment("코멘트 테스트")
                .commentType("production")
                .commentTypeIdx(productionIdx)
                .visible("Y")
                .build();

        List<AdminCommentDTO> adminCommentList = new ArrayList<>();
        adminCommentList.add(AdminCommentDTO.builder()
                .comment("코멘트 테스트")
                .commentType("production")
                .commentTypeIdx(productionIdx)
                .visible("Y")
                .build());

        given(mockAdminProductionJpaService.findProductionAdminComment(adminProductionEntity)).willReturn(adminCommentList);
        List<AdminCommentDTO> newAdminCommentList = mockAdminProductionJpaService.findProductionAdminComment(adminProductionEntity);

        assertThat(newAdminCommentList.get(0).getCommentType()).isEqualTo("production");
        assertThat(newAdminCommentList.get(0).getCommentTypeIdx()).isEqualTo(adminProductionEntity.getIdx());

        // verify
        then(mockAdminProductionJpaService).should(times(1)).findProductionAdminComment(adminProductionEntity);
        then(mockAdminProductionJpaService).should(atLeastOnce()).findProductionAdminComment(adminProductionEntity);
        then(mockAdminProductionJpaService).shouldHaveNoMoreInteractions();
    }
}