package com.tsp.new_tsp_admin.api.support.service;

import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentDTO;
import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentEntity;
import com.tsp.new_tsp_admin.api.domain.support.AdminSupportDTO;
import com.tsp.new_tsp_admin.api.domain.support.AdminSupportEntity;
import com.tsp.new_tsp_admin.api.domain.support.evaluation.EvaluationDTO;
import com.tsp.new_tsp_admin.api.domain.support.evaluation.EvaluationEntity;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.event.EventListener;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("지원모델 Repository Test")
class AdminSupportJpaQueryRepositoryTest {
    @Mock
    AdminSupportJpaQueryRepository mockAdminSupportJpaQueryRepository;
    private final AdminSupportJpaQueryRepository adminSupportJpaQueryRepository;
    private final AdminSupportJpaRepository adminSupportJpaRepository;

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
                .supportTime(LocalDateTime.now())
                .visible("Y")
                .build();

        adminSupportDTO = AdminSupportEntity.toDto(adminSupportEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createSupport();
    }

    @Test
    @DisplayName("지원모델 리스트 조회 테스트")
    void 지원모델리스트조회테스트() {
        // given
        Map<String, Object> supportMap = new HashMap<>();
        supportMap.put("jpaStartPage", 1);
        supportMap.put("size", 3);

        // then
        assertThat(adminSupportJpaQueryRepository.findSupportList(supportMap)).isNotEmpty();
    }

    @Test
    @DisplayName("지원모델 Mockito 조회 테스트")
    void 지원모델Mockito조회테스트() {
        // given
        Map<String, Object> supportMap = new HashMap<>();
        supportMap.put("jpaStartPage", 1);
        supportMap.put("size", 3);

        List<AdminSupportDTO> supportList = new ArrayList<>();
        supportList.add(AdminSupportDTO.builder().idx(1L).supportName("조찬희").supportPhone("010-9466-2702").build());

        // when
        when(mockAdminSupportJpaQueryRepository.findSupportList(supportMap)).thenReturn(supportList);
        List<AdminSupportDTO> supportInfo = mockAdminSupportJpaQueryRepository.findSupportList(supportMap);

        // then
        assertThat(supportInfo.get(0).getIdx()).isEqualTo(supportList.get(0).getIdx());
        assertThat(supportInfo.get(0).getSupportName()).isEqualTo(supportList.get(0).getSupportName());

        // verify
        verify(mockAdminSupportJpaQueryRepository, times(1)).findSupportList(supportMap);
        verify(mockAdminSupportJpaQueryRepository, atLeastOnce()).findSupportList(supportMap);
        verifyNoMoreInteractions(mockAdminSupportJpaQueryRepository);

        InOrder inOrder = inOrder(mockAdminSupportJpaQueryRepository);
        inOrder.verify(mockAdminSupportJpaQueryRepository).findSupportList(supportMap);
    }

    @Test
    @DisplayName("지원모델 BDD 조회 테스트")
    void 지원모델BDD조회테스트() {
        // given
        Map<String, Object> supportMap = new HashMap<>();
        supportMap.put("jpaStartPage", 1);
        supportMap.put("size", 3);

        List<AdminSupportDTO> supportList = new ArrayList<>();
        supportList.add(AdminSupportDTO.builder().idx(1L).supportName("조찬희").supportPhone("010-9466-2702").build());

        // when
        given(mockAdminSupportJpaQueryRepository.findSupportList(supportMap)).willReturn(supportList);
        List<AdminSupportDTO> newSupportList = mockAdminSupportJpaQueryRepository.findSupportList(supportMap);

        // then
        assertThat(newSupportList.get(0).getIdx()).isEqualTo(supportList.get(0).getIdx());
        assertThat(newSupportList.get(0).getSupportName()).isEqualTo(supportList.get(0).getSupportName());

        // verify
        then(mockAdminSupportJpaQueryRepository).should(times(1)).findSupportList(supportMap);
        then(mockAdminSupportJpaQueryRepository).should(atLeastOnce()).findSupportList(supportMap);
        then(mockAdminSupportJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("지원 모델 평가 리스트 조회 Mockito 테스트")
    void 지원모델평가리스트조회Mockito테스트() {
        // given
        Map<String, Object> evaluationMap = new HashMap<>();
        evaluationMap.put("jpaStartPage", 1);
        evaluationMap.put("size", 3);

        List<EvaluationDTO> evaluationList = new ArrayList<>();
        evaluationList.add(EvaluationDTO.builder().idx(1L)
                .supportIdx(adminSupportEntity.getIdx()).evaluateComment("합격").visible("Y").build());

        // when
        when(mockAdminSupportJpaQueryRepository.findEvaluationList(evaluationMap)).thenReturn(evaluationList);
        List<EvaluationDTO> evaluationInfo = mockAdminSupportJpaQueryRepository.findEvaluationList(evaluationMap);

        // then
        assertThat(evaluationInfo.get(0).getIdx()).isEqualTo(evaluationInfo.get(0).getIdx());
        assertThat(evaluationInfo.get(0).getSupportIdx()).isEqualTo(evaluationInfo.get(0).getSupportIdx());
        assertThat(evaluationInfo.get(0).getEvaluateComment()).isEqualTo(evaluationInfo.get(0).getEvaluateComment());

        // verify
        verify(mockAdminSupportJpaQueryRepository, times(1)).findEvaluationList(evaluationMap);
        verify(mockAdminSupportJpaQueryRepository, atLeastOnce()).findEvaluationList(evaluationMap);
        verifyNoMoreInteractions(mockAdminSupportJpaQueryRepository);

        InOrder inOrder = inOrder(mockAdminSupportJpaQueryRepository);
        inOrder.verify(mockAdminSupportJpaQueryRepository).findEvaluationList(evaluationMap);
    }

    @Test
    @DisplayName("지원 모델 평가 리스트 조회 BDD 테스트")
    void 지원모델평가리스트조회BDD테스트() {
        // given
        Map<String, Object> evaluationMap = new HashMap<>();
        evaluationMap.put("jpaStartPage", 1);
        evaluationMap.put("size", 3);

        List<EvaluationDTO> evaluationList = new ArrayList<>();
        evaluationList.add(EvaluationDTO.builder().idx(1L)
                .supportIdx(adminSupportEntity.getIdx()).evaluateComment("합격").visible("Y").build());

        // when
        given(mockAdminSupportJpaQueryRepository.findEvaluationList(evaluationMap)).willReturn(evaluationList);
        List<EvaluationDTO> evaluationInfo = mockAdminSupportJpaQueryRepository.findEvaluationList(evaluationMap);

        // then
        assertThat(evaluationInfo.get(0).getIdx()).isEqualTo(evaluationInfo.get(0).getIdx());
        assertThat(evaluationInfo.get(0).getSupportIdx()).isEqualTo(evaluationInfo.get(0).getSupportIdx());
        assertThat(evaluationInfo.get(0).getEvaluateComment()).isEqualTo(evaluationInfo.get(0).getEvaluateComment());

        // verify
        then(mockAdminSupportJpaQueryRepository).should(times(1)).findEvaluationList(evaluationMap);
        then(mockAdminSupportJpaQueryRepository).should(atLeastOnce()).findEvaluationList(evaluationMap);
        then(mockAdminSupportJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("지원 모델 평가 상세 조회 Mockito 테스트")
    void 지원모델평가상세조회Mockito테스트() {
        // given
        evaluationEntity = EvaluationEntity.builder()
                .idx(1L).adminSupportEntity(adminSupportEntity)
                .evaluateComment("합격").visible("Y").build();

        evaluationDTO = EvaluationEntity.toDto(evaluationEntity);

        // when
        when(mockAdminSupportJpaQueryRepository.findOneEvaluation(evaluationEntity.getIdx())).thenReturn(evaluationDTO);
        EvaluationDTO evaluationInfo = mockAdminSupportJpaQueryRepository.findOneEvaluation(evaluationEntity.getIdx());

        // then
        assertThat(evaluationInfo.getIdx()).isEqualTo(1);
        assertThat(evaluationInfo.getSupportIdx()).isEqualTo(adminSupportEntity.getIdx());
        assertThat(evaluationInfo.getEvaluateComment()).isEqualTo("합격");

        // verify
        verify(mockAdminSupportJpaQueryRepository, times(1)).findOneEvaluation(evaluationEntity.getIdx());
        verify(mockAdminSupportJpaQueryRepository, atLeastOnce()).findOneEvaluation(evaluationEntity.getIdx());
        verifyNoMoreInteractions(mockAdminSupportJpaQueryRepository);

        InOrder inOrder = inOrder(mockAdminSupportJpaQueryRepository);
        inOrder.verify(mockAdminSupportJpaQueryRepository).findOneEvaluation(evaluationEntity.getIdx());
    }

    @Test
    @DisplayName("지원 모델 평가 상세 조회 BDD 테스트")
    void 지원모델평가상세조회BDD테스트() {
        // given
        evaluationEntity = EvaluationEntity.builder()
                .idx(1L).adminSupportEntity(adminSupportEntity)
                .evaluateComment("합격").visible("Y").build();

        evaluationDTO = EvaluationEntity.toDto(evaluationEntity);

        // when
        given(mockAdminSupportJpaQueryRepository.findOneEvaluation(evaluationEntity.getIdx())).willReturn(evaluationDTO);
        EvaluationDTO evaluationInfo = mockAdminSupportJpaQueryRepository.findOneEvaluation(evaluationEntity.getIdx());

        // then
        assertThat(evaluationInfo.getIdx()).isEqualTo(1);
        assertThat(evaluationInfo.getSupportIdx()).isEqualTo(adminSupportEntity.getIdx());
        assertThat(evaluationInfo.getEvaluateComment()).isEqualTo("합격");

        // verify
        then(mockAdminSupportJpaQueryRepository).should(times(1)).findOneEvaluation(evaluationEntity.getIdx());
        then(mockAdminSupportJpaQueryRepository).should(atLeastOnce()).findOneEvaluation(evaluationEntity.getIdx());
        then(mockAdminSupportJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("지원모델 어드민 코멘트 조회 Mockito 테스트")
    void 지원모델어드민코멘트조회Mockito테스트() {
        adminSupportEntity = AdminSupportEntity.builder()
                .supportName("조찬희")
                .supportHeight(170)
                .supportMessage("조찬희")
                .supportPhone("010-9466-2702")
                .supportSize3("31-24-31")
                .supportTime(LocalDateTime.now())
                .passYn("Y")
                .passTime(now())
                .visible("Y")
                .build();

        Long supportIdx = adminSupportJpaRepository.save(adminSupportEntity).getIdx();

        adminCommentEntity = AdminCommentEntity.builder()
                .comment("코멘트 테스트")
                .commentType("support")
                .visible("Y")
                .build();

        List<AdminCommentDTO> adminCommentList = new ArrayList<>();
        adminCommentList.add(AdminCommentDTO.builder()
                .comment("코멘트 테스트")
                .commentType("support")
                .commentTypeIdx(supportIdx)
                .visible("Y")
                .build());

        when(mockAdminSupportJpaQueryRepository.findSupportAdminComment(adminSupportEntity.getIdx())).thenReturn(adminCommentList);
        List<AdminCommentDTO> newAdminCommentList = mockAdminSupportJpaQueryRepository.findSupportAdminComment(adminSupportEntity.getIdx());

        assertThat(newAdminCommentList.get(0).getCommentType()).isEqualTo("support");
        assertThat(newAdminCommentList.get(0).getCommentTypeIdx()).isEqualTo(adminSupportEntity.getIdx());

        verify(mockAdminSupportJpaQueryRepository, times(1)).findSupportAdminComment(adminSupportEntity.getIdx());
        verify(mockAdminSupportJpaQueryRepository, atLeastOnce()).findSupportAdminComment(adminSupportEntity.getIdx());
        verifyNoMoreInteractions(mockAdminSupportJpaQueryRepository);

        InOrder inOrder = inOrder(mockAdminSupportJpaQueryRepository);
        inOrder.verify(mockAdminSupportJpaQueryRepository).findSupportAdminComment(adminSupportEntity.getIdx());
    }

    @Test
    @DisplayName("지원모델 어드민 코멘트 조회 BDD 테스트")
    void 지원모델어드민코멘트조회BDD테스트() {
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

        Long supportIdx = adminSupportJpaRepository.save(adminSupportEntity).getIdx();

        adminCommentEntity = AdminCommentEntity.builder()
                .comment("코멘트 테스트")
                .commentType("support")
                .visible("Y")
                .build();

        List<AdminCommentDTO> adminCommentList = new ArrayList<>();
        adminCommentList.add(AdminCommentDTO.builder()
                .comment("코멘트 테스트")
                .commentType("support")
                .commentTypeIdx(supportIdx)
                .visible("Y")
                .build());

        given(mockAdminSupportJpaQueryRepository.findSupportAdminComment(adminSupportEntity.getIdx())).willReturn(adminCommentList);
        List<AdminCommentDTO> newAdminCommentList = mockAdminSupportJpaQueryRepository.findSupportAdminComment(adminSupportEntity.getIdx());

        assertThat(newAdminCommentList.get(0).getCommentType()).isEqualTo("support");
        assertThat(newAdminCommentList.get(0).getCommentTypeIdx()).isEqualTo(adminSupportEntity.getIdx());

        // verify
        then(mockAdminSupportJpaQueryRepository).should(times(1)).findSupportAdminComment(adminSupportEntity.getIdx());
        then(mockAdminSupportJpaQueryRepository).should(atLeastOnce()).findSupportAdminComment(adminSupportEntity.getIdx());
        then(mockAdminSupportJpaQueryRepository).shouldHaveNoMoreInteractions();
    }
}