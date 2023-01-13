package com.tsp.new_tsp_admin.api.model.service.negotiation;

import com.tsp.new_tsp_admin.api.domain.model.AdminModelDTO;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import com.tsp.new_tsp_admin.api.domain.model.CareerJson;
import com.tsp.new_tsp_admin.api.domain.model.agency.AdminAgencyDTO;
import com.tsp.new_tsp_admin.api.domain.model.agency.AdminAgencyEntity;
import com.tsp.new_tsp_admin.api.domain.model.negotiation.AdminNegotiationDTO;
import com.tsp.new_tsp_admin.api.domain.model.negotiation.AdminNegotiationEntity;
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
@DisplayName("모델 섭외 Repository Test")
class AdminNegotiationJpaQueryRepositoryTest {
    @Mock private AdminNegotiationJpaQueryRepository mockAdminNegotiationJpaQueryRepository;
    private final AdminNegotiationJpaQueryRepository adminNegotiationJpaQueryRepository;
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

        adminAgencyDTO = AdminAgencyEntity.toDto(adminAgencyEntity);

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
                .adminAgencyEntity(adminAgencyEntity)
                .careerList(careerList)
                .height(170)
                .size3("34-24-34")
                .shoes(270)
                .visible("Y")
                .build();

        em.persist(adminModelEntity);

        adminModelDTO = AdminModelEntity.toDto(adminModelEntity);

        adminNegotiationEntity = AdminNegotiationEntity.builder()
                .modelKorName(adminModelEntity.getModelKorName())
                .modelNegotiationDesc("영화 프로젝트 참여")
                .modelNegotiationDate(now())
                .name("조찬희")
                .phone("010-1234-5678")
                .email("test@gmail.com")
                .visible("Y")
                .build();

        adminNegotiationDTO = AdminNegotiationEntity.toDto(adminNegotiationEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createModelAndNegotiation();
    }

    @Test
    @DisplayName("모델 섭외 리스트 조회 테스트")
    void 모델섭외리스트조회테스트() {
        // given
        Map<String, Object> negotiationMap = new HashMap<>();
        negotiationMap.put("searchKeyword", "김예영");
        negotiationMap.put("jpaStartPage", 0);
        negotiationMap.put("size", 100);

        // then
        assertThat(adminNegotiationJpaQueryRepository.findNegotiationList(negotiationMap)).isNotEmpty();

        Map<String, Object> lastMonthNegotiationMap = new HashMap<>();
        lastMonthNegotiationMap.put("searchStartTime", of(now().getYear(), LocalDate.now().minusMonths(1).getMonth(), 1, 0, 0, 0, 0));
        lastMonthNegotiationMap.put("searchEndTime", of(now().getYear(), LocalDate.now().minusMonths(1).getMonth(), 30, 23, 59, 59));
        lastMonthNegotiationMap.put("jpaStartPage", 0);
        lastMonthNegotiationMap.put("size", 100);

        // then
        assertThat(adminNegotiationJpaQueryRepository.findNegotiationList(negotiationMap)).isNotEmpty();

        Map<String, Object> currentNegotiationMap = new HashMap<>();
        currentNegotiationMap.put("searchStartTime", of(now().getYear(), LocalDate.now().getMonth(), 1, 0, 0, 0, 0));
        currentNegotiationMap.put("searchEndTime", of(now().getYear(), LocalDate.now().getMonth(), 30, 23, 59, 59));
        currentNegotiationMap.put("jpaStartPage", 0);
        currentNegotiationMap.put("size", 100);

        // then
        assertThat(adminNegotiationJpaQueryRepository.findNegotiationList(negotiationMap)).isNotEmpty();
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

        // when
        when(mockAdminNegotiationJpaQueryRepository.findNegotiationList(negotiationMap)).thenReturn(negotiationList);
        List<AdminNegotiationDTO> newModelNegotiationList = mockAdminNegotiationJpaQueryRepository.findNegotiationList(negotiationMap);

        // then
        assertThat(newModelNegotiationList.get(0).getIdx()).isEqualTo(negotiationList.get(0).getIdx());
        assertThat(newModelNegotiationList.get(0).getModelNegotiationDesc()).isEqualTo(negotiationList.get(0).getModelNegotiationDesc());

        // verify
        verify(mockAdminNegotiationJpaQueryRepository, times(1)).findNegotiationList(negotiationMap);
        verify(mockAdminNegotiationJpaQueryRepository, atLeastOnce()).findNegotiationList(negotiationMap);
        verifyNoMoreInteractions(mockAdminNegotiationJpaQueryRepository);

        InOrder inOrder = inOrder(mockAdminNegotiationJpaQueryRepository);
        inOrder.verify(mockAdminNegotiationJpaQueryRepository).findNegotiationList(negotiationMap);
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

        // when
        given(mockAdminNegotiationJpaQueryRepository.findNegotiationList(negotiationMap)).willReturn(negotiationList);
        List<AdminNegotiationDTO> newModelNegotiationList = mockAdminNegotiationJpaQueryRepository.findNegotiationList(negotiationMap);

        // then
        assertThat(newModelNegotiationList.get(0).getIdx()).isEqualTo(negotiationList.get(0).getIdx());
        assertThat(newModelNegotiationList.get(0).getModelNegotiationDesc()).isEqualTo(negotiationList.get(0).getModelNegotiationDesc());

        // verify
        then(mockAdminNegotiationJpaQueryRepository).should(times(1)).findNegotiationList(negotiationMap);
        then(mockAdminNegotiationJpaQueryRepository).should(atLeastOnce()).findNegotiationList(negotiationMap);
        then(mockAdminNegotiationJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 or 다음 모델 섭외 상세 조회 테스트")
    void 이전or다음모델섭외상세조회테스트() {
        // given
        adminNegotiationEntity = AdminNegotiationEntity.builder().idx(2L).build();

        // when
        adminNegotiationDTO = adminNegotiationJpaQueryRepository.findOneNegotiation(adminNegotiationEntity.getIdx());

        // 이전 모델 섭외
        assertThat(adminNegotiationJpaQueryRepository.findPrevOneNegotiation(adminNegotiationEntity.getIdx()).getIdx()).isEqualTo(1);
        // 다음 모델 섭외
        assertThat(adminNegotiationJpaQueryRepository.findNextOneNegotiation(adminNegotiationEntity.getIdx()).getIdx()).isEqualTo(3);
    }

    @Test
    @DisplayName("이전 모델 섭외 상세 조회 Mockito 테스트")
    void 이전모델섭외상세조회Mockito테스트() {
        // given
        adminNegotiationEntity = AdminNegotiationEntity.builder().idx(2L).build();

        // when
        adminNegotiationDTO = adminNegotiationJpaQueryRepository.findOneNegotiation(adminNegotiationEntity.getIdx());

        when(mockAdminNegotiationJpaQueryRepository.findPrevOneNegotiation(adminNegotiationEntity.getIdx())).thenReturn(adminNegotiationDTO);
        AdminNegotiationDTO negotiationInfo = mockAdminNegotiationJpaQueryRepository.findPrevOneNegotiation(adminNegotiationEntity.getIdx());

        // then
        assertThat(negotiationInfo.getIdx()).isEqualTo(1);

        // verify
        verify(mockAdminNegotiationJpaQueryRepository, times(1)).findPrevOneNegotiation(adminNegotiationEntity.getIdx());
        verify(mockAdminNegotiationJpaQueryRepository, atLeastOnce()).findPrevOneNegotiation(adminNegotiationEntity.getIdx());
        verifyNoMoreInteractions(mockAdminNegotiationJpaQueryRepository);

        InOrder inOrder = inOrder(mockAdminNegotiationJpaQueryRepository);
        inOrder.verify(mockAdminNegotiationJpaQueryRepository).findPrevOneNegotiation(adminNegotiationEntity.getIdx());
    }

    @Test
    @DisplayName("이전 모델 섭외 상세 조회 BDD 테스트")
    void 이전모델섭외상세조회BDD테스트() {
        // given
        adminNegotiationEntity = AdminNegotiationEntity.builder().idx(2L).build();

        // when
        adminNegotiationDTO = adminNegotiationJpaQueryRepository.findOneNegotiation(adminNegotiationEntity.getIdx());

        given(mockAdminNegotiationJpaQueryRepository.findPrevOneNegotiation(adminNegotiationEntity.getIdx())).willReturn(adminNegotiationDTO);
        AdminNegotiationDTO negotiationInfo = mockAdminNegotiationJpaQueryRepository.findPrevOneNegotiation(adminNegotiationEntity.getIdx());

        // then
        assertThat(negotiationInfo.getIdx()).isEqualTo(1);

        // verify
        then(mockAdminNegotiationJpaQueryRepository).should(times(1)).findPrevOneNegotiation(adminNegotiationEntity.getIdx());
        then(mockAdminNegotiationJpaQueryRepository).should(atLeastOnce()).findPrevOneNegotiation(adminNegotiationEntity.getIdx());
        then(mockAdminNegotiationJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 모델 섭외 상세 조회 Mockito 테스트")
    void 다음모델섭외상세조회Mockito테스트() {
        // given
        adminNegotiationEntity = AdminNegotiationEntity.builder().idx(2L).build();

        // when
        adminNegotiationDTO = adminNegotiationJpaQueryRepository.findOneNegotiation(adminNegotiationEntity.getIdx());

        when(mockAdminNegotiationJpaQueryRepository.findNextOneNegotiation(adminNegotiationEntity.getIdx())).thenReturn(adminNegotiationDTO);
        AdminNegotiationDTO negotiationInfo = mockAdminNegotiationJpaQueryRepository.findNextOneNegotiation(adminNegotiationEntity.getIdx());

        // then
        assertThat(negotiationInfo.getIdx()).isEqualTo(3);

        // verify
        verify(mockAdminNegotiationJpaQueryRepository, times(1)).findNextOneNegotiation(adminNegotiationEntity.getIdx());
        verify(mockAdminNegotiationJpaQueryRepository, atLeastOnce()).findNextOneNegotiation(adminNegotiationEntity.getIdx());
        verifyNoMoreInteractions(mockAdminNegotiationJpaQueryRepository);

        InOrder inOrder = inOrder(mockAdminNegotiationJpaQueryRepository);
        inOrder.verify(mockAdminNegotiationJpaQueryRepository).findNextOneNegotiation(adminNegotiationEntity.getIdx());
    }

    @Test
    @DisplayName("다음 모델 섭외 상세 조회 BDD 테스트")
    void 다음모델섭외상세조회BDD테스트() {
        // given
        adminNegotiationEntity = AdminNegotiationEntity.builder().idx(2L).build();

        // when
        adminNegotiationDTO = adminNegotiationJpaQueryRepository.findOneNegotiation(adminNegotiationEntity.getIdx());

        given(mockAdminNegotiationJpaQueryRepository.findNextOneNegotiation(adminNegotiationEntity.getIdx())).willReturn(adminNegotiationDTO);
        AdminNegotiationDTO negotiationInfo = mockAdminNegotiationJpaQueryRepository.findNextOneNegotiation(adminNegotiationEntity.getIdx());

        // then
        assertThat(negotiationInfo.getIdx()).isEqualTo(3);

        // verify
        then(mockAdminNegotiationJpaQueryRepository).should(times(1)).findNextOneNegotiation(adminNegotiationEntity.getIdx());
        then(mockAdminNegotiationJpaQueryRepository).should(atLeastOnce()).findNextOneNegotiation(adminNegotiationEntity.getIdx());
        then(mockAdminNegotiationJpaQueryRepository).shouldHaveNoMoreInteractions();
    }
}