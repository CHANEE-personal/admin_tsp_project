package com.tsp.new_tsp_admin.api.model.service.negotiation;

import com.tsp.new_tsp_admin.api.domain.model.AdminModelDTO;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import com.tsp.new_tsp_admin.api.domain.model.CareerJson;
import com.tsp.new_tsp_admin.api.domain.model.agency.AdminAgencyDTO;
import com.tsp.new_tsp_admin.api.domain.model.agency.AdminAgencyEntity;
import com.tsp.new_tsp_admin.api.domain.model.negotiation.AdminNegotiationDTO;
import com.tsp.new_tsp_admin.api.domain.model.negotiation.AdminNegotiationEntity;
import com.tsp.new_tsp_admin.api.model.mapper.ModelMapper;
import com.tsp.new_tsp_admin.api.model.mapper.agency.AgencyMapper;
import com.tsp.new_tsp_admin.api.model.mapper.negotiation.NegotiationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.time.LocalDateTime.now;
import static java.time.LocalDateTime.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@Slf4j
@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("모델 스케줄 Repository Test")
class AdminNegotiationJpaRepositoryTest {
    @Mock private AdminNegotiationJpaRepository mockAdminNegotiationJpaRepository;
    private final AdminNegotiationJpaRepository adminNegotiationJpaRepository;
    private final EntityManager em;

    private AdminModelEntity adminModelEntity;
    private AdminModelDTO adminModelDTO;
    private AdminNegotiationEntity adminNegotiationEntity;
    private AdminNegotiationDTO adminNegotiationDTO;
    private AdminAgencyEntity adminAgencyEntity;
    private AdminAgencyDTO adminAgencyDTO;

    void createModelAndNegotiation() {
        adminAgencyEntity = AdminAgencyEntity.builder()
                .agencyName("agency")
                .agencyDescription("agency")
                .visible("Y")
                .build();

        adminAgencyDTO = AgencyMapper.INSTANCE.toDto(adminAgencyEntity);

        ArrayList<CareerJson> careerList = new ArrayList<>();
        careerList.add(new CareerJson("title","txt"));

        adminModelEntity = AdminModelEntity.builder()
                .categoryCd(1)
                .categoryAge(2)
                .modelKorFirstName("조")
                .modelKorSecondName("찬희")
                .modelKorName("조찬희")
                .modelFirstName("CHO")
                .modelSecondName("CHANHEE")
                .modelEngName("CHOCHANHEE")
                .modelDescription("chaneeCho")
                .modelMainYn("Y")
                .status("active")
                .newYn("N")
                .favoriteCount(1)
                .viewCount(1)
                .agencyIdx(adminAgencyEntity.getIdx())
                .adminAgencyEntity(adminAgencyEntity)
                .careerList(careerList)
                .height(170)
                .size3("34-24-34")
                .shoes(270)
                .visible("Y")
                .build();

        em.persist(adminModelEntity);

        adminModelDTO = ModelMapper.INSTANCE.toDto(adminModelEntity);

        adminNegotiationEntity = AdminNegotiationEntity.builder()
                .modelIdx(adminModelEntity.getIdx())
                .modelKorName(adminModelEntity.getModelKorName())
                .modelNegotiationDesc("영화 프로젝트 참여")
                .modelNegotiationDate(now())
                .name("조찬희")
                .phone("010-1234-5678")
                .email("test@gmail.com")
                .visible("Y")
                .build();

        adminNegotiationDTO = NegotiationMapper.INSTANCE.toDto(adminNegotiationEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createModelAndNegotiation();
    }

    @Test
    @DisplayName("모델 스케줄 리스트 조회 테스트")
    void 모델스케줄리스트조회테스트() {
        // given
        Map<String, Object> negotiationMap = new HashMap<>();
        negotiationMap.put("searchKeyword", "김예영");
        negotiationMap.put("jpaStartPage", 0);
        negotiationMap.put("size", 100);

        // then
        assertThat(adminNegotiationJpaRepository.findModelNegotiationList(negotiationMap)).isNotEmpty();

        Map<String, Object> lastMonthNegotiationMap = new HashMap<>();
        lastMonthNegotiationMap.put("searchStartTime", of(now().getYear(), LocalDate.now().minusMonths(1).getMonth(), 1, 0, 0, 0, 0));
        lastMonthNegotiationMap.put("searchEndTime", of(now().getYear(), LocalDate.now().minusMonths(1).getMonth(), 30, 23, 59, 59));
        lastMonthNegotiationMap.put("jpaStartPage", 0);
        lastMonthNegotiationMap.put("size", 100);

        // then
        assertThat(adminNegotiationJpaRepository.findModelNegotiationList(negotiationMap)).isNotEmpty();

        Map<String, Object> currentNegotiationMap = new HashMap<>();
        currentNegotiationMap.put("searchStartTime", of(now().getYear(), LocalDate.now().getMonth(), 1, 0, 0, 0, 0));
        currentNegotiationMap.put("searchEndTime", of(now().getYear(), LocalDate.now().getMonth(), 30, 23, 59, 59));
        currentNegotiationMap.put("jpaStartPage", 0);
        currentNegotiationMap.put("size", 100);

        // then
        assertThat(adminNegotiationJpaRepository.findModelNegotiationList(negotiationMap)).isNotEmpty();
    }

    @Test
    @DisplayName("모델 섭외 Mockito 조회 테스트")
    void 모델섭외Mockito조회테스트() {
        // given
        Map<String, Object> negotiationMap = new HashMap<>();
        negotiationMap.put("jpaStartPage", 1);
        negotiationMap.put("size", 3);

        List<AdminNegotiationDTO> negotiationList = new ArrayList<>();
        negotiationList.add(AdminNegotiationDTO.builder().modelIdx(adminModelEntity.getIdx())
                .modelNegotiationDesc("영화 프로젝트 참여 테스트 첫번째").modelNegotiationDate(now()).build());
        negotiationList.add(AdminNegotiationDTO.builder().modelIdx(adminModelEntity.getIdx())
                .modelNegotiationDesc("영화 프로젝트 참여 테스트 두번째").modelNegotiationDate(now()).build());

        List<AdminModelDTO> modelNegotiationList = new ArrayList<>();
        modelNegotiationList.add(AdminModelDTO.builder().idx(3).categoryCd(1).modelKorName("조찬희")
                .modelNegotiation(negotiationList).build());

        // when
        when(mockAdminNegotiationJpaRepository.findModelNegotiationList(negotiationMap)).thenReturn(modelNegotiationList);
        List<AdminModelDTO> newModelNegotiationList = mockAdminNegotiationJpaRepository.findModelNegotiationList(negotiationMap);

        // then
        assertThat(newModelNegotiationList.get(0).getIdx()).isEqualTo(modelNegotiationList.get(0).getIdx());
        assertThat(newModelNegotiationList.get(0).getModelNegotiation().get(0).getModelNegotiationDesc()).isEqualTo(negotiationList.get(0).getModelNegotiationDesc());

        // verify
        verify(mockAdminNegotiationJpaRepository, times(1)).findModelNegotiationList(negotiationMap);
        verify(mockAdminNegotiationJpaRepository, atLeastOnce()).findModelNegotiationList(negotiationMap);
        verifyNoMoreInteractions(mockAdminNegotiationJpaRepository);

        InOrder inOrder = inOrder(mockAdminNegotiationJpaRepository);
        inOrder.verify(mockAdminNegotiationJpaRepository).findModelNegotiationList(negotiationMap);
    }

    @Test
    @DisplayName("모델 섭외 BDD 조회 테스트")
    void 모델섭외BDD조회테스트() {
        // given
        Map<String, Object> negotiationMap = new HashMap<>();
        negotiationMap.put("jpaStartPage", 1);
        negotiationMap.put("size", 3);

        List<AdminNegotiationDTO> negotiationList = new ArrayList<>();
        negotiationList.add(AdminNegotiationDTO.builder().modelIdx(adminModelEntity.getIdx())
                .modelNegotiationDesc("영화 프로젝트 참여 테스트 첫번째").modelNegotiationDate(now()).build());
        negotiationList.add(AdminNegotiationDTO.builder().modelIdx(adminModelEntity.getIdx())
                .modelNegotiationDesc("영화 프로젝트 참여 테스트 두번째").modelNegotiationDate(now()).build());

        List<AdminModelDTO> modelNegotiationList = new ArrayList<>();
        modelNegotiationList.add(AdminModelDTO.builder().idx(3).categoryCd(1).modelKorName("조찬희")
                .modelNegotiation(negotiationList).build());

        // when
        given(mockAdminNegotiationJpaRepository.findModelNegotiationList(negotiationMap)).willReturn(modelNegotiationList);
        List<AdminModelDTO> newModelNegotiationList = mockAdminNegotiationJpaRepository.findModelNegotiationList(negotiationMap);

        // then
        assertThat(newModelNegotiationList.get(0).getIdx()).isEqualTo(modelNegotiationList.get(0).getIdx());
        assertThat(newModelNegotiationList.get(0).getModelNegotiation().get(0).getModelNegotiationDesc()).isEqualTo(negotiationList.get(0).getModelNegotiationDesc());

        // verify
        then(mockAdminNegotiationJpaRepository).should(times(1)).findModelNegotiationList(negotiationMap);
        then(mockAdminNegotiationJpaRepository).should(atLeastOnce()).findModelNegotiationList(negotiationMap);
        then(mockAdminNegotiationJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델섭외등록Mockito테스트")
    void 모델섭외등록Mockito테스트() {
        // given
        adminNegotiationJpaRepository.insertModelNegotiation(adminNegotiationEntity);

        // when
        when(mockAdminNegotiationJpaRepository.findOneNegotiation(adminNegotiationEntity)).thenReturn(adminNegotiationDTO);
        AdminNegotiationDTO negotiationInfo = mockAdminNegotiationJpaRepository.findOneNegotiation(adminNegotiationEntity);

        // then
        assertThat(negotiationInfo.getModelIdx()).isEqualTo(adminModelEntity.getIdx());
        assertThat(negotiationInfo.getModelKorName()).isEqualTo("조찬희");
        assertThat(negotiationInfo.getModelNegotiationDesc()).isNotNull();

        // verify
        verify(mockAdminNegotiationJpaRepository, times(1)).findOneNegotiation(adminNegotiationEntity);
        verify(mockAdminNegotiationJpaRepository, atLeastOnce()).findOneNegotiation(adminNegotiationEntity);
        verifyNoMoreInteractions(mockAdminNegotiationJpaRepository);

        InOrder inOrder = inOrder(mockAdminNegotiationJpaRepository);
        inOrder.verify(mockAdminNegotiationJpaRepository).findOneNegotiation(adminNegotiationEntity);
    }

    @Test
    @DisplayName("모델섭외등록BDD테스트")
    void 모델섭외등록BDD테스트() {
        // given
        adminNegotiationJpaRepository.insertModelNegotiation(adminNegotiationEntity);

        // when
        when(mockAdminNegotiationJpaRepository.findOneNegotiation(adminNegotiationEntity)).thenReturn(adminNegotiationDTO);
        AdminNegotiationDTO negotiationInfo = mockAdminNegotiationJpaRepository.findOneNegotiation(adminNegotiationEntity);

        // then
        assertThat(negotiationInfo.getModelIdx()).isEqualTo(adminModelEntity.getIdx());
        assertThat(negotiationInfo.getModelKorName()).isEqualTo("조찬희");
        assertThat(negotiationInfo.getModelNegotiationDesc()).isNotNull();

        // verify
        then(mockAdminNegotiationJpaRepository).should(times(1)).findOneNegotiation(adminNegotiationEntity);
        then(mockAdminNegotiationJpaRepository).should(atLeastOnce()).findOneNegotiation(adminNegotiationEntity);
        then(mockAdminNegotiationJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델섭외수정Mockito테스트")
    void 모델섭외수정Mockito테스트() {
        // given
        Integer idx = adminNegotiationJpaRepository.insertModelNegotiation(adminNegotiationEntity).getIdx();

        adminNegotiationEntity = AdminNegotiationEntity.builder()
                .idx(idx)
                .modelIdx(adminModelEntity.getIdx())
                .modelKorName(adminModelEntity.getModelKorName())
                .modelNegotiationDesc("섭외 수정 테스트")
                .modelNegotiationDate(now())
                .name("조찬희")
                .phone("010-1234-5678")
                .email("test@gmail.com")
                .visible("Y")
                .build();

        AdminNegotiationDTO adminNegotiationDTO = NegotiationMapper.INSTANCE.toDto(adminNegotiationEntity);

        adminNegotiationJpaRepository.updateModelNegotiation(adminNegotiationEntity);

        // when
        when(mockAdminNegotiationJpaRepository.findOneNegotiation(adminNegotiationEntity)).thenReturn(adminNegotiationDTO);
        AdminNegotiationDTO negotiationInfo = mockAdminNegotiationJpaRepository.findOneNegotiation(adminNegotiationEntity);

        // then
        assertThat(negotiationInfo.getModelIdx()).isEqualTo(adminModelEntity.getIdx());
        assertThat(negotiationInfo.getModelNegotiationDesc()).isEqualTo("섭외 수정 테스트");

        // verify
        verify(mockAdminNegotiationJpaRepository, times(1)).findOneNegotiation(adminNegotiationEntity);
        verify(mockAdminNegotiationJpaRepository, atLeastOnce()).findOneNegotiation(adminNegotiationEntity);
        verifyNoMoreInteractions(mockAdminNegotiationJpaRepository);

        InOrder inOrder = inOrder(mockAdminNegotiationJpaRepository);
        inOrder.verify(mockAdminNegotiationJpaRepository).findOneNegotiation(adminNegotiationEntity);
    }

    @Test
    @DisplayName("모델스케줄수정BDD테스트")
    void 모델스케줄수정BDD테스트() {
        // given
        Integer idx = adminNegotiationJpaRepository.insertModelNegotiation(adminNegotiationEntity).getIdx();

        adminNegotiationEntity = AdminNegotiationEntity.builder()
                .idx(idx)
                .modelIdx(adminModelEntity.getIdx())
                .modelKorName(adminModelEntity.getModelKorName())
                .modelNegotiationDesc("섭외 수정 테스트")
                .modelNegotiationDate(now())
                .name("조찬희")
                .phone("010-1234-5678")
                .email("test@gmail.com")
                .visible("Y")
                .build();

        AdminNegotiationDTO adminNegotiationDTO = NegotiationMapper.INSTANCE.toDto(adminNegotiationEntity);

        adminNegotiationJpaRepository.updateModelNegotiation(adminNegotiationEntity);

        // when
        given(mockAdminNegotiationJpaRepository.findOneNegotiation(adminNegotiationEntity)).willReturn(adminNegotiationDTO);
        AdminNegotiationDTO negotiationInfo = mockAdminNegotiationJpaRepository.findOneNegotiation(adminNegotiationEntity);

        // then
        assertThat(negotiationInfo.getModelIdx()).isEqualTo(adminModelEntity.getIdx());
        assertThat(negotiationInfo.getModelNegotiationDesc()).isEqualTo("섭외 수정 테스트");

        // verify
        then(mockAdminNegotiationJpaRepository).should(times(1)).findOneNegotiation(adminNegotiationEntity);
        then(mockAdminNegotiationJpaRepository).should(atLeastOnce()).findOneNegotiation(adminNegotiationEntity);
        then(mockAdminNegotiationJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델섭외삭제Mockito테스트")
    void 모델섭외삭제Mockito테스트() {
        // given
        em.persist(adminNegotiationEntity);
        adminNegotiationDTO = NegotiationMapper.INSTANCE.toDto(adminNegotiationEntity);

        // when
        when(mockAdminNegotiationJpaRepository.findOneNegotiation(adminNegotiationEntity)).thenReturn(adminNegotiationDTO);
        Integer deleteIdx = adminNegotiationJpaRepository.deleteModelNegotiation(adminNegotiationEntity.getIdx());

        // then
        assertThat(mockAdminNegotiationJpaRepository.findOneNegotiation(adminNegotiationEntity).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockAdminNegotiationJpaRepository, times(1)).findOneNegotiation(adminNegotiationEntity);
        verify(mockAdminNegotiationJpaRepository, atLeastOnce()).findOneNegotiation(adminNegotiationEntity);
        verifyNoMoreInteractions(mockAdminNegotiationJpaRepository);

        InOrder inOrder = inOrder(mockAdminNegotiationJpaRepository);
        inOrder.verify(mockAdminNegotiationJpaRepository).findOneNegotiation(adminNegotiationEntity);
    }

    @Test
    @DisplayName("모델섭외삭제BDD테스트")
    void 모델섭외삭제BDD테스트() {
        // given
        em.persist(adminNegotiationEntity);
        adminNegotiationDTO = NegotiationMapper.INSTANCE.toDto(adminNegotiationEntity);

        // when
        when(mockAdminNegotiationJpaRepository.findOneNegotiation(adminNegotiationEntity)).thenReturn(adminNegotiationDTO);
        Integer deleteIdx = adminNegotiationJpaRepository.deleteModelNegotiation(adminNegotiationEntity.getIdx());

        // then
        assertThat(mockAdminNegotiationJpaRepository.findOneNegotiation(adminNegotiationEntity).getIdx()).isEqualTo(deleteIdx);

        // verify
        then(mockAdminNegotiationJpaRepository).should(times(1)).findOneNegotiation(adminNegotiationEntity);
        then(mockAdminNegotiationJpaRepository).should(atLeastOnce()).findOneNegotiation(adminNegotiationEntity);
        then(mockAdminNegotiationJpaRepository).shouldHaveNoMoreInteractions();
    }
}