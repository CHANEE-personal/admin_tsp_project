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
class AdminSupportJpaRepositoryTest {
    @Mock AdminSupportJpaRepository mockAdminSupportJpaRepository;
    private final AdminSupportJpaRepository adminSupportJpaRepository;
    private final EntityManager em;

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
        assertThat(adminSupportJpaRepository.findSupportList(supportMap)).isNotEmpty();
    }

    @Test
    @Disabled
    @DisplayName("지원모델 상세 조회 테스트")
    void 지원모델상세조회테스트() {
        // given
        adminSupportEntity = AdminSupportEntity.builder().idx(1L).build();

        // when
        adminSupportDTO = adminSupportJpaRepository.findOneSupportModel(adminSupportEntity.getIdx());

        // then
        assertThat(adminSupportDTO.getIdx()).isEqualTo(1);
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
        when(mockAdminSupportJpaRepository.findSupportList(supportMap)).thenReturn(supportList);
        List<AdminSupportDTO> supportInfo = mockAdminSupportJpaRepository.findSupportList(supportMap);

        // then
        assertThat(supportInfo.get(0).getIdx()).isEqualTo(supportList.get(0).getIdx());
        assertThat(supportInfo.get(0).getSupportName()).isEqualTo(supportList.get(0).getSupportName());

        // verify
        verify(mockAdminSupportJpaRepository, times(1)).findSupportList(supportMap);
        verify(mockAdminSupportJpaRepository, atLeastOnce()).findSupportList(supportMap);
        verifyNoMoreInteractions(mockAdminSupportJpaRepository);

        InOrder inOrder = inOrder(mockAdminSupportJpaRepository);
        inOrder.verify(mockAdminSupportJpaRepository).findSupportList(supportMap);
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
        given(mockAdminSupportJpaRepository.findSupportList(supportMap)).willReturn(supportList);
        List<AdminSupportDTO> newSupportList = mockAdminSupportJpaRepository.findSupportList(supportMap);

        // then
        assertThat(newSupportList.get(0).getIdx()).isEqualTo(supportList.get(0).getIdx());
        assertThat(newSupportList.get(0).getSupportName()).isEqualTo(supportList.get(0).getSupportName());

        // verify
        then(mockAdminSupportJpaRepository).should(times(1)).findSupportList(supportMap);
        then(mockAdminSupportJpaRepository).should(atLeastOnce()).findSupportList(supportMap);
        then(mockAdminSupportJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("지원모델 수정 Mockito 테스트")
    void 지원모델수정Mockito테스트() {
        // given
        em.persist(adminSupportEntity);
        Long idx = em.find(AdminSupportEntity.class, this.adminSupportEntity.getIdx()).getIdx();

        adminSupportEntity = AdminSupportEntity.builder()
                .idx(idx)
                .supportName("test")
                .supportPhone("010-9466-2702")
                .supportHeight(170)
                .supportSize3("31-24-31")
                .supportMessage("test")
                .visible("Y")
                .supportInstagram("https://instagram.com")
                .build();

        adminSupportJpaRepository.updateSupportModel(adminSupportEntity);

        adminSupportDTO = AdminSupportEntity.toDto(adminSupportEntity);

        // when
        when(mockAdminSupportJpaRepository.findOneSupportModel(adminSupportEntity.getIdx())).thenReturn(adminSupportDTO);
        AdminSupportDTO supportInfo = mockAdminSupportJpaRepository.findOneSupportModel(adminSupportEntity.getIdx());

        // then
        assertThat(supportInfo.getSupportName()).isEqualTo("test");
        assertThat(supportInfo.getSupportMessage()).isEqualTo("test");
        assertThat(supportInfo.getSupportHeight()).isEqualTo(170);

        // verify
        verify(mockAdminSupportJpaRepository, times(1)).findOneSupportModel(adminSupportEntity.getIdx());
        verify(mockAdminSupportJpaRepository, atLeastOnce()).findOneSupportModel(adminSupportEntity.getIdx());
        verifyNoMoreInteractions(mockAdminSupportJpaRepository);

        InOrder inOrder = inOrder(mockAdminSupportJpaRepository);
        inOrder.verify(mockAdminSupportJpaRepository).findOneSupportModel(adminSupportEntity.getIdx());
    }

    @Test
    @DisplayName("지원모델 수정 BDD 테스트")
    void 지원모델수정BDD테스트() {
        // given
        em.persist(adminSupportEntity);
        Long idx = em.find(AdminSupportEntity.class, this.adminSupportEntity.getIdx()).getIdx();

        adminSupportEntity = AdminSupportEntity.builder()
                .idx(idx)
                .supportName("test")
                .supportPhone("010-9466-2702")
                .supportHeight(170)
                .supportSize3("31-24-31")
                .supportMessage("test")
                .supportInstagram("https://instagram.com")
                .visible("Y")
                .build();

        adminSupportJpaRepository.updateSupportModel(adminSupportEntity);

        adminSupportDTO = AdminSupportEntity.toDto(adminSupportEntity);

        // when
        given(mockAdminSupportJpaRepository.findOneSupportModel(adminSupportEntity.getIdx())).willReturn(adminSupportDTO);
        AdminSupportDTO supportInfo = mockAdminSupportJpaRepository.findOneSupportModel(adminSupportEntity.getIdx());

        // then
        assertThat(supportInfo.getSupportName()).isEqualTo("test");
        assertThat(supportInfo.getSupportMessage()).isEqualTo("test");
        assertThat(supportInfo.getSupportHeight()).isEqualTo(170);

        // verify
        then(mockAdminSupportJpaRepository).should(times(1)).findOneSupportModel(adminSupportEntity.getIdx());
        then(mockAdminSupportJpaRepository).should(atLeastOnce()).findOneSupportModel(adminSupportEntity.getIdx());
        then(mockAdminSupportJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("지원 모델 삭제 테스트")
    void 지원모델삭제테스트() {
        // given
        em.persist(adminSupportEntity);

        Long entityIdx = adminSupportEntity.getIdx();
        Long deleteIdx = adminSupportJpaRepository.deleteSupportModel(adminSupportEntity.getIdx());

        // then
        assertThat(deleteIdx).isEqualTo(entityIdx);
    }

    @Test
    @DisplayName("지원 모델 삭제 Mockito 테스트")
    void 지원모델삭제Mockito테스트() {
        // given
        em.persist(adminSupportEntity);
        adminSupportDTO = AdminSupportEntity.toDto(adminSupportEntity);

        // when
        when(mockAdminSupportJpaRepository.findOneSupportModel(adminSupportEntity.getIdx())).thenReturn(adminSupportDTO);
        Long deleteIdx = adminSupportJpaRepository.deleteSupportModel(adminSupportEntity.getIdx());

        // then
        assertThat(mockAdminSupportJpaRepository.findOneSupportModel(adminSupportEntity.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockAdminSupportJpaRepository, times(1)).findOneSupportModel(adminSupportEntity.getIdx());
        verify(mockAdminSupportJpaRepository, atLeastOnce()).findOneSupportModel(adminSupportEntity.getIdx());
        verifyNoMoreInteractions(mockAdminSupportJpaRepository);

        InOrder inOrder = inOrder(mockAdminSupportJpaRepository);
        inOrder.verify(mockAdminSupportJpaRepository).findOneSupportModel(adminSupportEntity.getIdx());
    }

    @Test
    @DisplayName("지원 모델 삭제 BDD 테스트")
    void 지원모델삭제BDD테스트() {
        // given
        em.persist(adminSupportEntity);
        adminSupportDTO = AdminSupportEntity.toDto(adminSupportEntity);

        // when
        given(mockAdminSupportJpaRepository.findOneSupportModel(adminSupportEntity.getIdx())).willReturn(adminSupportDTO);
        Long deleteIdx = adminSupportJpaRepository.deleteSupportModel(adminSupportEntity.getIdx());

        // then
        assertThat(mockAdminSupportJpaRepository.findOneSupportModel(adminSupportEntity.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        then(mockAdminSupportJpaRepository).should(times(1)).findOneSupportModel(adminSupportEntity.getIdx());
        then(mockAdminSupportJpaRepository).should(atLeastOnce()).findOneSupportModel(adminSupportEntity.getIdx());
        then(mockAdminSupportJpaRepository).shouldHaveNoMoreInteractions();
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
        when(mockAdminSupportJpaRepository.findEvaluationList(evaluationMap)).thenReturn(evaluationList);
        List<EvaluationDTO> evaluationInfo = mockAdminSupportJpaRepository.findEvaluationList(evaluationMap);

        // then
        assertThat(evaluationInfo.get(0).getIdx()).isEqualTo(evaluationInfo.get(0).getIdx());
        assertThat(evaluationInfo.get(0).getSupportIdx()).isEqualTo(evaluationInfo.get(0).getSupportIdx());
        assertThat(evaluationInfo.get(0).getEvaluateComment()).isEqualTo(evaluationInfo.get(0).getEvaluateComment());

        // verify
        verify(mockAdminSupportJpaRepository, times(1)).findEvaluationList(evaluationMap);
        verify(mockAdminSupportJpaRepository, atLeastOnce()).findEvaluationList(evaluationMap);
        verifyNoMoreInteractions(mockAdminSupportJpaRepository);

        InOrder inOrder = inOrder(mockAdminSupportJpaRepository);
        inOrder.verify(mockAdminSupportJpaRepository).findEvaluationList(evaluationMap);
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
        given(mockAdminSupportJpaRepository.findEvaluationList(evaluationMap)).willReturn(evaluationList);
        List<EvaluationDTO> evaluationInfo = mockAdminSupportJpaRepository.findEvaluationList(evaluationMap);

        // then
        assertThat(evaluationInfo.get(0).getIdx()).isEqualTo(evaluationInfo.get(0).getIdx());
        assertThat(evaluationInfo.get(0).getSupportIdx()).isEqualTo(evaluationInfo.get(0).getSupportIdx());
        assertThat(evaluationInfo.get(0).getEvaluateComment()).isEqualTo(evaluationInfo.get(0).getEvaluateComment());

        // verify
        then(mockAdminSupportJpaRepository).should(times(1)).findEvaluationList(evaluationMap);
        then(mockAdminSupportJpaRepository).should(atLeastOnce()).findEvaluationList(evaluationMap);
        then(mockAdminSupportJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("지원 모델 평가 상세 조회 Mockito 테스트")
    void 지원모델평가상세조회Mockito테스트() {
        // given
        evaluationEntity = EvaluationEntity.builder()
                .idx(1L).supportIdx(adminSupportEntity.getIdx())
                .evaluateComment("합격").visible("Y").build();

        evaluationDTO = EvaluationEntity.toDto(evaluationEntity);

        // when
        when(mockAdminSupportJpaRepository.findOneEvaluation(evaluationEntity.getIdx())).thenReturn(evaluationDTO);
        EvaluationDTO evaluationInfo = mockAdminSupportJpaRepository.findOneEvaluation(evaluationEntity.getIdx());

        // then
        assertThat(evaluationInfo.getIdx()).isEqualTo(1);
        assertThat(evaluationInfo.getSupportIdx()).isEqualTo(adminSupportEntity.getIdx());
        assertThat(evaluationInfo.getEvaluateComment()).isEqualTo("합격");

        // verify
        verify(mockAdminSupportJpaRepository, times(1)).findOneEvaluation(evaluationEntity.getIdx());
        verify(mockAdminSupportJpaRepository, atLeastOnce()).findOneEvaluation(evaluationEntity.getIdx());
        verifyNoMoreInteractions(mockAdminSupportJpaRepository);

        InOrder inOrder = inOrder(mockAdminSupportJpaRepository);
        inOrder.verify(mockAdminSupportJpaRepository).findOneEvaluation(evaluationEntity.getIdx());
    }

    @Test
    @DisplayName("지원 모델 평가 상세 조회 BDD 테스트")
    void 지원모델평가상세조회BDD테스트() {
        // given
        evaluationEntity = EvaluationEntity.builder()
                .idx(1L).supportIdx(adminSupportEntity.getIdx())
                .evaluateComment("합격").visible("Y").build();

        evaluationDTO = EvaluationEntity.toDto(evaluationEntity);

        // when
        given(mockAdminSupportJpaRepository.findOneEvaluation(evaluationEntity.getIdx())).willReturn(evaluationDTO);
        EvaluationDTO evaluationInfo = mockAdminSupportJpaRepository.findOneEvaluation(evaluationEntity.getIdx());

        // then
        assertThat(evaluationInfo.getIdx()).isEqualTo(1);
        assertThat(evaluationInfo.getSupportIdx()).isEqualTo(adminSupportEntity.getIdx());
        assertThat(evaluationInfo.getEvaluateComment()).isEqualTo("합격");

        // verify
        then(mockAdminSupportJpaRepository).should(times(1)).findOneEvaluation(evaluationEntity.getIdx());
        then(mockAdminSupportJpaRepository).should(atLeastOnce()).findOneEvaluation(evaluationEntity.getIdx());
        then(mockAdminSupportJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("지원 모델 평가 Mockito 테스트")
    void 지원모델평가Mockito테스트() {
        // given
        Long supportIdx = adminSupportJpaRepository.insertSupportModel(adminSupportEntity).getIdx();

        evaluationEntity = EvaluationEntity.builder()
                .supportIdx(supportIdx)
                .evaluateComment("합격")
                .visible("Y")
                .build();

        adminSupportJpaRepository.evaluationSupportModel(evaluationEntity);

        evaluationDTO = EvaluationEntity.toDto(evaluationEntity);

        // when
        when(mockAdminSupportJpaRepository.findOneEvaluation(evaluationEntity.getIdx())).thenReturn(evaluationDTO);
        EvaluationDTO evaluationInfo = mockAdminSupportJpaRepository.findOneEvaluation(evaluationEntity.getIdx());

        // then
        assertThat(evaluationInfo.getSupportIdx()).isEqualTo(supportIdx);
        assertThat(evaluationInfo.getEvaluateComment()).isEqualTo("합격");

        // verify
        verify(mockAdminSupportJpaRepository, times(1)).findOneEvaluation(evaluationEntity.getIdx());
        verify(mockAdminSupportJpaRepository, atLeastOnce()).findOneEvaluation(evaluationEntity.getIdx());
        verifyNoMoreInteractions(mockAdminSupportJpaRepository);

        InOrder inOrder = inOrder(mockAdminSupportJpaRepository);
        inOrder.verify(mockAdminSupportJpaRepository).findOneEvaluation(evaluationEntity.getIdx());
    }

    @Test
    @DisplayName("지원 모델 평가 BDD 테스트")
    void 지원모델평가BDD테스트() {
        // given
        Long supportIdx = adminSupportJpaRepository.insertSupportModel(adminSupportEntity).getIdx();

        evaluationEntity = EvaluationEntity.builder()
                .supportIdx(supportIdx)
                .evaluateComment("합격")
                .visible("Y")
                .build();

        adminSupportJpaRepository.evaluationSupportModel(evaluationEntity);

        evaluationDTO = EvaluationEntity.toDto(evaluationEntity);

        // when
        given(mockAdminSupportJpaRepository.findOneEvaluation(evaluationEntity.getIdx())).willReturn(evaluationDTO);
        EvaluationDTO evaluationInfo = mockAdminSupportJpaRepository.findOneEvaluation(evaluationEntity.getIdx());

        // then
        assertThat(evaluationInfo.getSupportIdx()).isEqualTo(supportIdx);
        assertThat(evaluationInfo.getEvaluateComment()).isEqualTo("합격");

        // verify
        then(mockAdminSupportJpaRepository).should(times(1)).findOneEvaluation(evaluationEntity.getIdx());
        then(mockAdminSupportJpaRepository).should(atLeastOnce()).findOneEvaluation(evaluationEntity.getIdx());
        then(mockAdminSupportJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("지원 모델 평가 수정 Mockito 테스트")
    void 지원모델평가수정Mockito테스트() {
        // given
        Long supportIdx = adminSupportJpaRepository.insertSupportModel(adminSupportEntity).getIdx();

        evaluationEntity = EvaluationEntity.builder()
                .supportIdx(supportIdx)
                .evaluateComment("합격").visible("Y").build();

        // 지원모델 평가 저장
        em.persist(evaluationEntity);

        Long idx = em.find(EvaluationEntity.class, this.evaluationEntity.getIdx()).getIdx();

        evaluationEntity = EvaluationEntity.builder()
                .idx(idx)
                .supportIdx(adminSupportEntity.getIdx())
                .evaluateComment("합격")
                .visible("Y")
                .build();

        // 지원모델 평가 수정
        adminSupportJpaRepository.updateEvaluation(evaluationEntity);

        evaluationDTO = EvaluationEntity.toDto(evaluationEntity);

        // when
        when(mockAdminSupportJpaRepository.findOneEvaluation(evaluationEntity.getIdx())).thenReturn(evaluationDTO);
        EvaluationDTO evaluationInfo = mockAdminSupportJpaRepository.findOneEvaluation(evaluationEntity.getIdx());

        // then
        assertThat(evaluationInfo.getSupportIdx()).isEqualTo(adminSupportEntity.getIdx());
        assertThat(evaluationInfo.getEvaluateComment()).isEqualTo("합격");

        // verify
        verify(mockAdminSupportJpaRepository, times(1)).findOneEvaluation(evaluationEntity.getIdx());
        verify(mockAdminSupportJpaRepository, atLeastOnce()).findOneEvaluation(evaluationEntity.getIdx());
        verifyNoMoreInteractions(mockAdminSupportJpaRepository);

        InOrder inOrder = inOrder(mockAdminSupportJpaRepository);
        inOrder.verify(mockAdminSupportJpaRepository).findOneEvaluation(evaluationEntity.getIdx());
    }

    @Test
    @DisplayName("지원 모델 평가 수정 BDD 테스트")
    void 지원모델평가수정BDD테스트() {
        // given
        Long supportIdx = adminSupportJpaRepository.insertSupportModel(adminSupportEntity).getIdx();

        evaluationEntity = EvaluationEntity.builder()
                .supportIdx(supportIdx)
                .evaluateComment("합격").visible("Y").build();

        // 지원모델 평가 저장
        em.persist(evaluationEntity);

        Long idx = em.find(EvaluationEntity.class, this.evaluationEntity.getIdx()).getIdx();

        evaluationEntity = EvaluationEntity.builder()
                .idx(idx)
                .supportIdx(adminSupportEntity.getIdx())
                .evaluateComment("합격")
                .visible("Y")
                .build();

        // 지원모델 평가 수정
        adminSupportJpaRepository.updateEvaluation(evaluationEntity);

        evaluationDTO = EvaluationEntity.toDto(evaluationEntity);

        // when
        given(mockAdminSupportJpaRepository.findOneEvaluation(evaluationEntity.getIdx())).willReturn(evaluationDTO);
        EvaluationDTO evaluationInfo = mockAdminSupportJpaRepository.findOneEvaluation(evaluationEntity.getIdx());

        // then
        assertThat(evaluationInfo.getSupportIdx()).isEqualTo(adminSupportEntity.getIdx());
        assertThat(evaluationInfo.getEvaluateComment()).isEqualTo("합격");

        // verify
        then(mockAdminSupportJpaRepository).should(times(1)).findOneEvaluation(evaluationEntity.getIdx());
        then(mockAdminSupportJpaRepository).should(atLeastOnce()).findOneEvaluation(evaluationEntity.getIdx());
        then(mockAdminSupportJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("지원 모델 평가 삭제 Mockito 테스트")
    void 지원모델평가삭제Mockito테스트() {
        // given
        Long supportIdx = adminSupportJpaRepository.insertSupportModel(adminSupportEntity).getIdx();

        evaluationEntity = EvaluationEntity.builder()
                .supportIdx(supportIdx)
                .evaluateComment("합격").visible("Y").build();

        // 지원모델 평가 저장
        em.persist(evaluationEntity);
        evaluationDTO = EvaluationEntity.toDto(evaluationEntity);

        when(mockAdminSupportJpaRepository.findOneEvaluation(evaluationEntity.getIdx())).thenReturn(evaluationDTO);
        Long deleteIdx = adminSupportJpaRepository.deleteEvaluation(evaluationEntity.getIdx());

        // then
        assertThat(mockAdminSupportJpaRepository.findOneEvaluation(evaluationEntity.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockAdminSupportJpaRepository, times(1)).findOneEvaluation(evaluationEntity.getIdx());
        verify(mockAdminSupportJpaRepository, atLeastOnce()).findOneEvaluation(evaluationEntity.getIdx());
        verifyNoMoreInteractions(mockAdminSupportJpaRepository);

        InOrder inOrder = inOrder(mockAdminSupportJpaRepository);
        inOrder.verify(mockAdminSupportJpaRepository).findOneEvaluation(evaluationEntity.getIdx());
    }

    @Test
    @DisplayName("지원 모델 평가 삭제 BDD 테스트")
    void 지원모델평가삭제BDD테스트() {
        // given
        Long supportIdx = adminSupportJpaRepository.insertSupportModel(adminSupportEntity).getIdx();

        evaluationEntity = EvaluationEntity.builder()
                .supportIdx(supportIdx)
                .evaluateComment("합격").visible("Y").build();

        // 지원모델 평가 저장
        em.persist(evaluationEntity);
        evaluationDTO = EvaluationEntity.toDto(evaluationEntity);

        given(mockAdminSupportJpaRepository.findOneEvaluation(evaluationEntity.getIdx())).willReturn(evaluationDTO);
        Long deleteIdx = adminSupportJpaRepository.deleteEvaluation(evaluationEntity.getIdx());

        // then
        assertThat(mockAdminSupportJpaRepository.findOneEvaluation(evaluationEntity.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        then(mockAdminSupportJpaRepository).should(times(1)).findOneEvaluation(evaluationEntity.getIdx());
        then(mockAdminSupportJpaRepository).should(atLeastOnce()).findOneEvaluation(evaluationEntity.getIdx());
        then(mockAdminSupportJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("지원 모델 합격 Mockito 테스트")
    void 지원모델합격Mockito테스트() {
        // given
        Long supportIdx = adminSupportJpaRepository.insertSupportModel(adminSupportEntity).getIdx();

        AdminSupportDTO supportDTO = adminSupportJpaRepository.updatePass(supportIdx);

        // when
        when(mockAdminSupportJpaRepository.findOneSupportModel(adminSupportEntity.getIdx())).thenReturn(supportDTO);
        AdminSupportDTO supportInfo = mockAdminSupportJpaRepository.findOneSupportModel(adminSupportEntity.getIdx());

        // then
        assertThat(supportInfo.getIdx()).isEqualTo(supportIdx);
        assertThat(supportInfo.getPassYn()).isEqualTo("Y");
        assertThat(supportInfo.getPassTime()).isNotNull();

        verify(mockAdminSupportJpaRepository, times(1)).findOneSupportModel(adminSupportEntity.getIdx());
        verify(mockAdminSupportJpaRepository, atLeastOnce()).findOneSupportModel(adminSupportEntity.getIdx());
        verifyNoMoreInteractions(mockAdminSupportJpaRepository);

        InOrder inOrder = inOrder(mockAdminSupportJpaRepository);
        inOrder.verify(mockAdminSupportJpaRepository).findOneSupportModel(adminSupportEntity.getIdx());
    }

    @Test
    @DisplayName("지원 모델 합격 BDD 테스트")
    void 지원모델합격BDD테스트() {
        // given
        Long supportIdx = adminSupportJpaRepository.insertSupportModel(adminSupportEntity).getIdx();

        adminSupportJpaRepository.updatePass(supportIdx);
        adminSupportDTO = AdminSupportEntity.toDto(adminSupportEntity);

        // when
        when(mockAdminSupportJpaRepository.findOneSupportModel(adminSupportEntity.getIdx())).thenReturn(adminSupportDTO);
        AdminSupportDTO supportInfo = mockAdminSupportJpaRepository.findOneSupportModel(adminSupportEntity.getIdx());

        // then
        assertThat(supportInfo.getIdx()).isEqualTo(adminSupportEntity.getIdx());
        assertThat(supportInfo.getPassYn()).isEqualTo("Y");
        assertThat(supportInfo.getPassTime()).isNotNull();

        then(mockAdminSupportJpaRepository).should(times(1)).findOneSupportModel(adminSupportEntity.getIdx());
        then(mockAdminSupportJpaRepository).should(atLeastOnce()).findOneSupportModel(adminSupportEntity.getIdx());
        then(mockAdminSupportJpaRepository).shouldHaveNoMoreInteractions();
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

        Long supportIdx = adminSupportJpaRepository.insertSupportModel(adminSupportEntity).getIdx();

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

        when(mockAdminSupportJpaRepository.findSupportAdminComment(adminSupportEntity.getIdx())).thenReturn(adminCommentList);
        List<AdminCommentDTO> newAdminCommentList = mockAdminSupportJpaRepository.findSupportAdminComment(adminSupportEntity.getIdx());

        assertThat(newAdminCommentList.get(0).getCommentType()).isEqualTo("support");
        assertThat(newAdminCommentList.get(0).getCommentTypeIdx()).isEqualTo(adminSupportEntity.getIdx());

        verify(mockAdminSupportJpaRepository, times(1)).findSupportAdminComment(adminSupportEntity.getIdx());
        verify(mockAdminSupportJpaRepository, atLeastOnce()).findSupportAdminComment(adminSupportEntity.getIdx());
        verifyNoMoreInteractions(mockAdminSupportJpaRepository);

        InOrder inOrder = inOrder(mockAdminSupportJpaRepository);
        inOrder.verify(mockAdminSupportJpaRepository).findSupportAdminComment(adminSupportEntity.getIdx());
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

        Long supportIdx = adminSupportJpaRepository.insertSupportModel(adminSupportEntity).getIdx();

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

        given(mockAdminSupportJpaRepository.findSupportAdminComment(adminSupportEntity.getIdx())).willReturn(adminCommentList);
        List<AdminCommentDTO> newAdminCommentList = mockAdminSupportJpaRepository.findSupportAdminComment(adminSupportEntity.getIdx());

        assertThat(newAdminCommentList.get(0).getCommentType()).isEqualTo("support");
        assertThat(newAdminCommentList.get(0).getCommentTypeIdx()).isEqualTo(adminSupportEntity.getIdx());

        // verify
        then(mockAdminSupportJpaRepository).should(times(1)).findSupportAdminComment(adminSupportEntity.getIdx());
        then(mockAdminSupportJpaRepository).should(atLeastOnce()).findSupportAdminComment(adminSupportEntity.getIdx());
        then(mockAdminSupportJpaRepository).shouldHaveNoMoreInteractions();
    }
}