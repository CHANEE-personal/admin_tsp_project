package com.tsp.new_tsp_admin.api.model.service.negotiation;

import com.tsp.new_tsp_admin.api.domain.model.AdminModelDTO;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import com.tsp.new_tsp_admin.api.domain.model.CareerJson;
import com.tsp.new_tsp_admin.api.domain.model.agency.AdminAgencyDTO;
import com.tsp.new_tsp_admin.api.domain.model.agency.AdminAgencyEntity;
import com.tsp.new_tsp_admin.api.domain.model.negotiation.AdminNegotiationDTO;
import com.tsp.new_tsp_admin.api.domain.model.negotiation.AdminNegotiationEntity;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;
import static java.time.LocalDateTime.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("모델 섭외 Service Test")
class AdminNegotiationJpaServiceTest {
    @Mock
    private AdminNegotiationJpaService mockAdminNegotiationJpaService;
    private final AdminNegotiationJpaService adminNegotiationJpaService;
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
        PageRequest pageRequest = PageRequest.of(1, 100);

        // then
        assertThat(adminNegotiationJpaService.findNegotiationList(negotiationMap, pageRequest)).isNotEmpty();

        Map<String, Object> lastMonthNegotiationMap = new HashMap<>();
        lastMonthNegotiationMap.put("searchStartTime", of(now().getYear(), LocalDate.now().minusMonths(1).getMonth(), 1, 0, 0, 0, 0));
        lastMonthNegotiationMap.put("searchEndTime", of(now().getYear(), LocalDate.now().minusMonths(1).getMonth(), 30, 23, 59, 59));
        PageRequest pageRequest2 = PageRequest.of(1, 100);

        // then
        assertThat(adminNegotiationJpaService.findNegotiationList(negotiationMap, pageRequest2)).isNotEmpty();

        Map<String, Object> currentNegotiationMap = new HashMap<>();
        currentNegotiationMap.put("searchStartTime", of(now().getYear(), LocalDate.now().getMonth(), 1, 0, 0, 0, 0));
        currentNegotiationMap.put("searchEndTime", of(now().getYear(), LocalDate.now().getMonth(), 30, 23, 59, 59));
        PageRequest pageRequest3 = PageRequest.of(1, 100);

        // then
        assertThat(adminNegotiationJpaService.findNegotiationList(negotiationMap, pageRequest3)).isNotEmpty();
    }

    @Test
    @DisplayName("모델 섭외 Mockito 조회 테스트")
    void 모델섭외Mockito조회테스트() {
        // given
        Map<String, Object> negotiationMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<AdminNegotiationDTO> negotiationList = new ArrayList<>();
        negotiationList.add(AdminNegotiationDTO.builder().modelIdx(adminModelEntity.getIdx())
                .modelNegotiationDesc("영화 프로젝트 참여 테스트 첫번째").modelNegotiationDate(now()).build());
        negotiationList.add(AdminNegotiationDTO.builder().modelIdx(adminModelEntity.getIdx())
                .modelNegotiationDesc("영화 프로젝트 참여 테스트 두번째").modelNegotiationDate(now()).build());

        Page<AdminNegotiationDTO> resultNegotiation = new PageImpl<>(negotiationList, pageRequest, negotiationList.size());

        // when
        when(mockAdminNegotiationJpaService.findNegotiationList(negotiationMap, pageRequest)).thenReturn(resultNegotiation);
        Page<AdminNegotiationDTO> newModelNegotiationList = mockAdminNegotiationJpaService.findNegotiationList(negotiationMap, pageRequest);
        List<AdminNegotiationDTO> findNegotiationList = newModelNegotiationList.stream().collect(Collectors.toList());

        // then
        assertThat(findNegotiationList.get(0).getIdx()).isEqualTo(negotiationList.get(0).getIdx());
        assertThat(findNegotiationList.get(0).getModelNegotiationDesc()).isEqualTo(negotiationList.get(0).getModelNegotiationDesc());

        // verify
        verify(mockAdminNegotiationJpaService, times(1)).findNegotiationList(negotiationMap, pageRequest);
        verify(mockAdminNegotiationJpaService, atLeastOnce()).findNegotiationList(negotiationMap, pageRequest);
        verifyNoMoreInteractions(mockAdminNegotiationJpaService);

        InOrder inOrder = inOrder(mockAdminNegotiationJpaService);
        inOrder.verify(mockAdminNegotiationJpaService).findNegotiationList(negotiationMap, pageRequest);
    }

    @Test
    @DisplayName("모델 섭외 BDD 조회 테스트")
    void 모델섭외BDD조회테스트() {
        // given
        Map<String, Object> negotiationMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<AdminNegotiationDTO> negotiationList = new ArrayList<>();
        negotiationList.add(AdminNegotiationDTO.builder().modelIdx(adminModelEntity.getIdx())
                .modelNegotiationDesc("영화 프로젝트 참여 테스트 첫번째").modelNegotiationDate(now()).build());
        negotiationList.add(AdminNegotiationDTO.builder().modelIdx(adminModelEntity.getIdx())
                .modelNegotiationDesc("영화 프로젝트 참여 테스트 두번째").modelNegotiationDate(now()).build());

        Page<AdminNegotiationDTO> resultNegotiation = new PageImpl<>(negotiationList, pageRequest, negotiationList.size());

        // when
        given(mockAdminNegotiationJpaService.findNegotiationList(negotiationMap, pageRequest)).willReturn(resultNegotiation);
        Page<AdminNegotiationDTO> newModelNegotiationList = mockAdminNegotiationJpaService.findNegotiationList(negotiationMap, pageRequest);
        List<AdminNegotiationDTO> findNegotiationList = newModelNegotiationList.stream().collect(Collectors.toList());

        // then
        assertThat(findNegotiationList.get(0).getIdx()).isEqualTo(negotiationList.get(0).getIdx());
        assertThat(findNegotiationList.get(0).getModelNegotiationDesc()).isEqualTo(negotiationList.get(0).getModelNegotiationDesc());

        // verify
        then(mockAdminNegotiationJpaService).should(times(1)).findNegotiationList(negotiationMap, pageRequest);
        then(mockAdminNegotiationJpaService).should(atLeastOnce()).findNegotiationList(negotiationMap, pageRequest);
        then(mockAdminNegotiationJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 or 다음 모델 섭외 상세 조회 테스트")
    void 이전or다음모델섭외상세조회테스트() {
        // given
        adminNegotiationEntity = AdminNegotiationEntity.builder().idx(2L).build();

        // when
        adminNegotiationDTO = adminNegotiationJpaService.findOneNegotiation(adminNegotiationEntity.getIdx());

        // 이전 모델 섭외
        assertThat(adminNegotiationJpaService.findPrevOneNegotiation(adminNegotiationEntity.getIdx()).getIdx()).isEqualTo(1);
        // 다음 모델 섭외
        assertThat(adminNegotiationJpaService.findNextOneNegotiation(adminNegotiationEntity.getIdx()).getIdx()).isEqualTo(3);
    }

    @Test
    @DisplayName("이전 모델 섭외 상세 조회 Mockito 테스트")
    void 이전모델섭외상세조회Mockito테스트() {
        // given
        adminNegotiationEntity = AdminNegotiationEntity.builder().idx(2L).build();

        // when
        adminNegotiationDTO = adminNegotiationJpaService.findOneNegotiation(adminNegotiationEntity.getIdx());

        when(mockAdminNegotiationJpaService.findPrevOneNegotiation(adminNegotiationEntity.getIdx())).thenReturn(adminNegotiationDTO);
        AdminNegotiationDTO negotiationInfo = mockAdminNegotiationJpaService.findPrevOneNegotiation(adminNegotiationEntity.getIdx());

        // then
        assertThat(negotiationInfo.getIdx()).isEqualTo(1);

        // verify
        verify(mockAdminNegotiationJpaService, times(1)).findPrevOneNegotiation(adminNegotiationEntity.getIdx());
        verify(mockAdminNegotiationJpaService, atLeastOnce()).findPrevOneNegotiation(adminNegotiationEntity.getIdx());
        verifyNoMoreInteractions(mockAdminNegotiationJpaService);

        InOrder inOrder = inOrder(mockAdminNegotiationJpaService);
        inOrder.verify(mockAdminNegotiationJpaService).findPrevOneNegotiation(adminNegotiationEntity.getIdx());
    }

    @Test
    @DisplayName("이전 모델 섭외 상세 조회 BDD 테스트")
    void 이전모델섭외상세조회BDD테스트() {
        // given
        adminNegotiationEntity = AdminNegotiationEntity.builder().idx(2L).build();

        // when
        adminNegotiationDTO = adminNegotiationJpaService.findOneNegotiation(adminNegotiationEntity.getIdx());

        given(mockAdminNegotiationJpaService.findPrevOneNegotiation(adminNegotiationEntity.getIdx())).willReturn(adminNegotiationDTO);
        AdminNegotiationDTO negotiationInfo = mockAdminNegotiationJpaService.findPrevOneNegotiation(adminNegotiationEntity.getIdx());

        // then
        assertThat(negotiationInfo.getIdx()).isEqualTo(1);

        // verify
        then(mockAdminNegotiationJpaService).should(times(1)).findPrevOneNegotiation(adminNegotiationEntity.getIdx());
        then(mockAdminNegotiationJpaService).should(atLeastOnce()).findPrevOneNegotiation(adminNegotiationEntity.getIdx());
        then(mockAdminNegotiationJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 모델 섭외 상세 조회 Mockito 테스트")
    void 다음모델섭외상세조회Mockito테스트() {
        // given
        adminNegotiationEntity = AdminNegotiationEntity.builder().idx(2L).build();

        // when
        adminNegotiationDTO = adminNegotiationJpaService.findOneNegotiation(adminNegotiationEntity.getIdx());

        when(mockAdminNegotiationJpaService.findNextOneNegotiation(adminNegotiationEntity.getIdx())).thenReturn(adminNegotiationDTO);
        AdminNegotiationDTO negotiationInfo = mockAdminNegotiationJpaService.findNextOneNegotiation(adminNegotiationEntity.getIdx());

        // then
        assertThat(negotiationInfo.getIdx()).isEqualTo(3);

        // verify
        verify(mockAdminNegotiationJpaService, times(1)).findNextOneNegotiation(adminNegotiationEntity.getIdx());
        verify(mockAdminNegotiationJpaService, atLeastOnce()).findNextOneNegotiation(adminNegotiationEntity.getIdx());
        verifyNoMoreInteractions(mockAdminNegotiationJpaService);

        InOrder inOrder = inOrder(mockAdminNegotiationJpaService);
        inOrder.verify(mockAdminNegotiationJpaService).findNextOneNegotiation(adminNegotiationEntity.getIdx());
    }

    @Test
    @DisplayName("다음 모델 섭외 상세 조회 BDD 테스트")
    void 다음모델섭외상세조회BDD테스트() {
        // given
        AdminNegotiationDTO oneNegotiation = adminNegotiationJpaService.insertModelNegotiation(adminModelEntity, adminNegotiationEntity);

        // when
        adminNegotiationDTO = adminNegotiationJpaService.findOneNegotiation(oneNegotiation.getIdx());

        given(mockAdminNegotiationJpaService.findNextOneNegotiation(adminNegotiationEntity.getIdx())).willReturn(adminNegotiationDTO);
        AdminNegotiationDTO negotiationInfo = mockAdminNegotiationJpaService.findNextOneNegotiation(adminNegotiationEntity.getIdx());

        // then
        assertThat(negotiationInfo.getIdx()).isEqualTo(3);

        // verify
        then(mockAdminNegotiationJpaService).should(times(1)).findNextOneNegotiation(adminNegotiationEntity.getIdx());
        then(mockAdminNegotiationJpaService).should(atLeastOnce()).findNextOneNegotiation(adminNegotiationEntity.getIdx());
        then(mockAdminNegotiationJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델섭외등록Mockito테스트")
    void 모델섭외등록Mockito테스트() {
        // given
        AdminNegotiationDTO oneNegotiation = adminNegotiationJpaService.insertModelNegotiation(adminModelEntity, adminNegotiationEntity);

        // when
        when(mockAdminNegotiationJpaService.findOneNegotiation(oneNegotiation.getIdx())).thenReturn(oneNegotiation);
        AdminNegotiationDTO negotiationInfo = mockAdminNegotiationJpaService.findOneNegotiation(oneNegotiation.getIdx());

        // then
        assertThat(negotiationInfo.getModelIdx()).isEqualTo(adminModelEntity.getIdx());
        assertThat(negotiationInfo.getModelKorName()).isEqualTo("조찬희");
        assertThat(negotiationInfo.getModelNegotiationDesc()).isNotNull();

        // verify
        verify(mockAdminNegotiationJpaService, times(1)).findOneNegotiation(oneNegotiation.getIdx());
        verify(mockAdminNegotiationJpaService, atLeastOnce()).findOneNegotiation(oneNegotiation.getIdx());
        verifyNoMoreInteractions(mockAdminNegotiationJpaService);

        InOrder inOrder = inOrder(mockAdminNegotiationJpaService);
        inOrder.verify(mockAdminNegotiationJpaService).findOneNegotiation(oneNegotiation.getIdx());
    }

    @Test
    @DisplayName("모델섭외등록BDD테스트")
    void 모델섭외등록BDD테스트() {
        // given
        AdminNegotiationDTO oneNegotiation = adminNegotiationJpaService.insertModelNegotiation(adminModelEntity, adminNegotiationEntity);

        // when
        given(mockAdminNegotiationJpaService.findOneNegotiation(oneNegotiation.getIdx())).willReturn(oneNegotiation);
        AdminNegotiationDTO negotiationInfo = mockAdminNegotiationJpaService.findOneNegotiation(oneNegotiation.getIdx());

        // then
        assertThat(negotiationInfo.getModelIdx()).isEqualTo(oneNegotiation.getModelIdx());
        assertThat(negotiationInfo.getModelKorName()).isEqualTo("조찬희");
        assertThat(negotiationInfo.getModelNegotiationDesc()).isNotNull();

        // verify
        then(mockAdminNegotiationJpaService).should(times(1)).findOneNegotiation(oneNegotiation.getIdx());
        then(mockAdminNegotiationJpaService).should(atLeastOnce()).findOneNegotiation(oneNegotiation.getIdx());
        then(mockAdminNegotiationJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델섭외수정Mockito테스트")
    void 모델섭외수정Mockito테스트() {
        // given
        Long idx = adminNegotiationJpaService.insertModelNegotiation(adminModelEntity, adminNegotiationEntity).getIdx();

        adminNegotiationEntity = AdminNegotiationEntity.builder()
                .idx(idx)
                .modelKorName(adminModelEntity.getModelKorName())
                .adminModelEntity(adminModelEntity)
                .modelNegotiationDesc("섭외 수정 테스트")
                .modelNegotiationDate(now())
                .name("조찬희")
                .phone("010-1234-5678")
                .email("test@gmail.com")
                .visible("Y")
                .build();

        AdminNegotiationDTO adminNegotiationDTO = AdminNegotiationEntity.toDto(adminNegotiationEntity);

        adminNegotiationJpaService.updateModelNegotiation(idx, adminNegotiationEntity);

        em.flush();
        em.clear();

        // when
        when(mockAdminNegotiationJpaService.findOneNegotiation(adminNegotiationEntity.getIdx())).thenReturn(adminNegotiationDTO);
        AdminNegotiationDTO negotiationInfo = mockAdminNegotiationJpaService.findOneNegotiation(adminNegotiationEntity.getIdx());

        // then
        assertThat(negotiationInfo.getModelIdx()).isEqualTo(adminModelEntity.getIdx());
        assertThat(negotiationInfo.getModelNegotiationDesc()).isEqualTo("섭외 수정 테스트");

        // verify
        verify(mockAdminNegotiationJpaService, times(1)).findOneNegotiation(adminNegotiationEntity.getIdx());
        verify(mockAdminNegotiationJpaService, atLeastOnce()).findOneNegotiation(adminNegotiationEntity.getIdx());
        verifyNoMoreInteractions(mockAdminNegotiationJpaService);

        InOrder inOrder = inOrder(mockAdminNegotiationJpaService);
        inOrder.verify(mockAdminNegotiationJpaService).findOneNegotiation(adminNegotiationEntity.getIdx());
    }

    @Test
    @DisplayName("모델섭외수정BDD테스트")
    void 모델섭외수정BDD테스트() {
        // given
        Long idx = adminNegotiationJpaService.insertModelNegotiation(adminModelEntity, adminNegotiationEntity).getIdx();

        adminNegotiationEntity = AdminNegotiationEntity.builder()
                .idx(idx)
                .modelKorName(adminModelEntity.getModelKorName())
                .adminModelEntity(adminModelEntity)
                .modelNegotiationDesc("섭외 수정 테스트")
                .modelNegotiationDate(now())
                .name("조찬희")
                .phone("010-1234-5678")
                .email("test@gmail.com")
                .visible("Y")
                .build();

        AdminNegotiationDTO adminNegotiationDTO = AdminNegotiationEntity.toDto(adminNegotiationEntity);

        adminNegotiationJpaService.updateModelNegotiation(idx, adminNegotiationEntity);

        em.flush();
        em.clear();

        // when
        given(mockAdminNegotiationJpaService.findOneNegotiation(adminNegotiationEntity.getIdx())).willReturn(adminNegotiationDTO);
        AdminNegotiationDTO negotiationInfo = mockAdminNegotiationJpaService.findOneNegotiation(adminNegotiationEntity.getIdx());

        // then
        assertThat(negotiationInfo.getModelIdx()).isEqualTo(adminModelEntity.getIdx());
        assertThat(negotiationInfo.getModelNegotiationDesc()).isEqualTo("섭외 수정 테스트");

        // verify
        then(mockAdminNegotiationJpaService).should(times(1)).findOneNegotiation(adminNegotiationEntity.getIdx());
        then(mockAdminNegotiationJpaService).should(atLeastOnce()).findOneNegotiation(adminNegotiationEntity.getIdx());
        then(mockAdminNegotiationJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델섭외삭제Mockito테스트")
    void 모델섭외삭제Mockito테스트() {
        // given
        AdminNegotiationDTO oneNegotiation = adminNegotiationJpaService.insertModelNegotiation(adminModelEntity, adminNegotiationEntity);

        // when
        when(mockAdminNegotiationJpaService.findOneNegotiation(oneNegotiation.getIdx())).thenReturn(oneNegotiation);
        Long deleteIdx = adminNegotiationJpaService.deleteModelNegotiation(oneNegotiation.getIdx());

        // then
        assertThat(mockAdminNegotiationJpaService.findOneNegotiation(oneNegotiation.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockAdminNegotiationJpaService, times(1)).findOneNegotiation(oneNegotiation.getIdx());
        verify(mockAdminNegotiationJpaService, atLeastOnce()).findOneNegotiation(oneNegotiation.getIdx());
        verifyNoMoreInteractions(mockAdminNegotiationJpaService);

        InOrder inOrder = inOrder(mockAdminNegotiationJpaService);
        inOrder.verify(mockAdminNegotiationJpaService).findOneNegotiation(oneNegotiation.getIdx());
    }

    @Test
    @DisplayName("모델섭외삭제BDD테스트")
    void 모델섭외삭제BDD테스트() {
        // given
        AdminNegotiationDTO oneNegotiation = adminNegotiationJpaService.insertModelNegotiation(adminModelEntity, adminNegotiationEntity);

        // when
        given(mockAdminNegotiationJpaService.findOneNegotiation(oneNegotiation.getIdx())).willReturn(oneNegotiation);
        Long deleteIdx = adminNegotiationJpaService.deleteModelNegotiation(oneNegotiation.getIdx());

        // then
        assertThat(mockAdminNegotiationJpaService.findOneNegotiation(oneNegotiation.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        then(mockAdminNegotiationJpaService).should(times(1)).findOneNegotiation(oneNegotiation.getIdx());
        then(mockAdminNegotiationJpaService).should(atLeastOnce()).findOneNegotiation(oneNegotiation.getIdx());
        then(mockAdminNegotiationJpaService).shouldHaveNoMoreInteractions();
    }
}