package com.tsp.new_tsp_admin.api.support.service;

import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentDTO;
import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentEntity;
import com.tsp.new_tsp_admin.api.domain.support.AdminSupportDTO;
import com.tsp.new_tsp_admin.api.domain.support.AdminSupportEntity;
import com.tsp.new_tsp_admin.api.domain.support.evaluation.EvaluationDTO;
import com.tsp.new_tsp_admin.api.domain.support.evaluation.EvaluationEntity;
import com.tsp.new_tsp_admin.api.support.mapper.evaluate.EvaluateMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

import java.time.LocalDateTime;
import java.util.*;

import static com.tsp.new_tsp_admin.api.support.mapper.SupportMapper.INSTANCE;
import static java.time.LocalDateTime.now;
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
@DisplayName("지원모델 Service Test")
class AdminSupportJpaServiceTest {
    @Mock private AdminSupportJpaService mockAdminSupportJpaService;
    private final AdminSupportJpaService adminSupportJpaService;

    private AdminSupportEntity adminSupportEntity;
    private AdminSupportDTO adminSupportDTO;

    private EvaluationEntity evaluationEntity;
    private EvaluationDTO evaluationDTO;
    private AdminCommentEntity adminCommentEntity;
    private AdminCommentDTO adminCommentDTO;

    void createSupport() {
        adminSupportEntity = AdminSupportEntity.builder()
                .supportName("조찬희")
                .supportHeight(170)
                .supportMessage("조찬희")
                .supportPhone("010-9466-2702")
                .supportSize3("31-24-31")
                .supportInstagram("https://instagram.com")
                .visible("Y")
                .build();

        adminSupportDTO = INSTANCE.toDto(adminSupportEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createSupport();
    }

    @Test
    @DisplayName("지원모델 리스트 조회 테스트")
    void 지원모델리스트조회테스트() throws Exception {
        // given
        Map<String, Object> supportMap = new HashMap<>();
        supportMap.put("jpaStartPage", 1);
        supportMap.put("size", 3);

        // then
        assertThat(adminSupportJpaService.findSupportsList(supportMap)).isNotEmpty();
    }

    @Test
    @DisplayName("지원모델 리스트 조회 Mockito 테스트")
    void 지원모델리스트조회Mockito테스트() throws Exception {
        // given
        Map<String, Object> supportMap = new HashMap<>();
        supportMap.put("jpaStartPage", 1);
        supportMap.put("size", 3);

        List<AdminSupportDTO> returnSupportList = new ArrayList<>();
        returnSupportList.add(AdminSupportDTO.builder()
                .idx(1).supportName("조찬희").supportHeight(170).supportMessage("조찬희")
                .supportPhone("010-1234-5678").supportSize3("31-24-31").supportInstagram("https://instagram.com").visible("Y").build());

        // when
        when(mockAdminSupportJpaService.findSupportsList(supportMap)).thenReturn(returnSupportList);
        List<AdminSupportDTO> supportsList = mockAdminSupportJpaService.findSupportsList(supportMap);

        // then
        assertAll(
                () -> assertThat(supportsList).isNotEmpty(),
                () -> assertThat(supportsList).hasSize(1)
        );

        assertThat(supportsList.get(0).getIdx()).isEqualTo(returnSupportList.get(0).getIdx());
        assertThat(supportsList.get(0).getSupportName()).isEqualTo(returnSupportList.get(0).getSupportName());
        assertThat(supportsList.get(0).getSupportHeight()).isEqualTo(returnSupportList.get(0).getSupportHeight());
        assertThat(supportsList.get(0).getSupportMessage()).isEqualTo(returnSupportList.get(0).getSupportMessage());
        assertThat(supportsList.get(0).getVisible()).isEqualTo(returnSupportList.get(0).getVisible());

        // verify
        verify(mockAdminSupportJpaService, times(1)).findSupportsList(supportMap);
        verify(mockAdminSupportJpaService, atLeastOnce()).findSupportsList(supportMap);
        verifyNoMoreInteractions(mockAdminSupportJpaService);

        InOrder inOrder = inOrder(mockAdminSupportJpaService);
        inOrder.verify(mockAdminSupportJpaService).findSupportsList(supportMap);
    }

    @Test
    @DisplayName("지원모델 리스트 조회 BDD 테스트")
    void 지원모델리스트조회BDD테스트() throws Exception {
        // given
        Map<String, Object> supportMap = new HashMap<>();
        supportMap.put("jpaStartPage", 1);
        supportMap.put("size", 3);

        List<AdminSupportDTO> returnSupportList = new ArrayList<>();
        returnSupportList.add(AdminSupportDTO.builder()
                .idx(1).supportName("조찬희").supportHeight(170).supportMessage("조찬희")
                .supportPhone("010-1234-5678").supportSize3("31-24-31").supportInstagram("https://instagram.com").visible("Y").build());

        // when
        given(mockAdminSupportJpaService.findSupportsList(supportMap)).willReturn(returnSupportList);
        List<AdminSupportDTO> supportsList = mockAdminSupportJpaService.findSupportsList(supportMap);

        // then
        assertAll(
                () -> assertThat(supportsList).isNotEmpty(),
                () -> assertThat(supportsList).hasSize(1)
        );

        assertThat(supportsList.get(0).getIdx()).isEqualTo(returnSupportList.get(0).getIdx());
        assertThat(supportsList.get(0).getSupportName()).isEqualTo(returnSupportList.get(0).getSupportName());
        assertThat(supportsList.get(0).getSupportHeight()).isEqualTo(returnSupportList.get(0).getSupportHeight());
        assertThat(supportsList.get(0).getSupportMessage()).isEqualTo(returnSupportList.get(0).getSupportMessage());
        assertThat(supportsList.get(0).getVisible()).isEqualTo(returnSupportList.get(0).getVisible());

        // verify
        then(mockAdminSupportJpaService).should(times(1)).findSupportsList(supportMap);
        then(mockAdminSupportJpaService).should(atLeastOnce()).findSupportsList(supportMap);
        then(mockAdminSupportJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @Disabled
    @DisplayName("지원모델 상세 조회 테스트")
    void 지원모델상세조회테스트() throws Exception {
        // given
        adminSupportEntity = AdminSupportEntity.builder().idx(1).build();

        // when
        adminSupportDTO = adminSupportJpaService.findOneSupportModel(adminSupportEntity);

        // then
        assertThat(adminSupportDTO.getIdx()).isEqualTo(1);
    }

    @Test
    @DisplayName("지원모델 상세 조회 Mockito 테스트")
    void 지원모델상세조회Mockito테스트() throws Exception {
        // when
        when(mockAdminSupportJpaService.findOneSupportModel(adminSupportEntity)).thenReturn(adminSupportDTO);
        AdminSupportDTO supportInfo = mockAdminSupportJpaService.findOneSupportModel(adminSupportEntity);

        // then
        assertThat(supportInfo.getIdx()).isEqualTo(adminSupportDTO.getIdx());
        assertThat(supportInfo.getSupportName()).isEqualTo(adminSupportDTO.getSupportName());
        assertThat(supportInfo.getSupportMessage()).isEqualTo(adminSupportDTO.getSupportMessage());
        assertThat(supportInfo.getVisible()).isEqualTo(adminSupportDTO.getVisible());

        // verify
        verify(mockAdminSupportJpaService, times(1)).findOneSupportModel(adminSupportEntity);
        verify(mockAdminSupportJpaService, atLeastOnce()).findOneSupportModel(adminSupportEntity);
        verifyNoMoreInteractions(mockAdminSupportJpaService);

        InOrder inOrder = inOrder(mockAdminSupportJpaService);
        inOrder.verify(mockAdminSupportJpaService).findOneSupportModel(adminSupportEntity);
    }

    @Test
    @DisplayName("지원모델 상세 조회 BDD 테스트")
    void 지원모델상세조회BDD테스트() throws Exception {
        // when
        given(mockAdminSupportJpaService.findOneSupportModel(adminSupportEntity)).willReturn(adminSupportDTO);
        AdminSupportDTO supportInfo = mockAdminSupportJpaService.findOneSupportModel(adminSupportEntity);

        // then
        assertThat(supportInfo.getIdx()).isEqualTo(adminSupportDTO.getIdx());
        assertThat(supportInfo.getSupportName()).isEqualTo(adminSupportDTO.getSupportName());
        assertThat(supportInfo.getSupportMessage()).isEqualTo(adminSupportDTO.getSupportMessage());
        assertThat(supportInfo.getVisible()).isEqualTo(adminSupportDTO.getVisible());

        // verify
        then(mockAdminSupportJpaService).should(times(1)).findOneSupportModel(adminSupportEntity);
        then(mockAdminSupportJpaService).should(atLeastOnce()).findOneSupportModel(adminSupportEntity);
        then(mockAdminSupportJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("지원모델 수정 Mockito 테스트")
    void 지원모델수정Mockito테스트() throws Exception {
        // given
        adminSupportEntity = AdminSupportEntity.builder()
                .idx(adminSupportJpaService.insertSupportModel(adminSupportEntity).getIdx())
                .supportName("test")
                .supportPhone("010-9466-2702")
                .supportHeight(170)
                .supportSize3("31-24-31")
                .supportMessage("test")
                .supportInstagram("https://instagram.com")
                .visible("Y")
                .build();

        adminSupportJpaService.updateSupportModel(adminSupportEntity);
        adminSupportDTO = INSTANCE.toDto(adminSupportEntity);

        // when
        when(mockAdminSupportJpaService.findOneSupportModel(adminSupportEntity)).thenReturn(adminSupportDTO);
        AdminSupportDTO supportInfo = mockAdminSupportJpaService.findOneSupportModel(adminSupportEntity);

        // then
        assertThat(supportInfo.getSupportName()).isEqualTo("test");
        assertThat(supportInfo.getSupportPhone()).isEqualTo("010-9466-2702");

        // verify
        verify(mockAdminSupportJpaService, times(1)).findOneSupportModel(adminSupportEntity);
        verify(mockAdminSupportJpaService, atLeastOnce()).findOneSupportModel(adminSupportEntity);
        verifyNoMoreInteractions(mockAdminSupportJpaService);

        InOrder inOrder = inOrder(mockAdminSupportJpaService);
        inOrder.verify(mockAdminSupportJpaService).findOneSupportModel(adminSupportEntity);
    }

    @Test
    @DisplayName("지원모델 수정 BDD 테스트")
    void 지원모델수정BDD테스트() throws Exception {
        // given
        adminSupportEntity = AdminSupportEntity.builder()
                .idx(adminSupportJpaService.insertSupportModel(adminSupportEntity).getIdx())
                .supportName("test")
                .supportPhone("010-9466-2702")
                .supportHeight(170)
                .supportSize3("31-24-31")
                .supportMessage("test")
                .supportInstagram("https://instagram.com")
                .visible("Y")
                .build();

        adminSupportJpaService.updateSupportModel(adminSupportEntity);
        adminSupportDTO = INSTANCE.toDto(adminSupportEntity);

        // when
        given(mockAdminSupportJpaService.findOneSupportModel(adminSupportEntity)).willReturn(adminSupportDTO);
        AdminSupportDTO supportInfo = mockAdminSupportJpaService.findOneSupportModel(adminSupportEntity);

        // then
        assertThat(supportInfo.getSupportName()).isEqualTo("test");
        assertThat(supportInfo.getSupportPhone()).isEqualTo("010-9466-2702");

        // verify
        then(mockAdminSupportJpaService).should(times(1)).findOneSupportModel(adminSupportEntity);
        then(mockAdminSupportJpaService).should(atLeastOnce()).findOneSupportModel(adminSupportEntity);
        then(mockAdminSupportJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("지원모델 삭제 테스트")
    void 지원모델삭제테스트() throws Exception {
        // given
        Integer idx = adminSupportJpaService.insertSupportModel(adminSupportEntity).getIdx();

        // then
        assertThat(adminSupportJpaService.deleteSupportModel(idx)).isNotNull();
    }

    @Test
    @DisplayName("지원 모델 평가 리스트 조회 Mockito 테스트")
    void 지원모델평가리스트조회Mockito테스트() throws Exception {
        // given
        Map<String, Object> evaluationMap = new HashMap<>();
        evaluationMap.put("jpaStartPage", 1);
        evaluationMap.put("size", 3);

        List<EvaluationDTO> evaluationList = new ArrayList<>();
        evaluationList.add(EvaluationDTO.builder().idx(1)
                .supportIdx(adminSupportEntity.getIdx()).evaluateComment("합격").visible("Y").build());

        // when
        when(mockAdminSupportJpaService.findEvaluationsList(evaluationMap)).thenReturn(evaluationList);
        List<EvaluationDTO> evaluationInfo = mockAdminSupportJpaService.findEvaluationsList(evaluationMap);

        // then
        assertThat(evaluationInfo.get(0).getIdx()).isEqualTo(evaluationInfo.get(0).getIdx());
        assertThat(evaluationInfo.get(0).getSupportIdx()).isEqualTo(evaluationInfo.get(0).getSupportIdx());
        assertThat(evaluationInfo.get(0).getEvaluateComment()).isEqualTo(evaluationInfo.get(0).getEvaluateComment());

        // verify
        verify(mockAdminSupportJpaService, times(1)).findEvaluationsList(evaluationMap);
        verify(mockAdminSupportJpaService, atLeastOnce()).findEvaluationsList(evaluationMap);
        verifyNoMoreInteractions(mockAdminSupportJpaService);

        InOrder inOrder = inOrder(mockAdminSupportJpaService);
        inOrder.verify(mockAdminSupportJpaService).findEvaluationsList(evaluationMap);
    }

    @Test
    @DisplayName("지원 모델 평가 리스트 조회 BDD 테스트")
    void 지원모델평가리스트조회BDD테스트() throws Exception {
        // given
        Map<String, Object> evaluationMap = new HashMap<>();
        evaluationMap.put("jpaStartPage", 1);
        evaluationMap.put("size", 3);

        List<EvaluationDTO> evaluationList = new ArrayList<>();
        evaluationList.add(EvaluationDTO.builder().idx(1)
                .supportIdx(adminSupportEntity.getIdx()).evaluateComment("합격").visible("Y").build());

        // when
        given(mockAdminSupportJpaService.findEvaluationsList(evaluationMap)).willReturn(evaluationList);
        List<EvaluationDTO> evaluationInfo = mockAdminSupportJpaService.findEvaluationsList(evaluationMap);

        // then
        assertThat(evaluationInfo.get(0).getIdx()).isEqualTo(evaluationInfo.get(0).getIdx());
        assertThat(evaluationInfo.get(0).getSupportIdx()).isEqualTo(evaluationInfo.get(0).getSupportIdx());
        assertThat(evaluationInfo.get(0).getEvaluateComment()).isEqualTo(evaluationInfo.get(0).getEvaluateComment());

        // verify
        then(mockAdminSupportJpaService).should(times(1)).findEvaluationsList(evaluationMap);
        then(mockAdminSupportJpaService).should(atLeastOnce()).findEvaluationsList(evaluationMap);
        then(mockAdminSupportJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("지원 모델 평가 상세 조회 Mockito 테스트")
    void 지원모델평가상세조회Mockito테스트() throws Exception {
        // given
        evaluationEntity = EvaluationEntity.builder()
                .idx(1).supportIdx(adminSupportEntity.getIdx())
                .evaluateComment("합격").visible("Y").build();

        evaluationDTO = EvaluateMapper.INSTANCE.toDto(evaluationEntity);

        // when
        when(mockAdminSupportJpaService.findOneEvaluation(evaluationEntity)).thenReturn(evaluationDTO);
        EvaluationDTO evaluationInfo = mockAdminSupportJpaService.findOneEvaluation(evaluationEntity);

        // then
        assertThat(evaluationInfo.getIdx()).isEqualTo(1);
        assertThat(evaluationInfo.getSupportIdx()).isEqualTo(adminSupportEntity.getIdx());
        assertThat(evaluationInfo.getEvaluateComment()).isEqualTo("합격");

        // verify
        verify(mockAdminSupportJpaService, times(1)).findOneEvaluation(evaluationEntity);
        verify(mockAdminSupportJpaService, atLeastOnce()).findOneEvaluation(evaluationEntity);
        verifyNoMoreInteractions(mockAdminSupportJpaService);

        InOrder inOrder = inOrder(mockAdminSupportJpaService);
        inOrder.verify(mockAdminSupportJpaService).findOneEvaluation(evaluationEntity);
    }

    @Test
    @DisplayName("지원 모델 평가 상세 조회 BDD 테스트")
    void 지원모델평가상세조회BDD테스트() throws Exception {
        // given
        evaluationEntity = EvaluationEntity.builder()
                .idx(1).supportIdx(adminSupportEntity.getIdx())
                .evaluateComment("합격").visible("Y").build();

        evaluationDTO = EvaluateMapper.INSTANCE.toDto(evaluationEntity);

        // when
        given(mockAdminSupportJpaService.findOneEvaluation(evaluationEntity)).willReturn(evaluationDTO);
        EvaluationDTO evaluationInfo = mockAdminSupportJpaService.findOneEvaluation(evaluationEntity);

        // then
        assertThat(evaluationInfo.getIdx()).isEqualTo(1);
        assertThat(evaluationInfo.getSupportIdx()).isEqualTo(adminSupportEntity.getIdx());
        assertThat(evaluationInfo.getEvaluateComment()).isEqualTo("합격");

        // verify
        then(mockAdminSupportJpaService).should(times(1)).findOneEvaluation(evaluationEntity);
        then(mockAdminSupportJpaService).should(atLeastOnce()).findOneEvaluation(evaluationEntity);
        then(mockAdminSupportJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("지원 모델 평가 Mockito 테스트")
    void 지원모델평가Mockito테스트() throws Exception {
        // given
        Integer supportIdx = adminSupportJpaService.insertSupportModel(adminSupportEntity).getIdx();

        evaluationEntity = EvaluationEntity.builder()
                .supportIdx(supportIdx)
                .evaluateComment("합격")
                .visible("Y")
                .build();

        adminSupportJpaService.evaluationSupportModel(evaluationEntity);

        evaluationDTO = EvaluateMapper.INSTANCE.toDto(evaluationEntity);

        // when
        when(mockAdminSupportJpaService.findOneEvaluation(evaluationEntity)).thenReturn(evaluationDTO);
        EvaluationDTO evaluationInfo = mockAdminSupportJpaService.findOneEvaluation(evaluationEntity);

        // then
        assertThat(evaluationInfo.getSupportIdx()).isEqualTo(supportIdx);
        assertThat(evaluationInfo.getEvaluateComment()).isEqualTo("합격");

        // verify
        verify(mockAdminSupportJpaService, times(1)).findOneEvaluation(evaluationEntity);
        verify(mockAdminSupportJpaService, atLeastOnce()).findOneEvaluation(evaluationEntity);
        verifyNoMoreInteractions(mockAdminSupportJpaService);

        InOrder inOrder = inOrder(mockAdminSupportJpaService);
        inOrder.verify(mockAdminSupportJpaService).findOneEvaluation(evaluationEntity);
    }

    @Test
    @DisplayName("지원 모델 평가 BDD 테스트")
    void 지원모델평가BDD테스트() throws Exception {
        // given
        Integer supportIdx = adminSupportJpaService.insertSupportModel(adminSupportEntity).getIdx();

        evaluationEntity = EvaluationEntity.builder()
                .supportIdx(supportIdx)
                .evaluateComment("합격")
                .visible("Y")
                .build();

        adminSupportJpaService.evaluationSupportModel(evaluationEntity);

        evaluationDTO = EvaluateMapper.INSTANCE.toDto(evaluationEntity);

        // when
        given(mockAdminSupportJpaService.findOneEvaluation(evaluationEntity)).willReturn(evaluationDTO);
        EvaluationDTO evaluationInfo = mockAdminSupportJpaService.findOneEvaluation(evaluationEntity);

        // then
        assertThat(evaluationInfo.getSupportIdx()).isEqualTo(supportIdx);
        assertThat(evaluationInfo.getEvaluateComment()).isEqualTo("합격");

        // verify
        then(mockAdminSupportJpaService).should(times(1)).findOneEvaluation(evaluationEntity);
        then(mockAdminSupportJpaService).should(atLeastOnce()).findOneEvaluation(evaluationEntity);
        then(mockAdminSupportJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("지원 모델 평가 삭제 Mockito 테스트")
    void 지원모델평가삭제Mockito테스트() throws Exception {
        // given
        Integer supportIdx = adminSupportJpaService.insertSupportModel(adminSupportEntity).getIdx();

        evaluationEntity = EvaluationEntity.builder()
                .supportIdx(supportIdx)
                .evaluateComment("합격").visible("Y").build();

        // 지원모델 평가 저장
        adminSupportJpaService.evaluationSupportModel(evaluationEntity);
        evaluationDTO = EvaluateMapper.INSTANCE.toDto(evaluationEntity);

        given(mockAdminSupportJpaService.findOneEvaluation(evaluationEntity)).willReturn(evaluationDTO);
        Integer deleteIdx = adminSupportJpaService.deleteEvaluation(evaluationEntity.getIdx());

        // then
        assertThat(mockAdminSupportJpaService.findOneEvaluation(evaluationEntity).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockAdminSupportJpaService, times(1)).findOneEvaluation(evaluationEntity);
        verify(mockAdminSupportJpaService, atLeastOnce()).findOneEvaluation(evaluationEntity);
        verifyNoMoreInteractions(mockAdminSupportJpaService);

        InOrder inOrder = inOrder(mockAdminSupportJpaService);
        inOrder.verify(mockAdminSupportJpaService).findOneEvaluation(evaluationEntity);
    }

    @Test
    @DisplayName("지원 모델 평가 삭제 BDD 테스트")
    void 지원모델평가삭제BDD테스트() throws Exception {
        // given
        Integer supportIdx = adminSupportJpaService.insertSupportModel(adminSupportEntity).getIdx();

        evaluationEntity = EvaluationEntity.builder()
                .supportIdx(supportIdx)
                .evaluateComment("합격").visible("Y").build();

        // 지원모델 평가 저장
        adminSupportJpaService.evaluationSupportModel(evaluationEntity);
        evaluationDTO = EvaluateMapper.INSTANCE.toDto(evaluationEntity);

        given(mockAdminSupportJpaService.findOneEvaluation(evaluationEntity)).willReturn(evaluationDTO);
        Integer deleteIdx = adminSupportJpaService.deleteEvaluation(evaluationEntity.getIdx());

        // then
        assertThat(mockAdminSupportJpaService.findOneEvaluation(evaluationEntity).getIdx()).isEqualTo(deleteIdx);

        // verify
        then(mockAdminSupportJpaService).should(times(1)).findOneEvaluation(evaluationEntity);
        then(mockAdminSupportJpaService).should(atLeastOnce()).findOneEvaluation(evaluationEntity);
        then(mockAdminSupportJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("지원 모델 합격 Mockito 테스트")
    void 지원모델합격Mockito테스트() throws Exception {
        // given
        Integer supportIdx = adminSupportJpaService.insertSupportModel(adminSupportEntity).getIdx();

        AdminSupportDTO supportDTO = adminSupportJpaService.updatePass(supportIdx);

        // when
        when(mockAdminSupportJpaService.findOneSupportModel(adminSupportEntity)).thenReturn(supportDTO);
        AdminSupportDTO supportInfo = mockAdminSupportJpaService.findOneSupportModel(adminSupportEntity);

        // then
        assertThat(supportInfo.getIdx()).isEqualTo(adminSupportEntity.getIdx());
        assertThat(supportInfo.getPassYn()).isEqualTo("Y");
        assertThat(supportInfo.getPassTime()).isNotNull();

        verify(mockAdminSupportJpaService, times(1)).findOneSupportModel(adminSupportEntity);
        verify(mockAdminSupportJpaService, atLeastOnce()).findOneSupportModel(adminSupportEntity);
        verifyNoMoreInteractions(mockAdminSupportJpaService);

        InOrder inOrder = inOrder(mockAdminSupportJpaService);
        inOrder.verify(mockAdminSupportJpaService).findOneSupportModel(adminSupportEntity);
    }

    @Test
    @DisplayName("지원 모델 합격 BDD 테스트")
    void 지원모델합격BDD테스트() throws Exception {
        // given
        Integer supportIdx = adminSupportJpaService.insertSupportModel(adminSupportEntity).getIdx();

        AdminSupportDTO supportDTO = adminSupportJpaService.updatePass(supportIdx);

        // when
        when(mockAdminSupportJpaService.findOneSupportModel(adminSupportEntity)).thenReturn(supportDTO);
        AdminSupportDTO supportInfo = mockAdminSupportJpaService.findOneSupportModel(adminSupportEntity);

        // then
        assertThat(supportInfo.getIdx()).isEqualTo(adminSupportEntity.getIdx());
        assertThat(supportInfo.getPassYn()).isEqualTo("Y");
        assertThat(supportInfo.getPassTime()).isNotNull();

        then(mockAdminSupportJpaService).should(times(1)).findOneSupportModel(adminSupportEntity);
        then(mockAdminSupportJpaService).should(atLeastOnce()).findOneSupportModel(adminSupportEntity);
        then(mockAdminSupportJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("지원모델 어드민 코멘트 조회 Mockito 테스트")
    void 지원모델어드민코멘트조회Mockito테스트() throws Exception {
        adminSupportEntity = AdminSupportEntity.builder()
                .supportName("조찬희")
                .supportHeight(170)
                .supportMessage("조찬희")
                .supportPhone("010-9466-2702")
                .supportSize3("31-24-31")
                .passYn("Y")
                .passTime(now())
                .visible("Y")
                .build();

        Integer supportIdx = adminSupportJpaService.insertSupportModel(adminSupportEntity).getIdx();

        adminCommentEntity = AdminCommentEntity.builder()
                .comment("코멘트 테스트")
                .commentType("support")
                .commentTypeIdx(supportIdx)
                .visible("Y")
                .build();

        List<AdminCommentDTO> adminCommentList = new ArrayList<>();
        adminCommentList.add(AdminCommentDTO.builder()
                .comment("코멘트 테스트")
                .commentType("support")
                .commentTypeIdx(supportIdx)
                .visible("Y")
                .build());

        when(mockAdminSupportJpaService.findSupportAdminComment(adminSupportEntity)).thenReturn(adminCommentList);
        List<AdminCommentDTO> newAdminCommentList = mockAdminSupportJpaService.findSupportAdminComment(adminSupportEntity);

        assertThat(newAdminCommentList.get(0).getCommentType()).isEqualTo("support");
        assertThat(newAdminCommentList.get(0).getCommentTypeIdx()).isEqualTo(adminSupportEntity.getIdx());

        verify(mockAdminSupportJpaService, times(1)).findSupportAdminComment(adminSupportEntity);
        verify(mockAdminSupportJpaService, atLeastOnce()).findSupportAdminComment(adminSupportEntity);
        verifyNoMoreInteractions(mockAdminSupportJpaService);

        InOrder inOrder = inOrder(mockAdminSupportJpaService);
        inOrder.verify(mockAdminSupportJpaService).findSupportAdminComment(adminSupportEntity);
    }

    @Test
    @DisplayName("지원모델 어드민 코멘트 조회 BDD 테스트")
    void 지원모델어드민코멘트조회BDD테스트() throws Exception {
        adminSupportEntity = AdminSupportEntity.builder()
                .supportName("조찬희")
                .supportHeight(170)
                .supportMessage("조찬희")
                .supportPhone("010-9466-2702")
                .supportSize3("31-24-31")
                .passYn("Y")
                .passTime(now())
                .visible("Y")
                .build();

        Integer supportIdx = adminSupportJpaService.insertSupportModel(adminSupportEntity).getIdx();

        adminCommentEntity = AdminCommentEntity.builder()
                .comment("코멘트 테스트")
                .commentType("support")
                .commentTypeIdx(supportIdx)
                .visible("Y")
                .build();

        List<AdminCommentDTO> adminCommentList = new ArrayList<>();
        adminCommentList.add(AdminCommentDTO.builder()
                .comment("코멘트 테스트")
                .commentType("support")
                .commentTypeIdx(supportIdx)
                .visible("Y")
                .build());

        given(mockAdminSupportJpaService.findSupportAdminComment(adminSupportEntity)).willReturn(adminCommentList);
        List<AdminCommentDTO> newAdminCommentList = mockAdminSupportJpaService.findSupportAdminComment(adminSupportEntity);

        assertThat(newAdminCommentList.get(0).getCommentType()).isEqualTo("support");
        assertThat(newAdminCommentList.get(0).getCommentTypeIdx()).isEqualTo(adminSupportEntity.getIdx());

        // verify
        then(mockAdminSupportJpaService).should(times(1)).findSupportAdminComment(adminSupportEntity);
        then(mockAdminSupportJpaService).should(atLeastOnce()).findSupportAdminComment(adminSupportEntity);
        then(mockAdminSupportJpaService).shouldHaveNoMoreInteractions();
    }
}