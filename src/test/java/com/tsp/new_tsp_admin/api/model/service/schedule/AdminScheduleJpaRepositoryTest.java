package com.tsp.new_tsp_admin.api.model.service.schedule;

import com.tsp.new_tsp_admin.api.domain.model.AdminModelDTO;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import com.tsp.new_tsp_admin.api.domain.model.CareerJson;
import com.tsp.new_tsp_admin.api.domain.model.agency.AdminAgencyDTO;
import com.tsp.new_tsp_admin.api.domain.model.agency.AdminAgencyEntity;
import com.tsp.new_tsp_admin.api.domain.model.schedule.AdminScheduleDTO;
import com.tsp.new_tsp_admin.api.domain.model.schedule.AdminScheduleEntity;
import com.tsp.new_tsp_admin.api.model.mapper.ModelMapper;
import com.tsp.new_tsp_admin.api.model.mapper.agency.AgencyMapper;
import com.tsp.new_tsp_admin.api.model.mapper.schedule.ScheduleMapper;
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
import java.util.*;

import static java.time.LocalDateTime.*;
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
class AdminScheduleJpaRepositoryTest {
    @Mock private AdminScheduleJpaRepository mockAdminScheduleJpaRepository;
    private final AdminScheduleJpaRepository adminScheduleJpaRepository;
    private final EntityManager em;

    private AdminModelEntity adminModelEntity;
    private AdminModelDTO adminModelDTO;
    private AdminScheduleEntity adminScheduleEntity;
    private AdminScheduleDTO adminScheduleDTO;
    private AdminAgencyEntity adminAgencyEntity;
    private AdminAgencyDTO adminAgencyDTO;

    void createModelAndSchedule() {
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

        adminScheduleEntity = AdminScheduleEntity.builder()
                .modelIdx(adminModelEntity.getIdx())
                .modelSchedule("스케줄 테스트")
                .modelScheduleTime(now())
                .visible("Y")
                .build();

        adminScheduleDTO = ScheduleMapper.INSTANCE.toDto(adminScheduleEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createModelAndSchedule();
    }

    @Test
    @DisplayName("모델 스케줄 리스트 조회 테스트")
    void 모델스케줄리스트조회테스트() {
        // given
        Map<String, Object> scheduleMap = new HashMap<>();
        scheduleMap.put("searchKeyword", "김예영");
        scheduleMap.put("jpaStartPage", 0);
        scheduleMap.put("size", 100);

        // then
        assertThat(adminScheduleJpaRepository.findModelScheduleList(scheduleMap)).isNotEmpty();

        Map<String, Object> lastMonthScheduleMap = new HashMap<>();
        lastMonthScheduleMap.put("searchStartTime", of(now().getYear(), LocalDate.now().minusMonths(1).getMonth(), 1, 0, 0, 0, 0));
        lastMonthScheduleMap.put("searchEndTime", of(now().getYear(), LocalDate.now().minusMonths(1).getMonth(), 30, 23, 59, 59));
        lastMonthScheduleMap.put("jpaStartPage", 0);
        lastMonthScheduleMap.put("size", 100);

        // then
        assertThat(adminScheduleJpaRepository.findModelScheduleList(lastMonthScheduleMap)).isEmpty();

        Map<String, Object> currentScheduleMap = new HashMap<>();
        currentScheduleMap.put("searchStartTime", of(now().getYear(), LocalDate.now().getMonth(), 1, 0, 0, 0, 0));
        currentScheduleMap.put("searchEndTime", of(now().getYear(), LocalDate.now().getMonth(), 30, 23, 59, 59));
        currentScheduleMap.put("jpaStartPage", 0);
        currentScheduleMap.put("size", 100);

        // then
        assertThat(adminScheduleJpaRepository.findModelScheduleList(currentScheduleMap)).isNotEmpty();
    }

    @Test
    @DisplayName("모델 스케줄 Mockito 조회 테스트")
    void 모델스케줄Mockito조회테스트() {
        // given
        Map<String, Object> scheduleMap = new HashMap<>();
        scheduleMap.put("jpaStartPage", 1);
        scheduleMap.put("size", 3);

        List<AdminScheduleDTO> scheduleList = new ArrayList<>();
        scheduleList.add(AdminScheduleDTO.builder().modelIdx(adminModelEntity.getIdx())
                .modelSchedule("스케줄 테스트").modelScheduleTime(now()).build());
        scheduleList.add(AdminScheduleDTO.builder().modelIdx(adminModelEntity.getIdx())
                .modelSchedule("스케줄 테스트 두번째").modelScheduleTime(now()).build());

        List<AdminModelDTO> modelScheduleList = new ArrayList<>();
        modelScheduleList.add(AdminModelDTO.builder().idx(3L).categoryCd(1).modelKorName("조찬희")
                .modelSchedule(scheduleList).build());

        // when
        when(mockAdminScheduleJpaRepository.findModelScheduleList(scheduleMap)).thenReturn(modelScheduleList);
        List<AdminModelDTO> newModelScheduleList = mockAdminScheduleJpaRepository.findModelScheduleList(scheduleMap);

        // then
        assertThat(newModelScheduleList.get(0).getIdx()).isEqualTo(modelScheduleList.get(0).getIdx());
        assertThat(newModelScheduleList.get(0).getModelKorName()).isEqualTo(modelScheduleList.get(0).getModelKorName());
        assertThat(newModelScheduleList.get(0).getModelSchedule().get(0).getModelSchedule()).isEqualTo(modelScheduleList.get(0).getModelSchedule().get(0).getModelSchedule());

        // verify
        verify(mockAdminScheduleJpaRepository, times(1)).findModelScheduleList(scheduleMap);
        verify(mockAdminScheduleJpaRepository, atLeastOnce()).findModelScheduleList(scheduleMap);
        verifyNoMoreInteractions(mockAdminScheduleJpaRepository);

        InOrder inOrder = inOrder(mockAdminScheduleJpaRepository);
        inOrder.verify(mockAdminScheduleJpaRepository).findModelScheduleList(scheduleMap);
    }

    @Test
    @DisplayName("모델 스케줄 BDD 조회 테스트")
    void 모델스케줄BDD조회테스트() {
        // given
        Map<String, Object> scheduleMap = new HashMap<>();
        scheduleMap.put("jpaStartPage", 1);
        scheduleMap.put("size", 3);

        List<AdminScheduleDTO> scheduleList = new ArrayList<>();
        scheduleList.add(AdminScheduleDTO.builder().modelIdx(adminModelEntity.getIdx())
                .modelSchedule("스케줄 테스트").modelScheduleTime(now()).build());
        scheduleList.add(AdminScheduleDTO.builder().modelIdx(adminModelEntity.getIdx())
                .modelSchedule("스케줄 테스트 두번째").modelScheduleTime(now()).build());

        List<AdminModelDTO> modelScheduleList = new ArrayList<>();
        modelScheduleList.add(AdminModelDTO.builder().idx(3L).categoryCd(1).modelKorName("조찬희")
                .modelSchedule(scheduleList).build());

        // when
        given(mockAdminScheduleJpaRepository.findModelScheduleList(scheduleMap)).willReturn(modelScheduleList);
        List<AdminModelDTO> newModelScheduleList = mockAdminScheduleJpaRepository.findModelScheduleList(scheduleMap);

        // then
        assertThat(newModelScheduleList.get(0).getIdx()).isEqualTo(modelScheduleList.get(0).getIdx());
        assertThat(newModelScheduleList.get(0).getModelKorName()).isEqualTo(modelScheduleList.get(0).getModelKorName());
        assertThat(newModelScheduleList.get(0).getModelSchedule().get(0).getModelSchedule()).isEqualTo(modelScheduleList.get(0).getModelSchedule().get(0).getModelSchedule());

        // verify
        then(mockAdminScheduleJpaRepository).should(times(1)).findModelScheduleList(scheduleMap);
        then(mockAdminScheduleJpaRepository).should(atLeastOnce()).findModelScheduleList(scheduleMap);
        then(mockAdminScheduleJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델 스케줄 Mockito 검색 및 날짜 조회 테스트")
    void 모델스케줄Mockito검색및날짜조회테스트() {
        // given
        Map<String, Object> scheduleMap = new HashMap<>();
        scheduleMap.put("jpaStartPage", 1);
        scheduleMap.put("size", 3);

        List<AdminScheduleDTO> scheduleList = new ArrayList<>();
        scheduleList.add(AdminScheduleDTO.builder().modelIdx(adminModelEntity.getIdx())
                .modelSchedule("스케줄 테스트").modelScheduleTime(now()).build());
        scheduleList.add(AdminScheduleDTO.builder().modelIdx(adminModelEntity.getIdx())
                .modelSchedule("스케줄 테스트 두번째").modelScheduleTime(now()).build());

        List<AdminModelDTO> modelScheduleList = new ArrayList<>();
        modelScheduleList.add(AdminModelDTO.builder().idx(3L).categoryCd(1).modelKorName("조찬희")
                .modelSchedule(scheduleList).build());

        // when
        when(mockAdminScheduleJpaRepository.findModelScheduleList(scheduleMap)).thenReturn(modelScheduleList);
        List<AdminModelDTO> newModelScheduleList = mockAdminScheduleJpaRepository.findModelScheduleList(scheduleMap);

        // then
        assertThat(newModelScheduleList.get(0).getIdx()).isEqualTo(modelScheduleList.get(0).getIdx());
        assertThat(newModelScheduleList.get(0).getModelKorName()).isEqualTo(modelScheduleList.get(0).getModelKorName());
        assertThat(newModelScheduleList.get(0).getModelSchedule().get(0).getModelSchedule()).isEqualTo(modelScheduleList.get(0).getModelSchedule().get(0).getModelSchedule());

        // verify
        verify(mockAdminScheduleJpaRepository, times(1)).findModelScheduleList(scheduleMap);
        verify(mockAdminScheduleJpaRepository, atLeastOnce()).findModelScheduleList(scheduleMap);
        verifyNoMoreInteractions(mockAdminScheduleJpaRepository);

        InOrder inOrder = inOrder(mockAdminScheduleJpaRepository);
        inOrder.verify(mockAdminScheduleJpaRepository).findModelScheduleList(scheduleMap);
    }

    @Test
    @DisplayName("모델스케줄상세Mockito조회테스트")
    void 모델스케줄상세Mockito조회테스트() {
        // given
        AdminScheduleEntity adminScheduleEntity = AdminScheduleEntity.builder()
                .idx(1L)
                .modelIdx(adminModelEntity.getIdx())
                .modelSchedule("스케줄 테스트")
                .modelScheduleTime(now())
                .visible("Y")
                .build();

        adminScheduleDTO = ScheduleMapper.INSTANCE.toDto(adminScheduleEntity);

        // when
        when(mockAdminScheduleJpaRepository.findOneSchedule(adminScheduleEntity.getIdx())).thenReturn(adminScheduleDTO);
        AdminScheduleDTO scheduleInfo = mockAdminScheduleJpaRepository.findOneSchedule(adminScheduleEntity.getIdx());

        // then
        assertThat(scheduleInfo.getIdx()).isEqualTo(1);
        assertThat(scheduleInfo.getModelIdx()).isEqualTo(adminModelEntity.getIdx());
        assertThat(scheduleInfo.getModelSchedule()).isEqualTo("스케줄 테스트");
        assertThat(scheduleInfo.getVisible()).isEqualTo("Y");

        // verify
        verify(mockAdminScheduleJpaRepository, times(1)).findOneSchedule(adminScheduleEntity.getIdx());
        verify(mockAdminScheduleJpaRepository, atLeastOnce()).findOneSchedule(adminScheduleEntity.getIdx());
        verifyNoMoreInteractions(mockAdminScheduleJpaRepository);

        InOrder inOrder = inOrder(mockAdminScheduleJpaRepository);
        inOrder.verify(mockAdminScheduleJpaRepository).findOneSchedule(adminScheduleEntity.getIdx());
    }

    @Test
    @DisplayName("모델스케줄상세BDD조회테스트")
    void 모델스케줄상세BDD조회테스트() {
        // given
        AdminScheduleEntity adminScheduleEntity = AdminScheduleEntity.builder()
                .idx(1L)
                .modelIdx(adminModelEntity.getIdx())
                .modelSchedule("스케줄 테스트")
                .modelScheduleTime(now())
                .visible("Y")
                .build();

        adminScheduleDTO = ScheduleMapper.INSTANCE.toDto(adminScheduleEntity);

        // when
        given(mockAdminScheduleJpaRepository.findOneSchedule(adminScheduleEntity.getIdx())).willReturn(adminScheduleDTO);
        AdminScheduleDTO scheduleInfo = mockAdminScheduleJpaRepository.findOneSchedule(adminScheduleEntity.getIdx());

        // then
        assertThat(scheduleInfo.getIdx()).isEqualTo(1);
        assertThat(scheduleInfo.getModelIdx()).isEqualTo(adminModelEntity.getIdx());
        assertThat(scheduleInfo.getModelSchedule()).isEqualTo("스케줄 테스트");
        assertThat(scheduleInfo.getVisible()).isEqualTo("Y");

        // verify
        then(mockAdminScheduleJpaRepository).should(times(1)).findOneSchedule(adminScheduleEntity.getIdx());
        then(mockAdminScheduleJpaRepository).should(atLeastOnce()).findOneSchedule(adminScheduleEntity.getIdx());
        then(mockAdminScheduleJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 or 다음 모델 스케줄 상세 조회 테스트")
    void 이전or다음모델스케줄상세조회테스트() {
        // given
        adminScheduleEntity = AdminScheduleEntity.builder().idx(2L).build();

        // when
        adminScheduleDTO = adminScheduleJpaRepository.findOneSchedule(adminScheduleEntity.getIdx());

        // 이전 모델 섭외
        assertThat(adminScheduleJpaRepository.findPrevOneSchedule(adminScheduleEntity.getIdx()).getIdx()).isEqualTo(1);
        // 다음 모델 섭외
        assertThat(adminScheduleJpaRepository.findNextOneSchedule(adminScheduleEntity.getIdx()).getIdx()).isEqualTo(3);
    }

    @Test
    @DisplayName("이전 모델 스케줄 상세 조회 Mockito 테스트")
    void 이전모델스케줄상세조회Mockito테스트() {
        // given
        adminScheduleEntity = AdminScheduleEntity.builder().idx(2L).build();

        // when
        adminScheduleDTO = adminScheduleJpaRepository.findOneSchedule(adminScheduleEntity.getIdx());

        when(mockAdminScheduleJpaRepository.findPrevOneSchedule(adminScheduleEntity.getIdx())).thenReturn(adminScheduleDTO);
        AdminScheduleDTO scheduleInfo = mockAdminScheduleJpaRepository.findPrevOneSchedule(adminScheduleEntity.getIdx());

        // then
        assertThat(scheduleInfo.getIdx()).isEqualTo(1);

        // verify
        verify(mockAdminScheduleJpaRepository, times(1)).findPrevOneSchedule(adminScheduleEntity.getIdx());
        verify(mockAdminScheduleJpaRepository, atLeastOnce()).findPrevOneSchedule(adminScheduleEntity.getIdx());
        verifyNoMoreInteractions(mockAdminScheduleJpaRepository);

        InOrder inOrder = inOrder(mockAdminScheduleJpaRepository);
        inOrder.verify(mockAdminScheduleJpaRepository).findPrevOneSchedule(adminScheduleEntity.getIdx());
    }

    @Test
    @DisplayName("이전 모델 스케줄 상세 조회 BDD 테스트")
    void 이전모델스케줄상세조회BDD테스트() {
        // given
        adminScheduleEntity = AdminScheduleEntity.builder().idx(2L).build();

        // when
        adminScheduleDTO = adminScheduleJpaRepository.findOneSchedule(adminScheduleEntity.getIdx());

        given(mockAdminScheduleJpaRepository.findPrevOneSchedule(adminScheduleEntity.getIdx())).willReturn(adminScheduleDTO);
        AdminScheduleDTO scheduleInfo = mockAdminScheduleJpaRepository.findPrevOneSchedule(adminScheduleEntity.getIdx());

        // then
        assertThat(scheduleInfo.getIdx()).isEqualTo(1);

        // verify
        then(mockAdminScheduleJpaRepository).should(times(1)).findPrevOneSchedule(adminScheduleEntity.getIdx());
        then(mockAdminScheduleJpaRepository).should(atLeastOnce()).findPrevOneSchedule(adminScheduleEntity.getIdx());
        then(mockAdminScheduleJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 모델 스케줄 상세 조회 Mockito 테스트")
    void 다음모델스케줄상세조회Mockito테스트() {
        // given
        adminScheduleEntity = AdminScheduleEntity.builder().idx(2L).build();

        // when
        adminScheduleDTO = adminScheduleJpaRepository.findOneSchedule(adminScheduleEntity.getIdx());

        when(mockAdminScheduleJpaRepository.findNextOneSchedule(adminScheduleEntity.getIdx())).thenReturn(adminScheduleDTO);
        AdminScheduleDTO scheduleInfo = mockAdminScheduleJpaRepository.findNextOneSchedule(adminScheduleEntity.getIdx());

        // then
        assertThat(scheduleInfo.getIdx()).isEqualTo(3);

        // verify
        verify(mockAdminScheduleJpaRepository, times(1)).findNextOneSchedule(adminScheduleEntity.getIdx());
        verify(mockAdminScheduleJpaRepository, atLeastOnce()).findNextOneSchedule(adminScheduleEntity.getIdx());
        verifyNoMoreInteractions(mockAdminScheduleJpaRepository);

        InOrder inOrder = inOrder(mockAdminScheduleJpaRepository);
        inOrder.verify(mockAdminScheduleJpaRepository).findNextOneSchedule(adminScheduleEntity.getIdx());
    }

    @Test
    @DisplayName("다음 모델 스케줄 상세 조회 BDD 테스트")
    void 다음모델스케줄상세조회BDD테스트() {
        // given
        adminScheduleEntity = AdminScheduleEntity.builder().idx(2L).build();

        // when
        adminScheduleDTO = adminScheduleJpaRepository.findOneSchedule(adminScheduleEntity.getIdx());

        given(mockAdminScheduleJpaRepository.findNextOneSchedule(adminScheduleEntity.getIdx())).willReturn(adminScheduleDTO);
        AdminScheduleDTO scheduleInfo = mockAdminScheduleJpaRepository.findNextOneSchedule(adminScheduleEntity.getIdx());

        // then
        assertThat(scheduleInfo.getIdx()).isEqualTo(3);

        // verify
        then(mockAdminScheduleJpaRepository).should(times(1)).findNextOneSchedule(adminScheduleEntity.getIdx());
        then(mockAdminScheduleJpaRepository).should(atLeastOnce()).findNextOneSchedule(adminScheduleEntity.getIdx());
        then(mockAdminScheduleJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델스케줄등록Mockito테스트")
    void 모델스케줄등록Mockito테스트() {
        // given
        adminScheduleJpaRepository.insertSchedule(adminScheduleEntity);

        // when
        when(mockAdminScheduleJpaRepository.findOneSchedule(adminScheduleEntity.getIdx())).thenReturn(adminScheduleDTO);
        AdminScheduleDTO scheduleInfo = mockAdminScheduleJpaRepository.findOneSchedule(adminScheduleEntity.getIdx());

        // then
        assertThat(scheduleInfo.getModelIdx()).isEqualTo(adminModelEntity.getIdx());
        assertThat(scheduleInfo.getModelSchedule()).isEqualTo("스케줄 테스트");
        assertThat(scheduleInfo.getModelScheduleTime()).isNotNull();

        // verify
        verify(mockAdminScheduleJpaRepository, times(1)).findOneSchedule(adminScheduleEntity.getIdx());
        verify(mockAdminScheduleJpaRepository, atLeastOnce()).findOneSchedule(adminScheduleEntity.getIdx());
        verifyNoMoreInteractions(mockAdminScheduleJpaRepository);

        InOrder inOrder = inOrder(mockAdminScheduleJpaRepository);
        inOrder.verify(mockAdminScheduleJpaRepository).findOneSchedule(adminScheduleEntity.getIdx());
    }

    @Test
    @DisplayName("모델스케줄등록BDD테스트")
    void 모델스케줄등록BDD테스트() {
        // given
        adminScheduleJpaRepository.insertSchedule(adminScheduleEntity);

        // when
        given(mockAdminScheduleJpaRepository.findOneSchedule(adminScheduleEntity.getIdx())).willReturn(adminScheduleDTO);
        AdminScheduleDTO scheduleInfo = mockAdminScheduleJpaRepository.findOneSchedule(adminScheduleEntity.getIdx());

        // then
        assertThat(scheduleInfo.getModelIdx()).isEqualTo(adminModelEntity.getIdx());
        assertThat(scheduleInfo.getModelSchedule()).isEqualTo("스케줄 테스트");
        assertThat(scheduleInfo.getModelScheduleTime()).isNotNull();

        // verify
        then(mockAdminScheduleJpaRepository).should(times(1)).findOneSchedule(adminScheduleEntity.getIdx());
        then(mockAdminScheduleJpaRepository).should(atLeastOnce()).findOneSchedule(adminScheduleEntity.getIdx());
        then(mockAdminScheduleJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델스케줄수정Mockito테스트")
    void 모델스케줄수정Mockito테스트() {
        // given
        Long idx = adminScheduleJpaRepository.insertSchedule(adminScheduleEntity).getIdx();

        adminScheduleEntity = AdminScheduleEntity.builder()
                .idx(idx)
                .modelIdx(adminModelEntity.getIdx())
                .modelSchedule("스케줄 수정 테스트")
                .modelScheduleTime(now())
                .visible("Y")
                .build();

        AdminScheduleDTO adminScheduleDTO = ScheduleMapper.INSTANCE.toDto(adminScheduleEntity);

        adminScheduleJpaRepository.updateSchedule(adminScheduleEntity);

        // when
        when(mockAdminScheduleJpaRepository.findOneSchedule(adminScheduleEntity.getIdx())).thenReturn(adminScheduleDTO);
        AdminScheduleDTO scheduleInfo = mockAdminScheduleJpaRepository.findOneSchedule(adminScheduleEntity.getIdx());

        // then
        assertThat(scheduleInfo.getModelIdx()).isEqualTo(adminModelEntity.getIdx());
        assertThat(scheduleInfo.getModelSchedule()).isEqualTo("스케줄 수정 테스트");

        // verify
        verify(mockAdminScheduleJpaRepository, times(1)).findOneSchedule(adminScheduleEntity.getIdx());
        verify(mockAdminScheduleJpaRepository, atLeastOnce()).findOneSchedule(adminScheduleEntity.getIdx());
        verifyNoMoreInteractions(mockAdminScheduleJpaRepository);

        InOrder inOrder = inOrder(mockAdminScheduleJpaRepository);
        inOrder.verify(mockAdminScheduleJpaRepository).findOneSchedule(adminScheduleEntity.getIdx());
    }

    @Test
    @DisplayName("모델스케줄수정BDD테스트")
    void 모델스케줄수정BDD테스트() {
        // given
        Long idx = adminScheduleJpaRepository.insertSchedule(adminScheduleEntity).getIdx();

        adminScheduleEntity = AdminScheduleEntity.builder()
                .idx(idx)
                .modelIdx(adminModelEntity.getIdx())
                .modelSchedule("스케줄 수정 테스트")
                .modelScheduleTime(now())
                .visible("Y")
                .build();

        AdminScheduleDTO adminScheduleDTO = ScheduleMapper.INSTANCE.toDto(adminScheduleEntity);

        adminScheduleJpaRepository.updateSchedule(adminScheduleEntity);

        // when
        given(mockAdminScheduleJpaRepository.findOneSchedule(adminScheduleEntity.getIdx())).willReturn(adminScheduleDTO);
        AdminScheduleDTO scheduleInfo = mockAdminScheduleJpaRepository.findOneSchedule(adminScheduleEntity.getIdx());

        // then
        assertThat(scheduleInfo.getModelIdx()).isEqualTo(adminModelEntity.getIdx());
        assertThat(scheduleInfo.getModelSchedule()).isEqualTo("스케줄 수정 테스트");

        // verify
        then(mockAdminScheduleJpaRepository).should(times(1)).findOneSchedule(adminScheduleEntity.getIdx());
        then(mockAdminScheduleJpaRepository).should(atLeastOnce()).findOneSchedule(adminScheduleEntity.getIdx());
        then(mockAdminScheduleJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델스케줄삭제Mockito테스트")
    void 모델스케줄삭제Mockito테스트() {
        // given
        em.persist(adminScheduleEntity);
        adminScheduleDTO = ScheduleMapper.INSTANCE.toDto(adminScheduleEntity);

        // when
        when(mockAdminScheduleJpaRepository.findOneSchedule(adminScheduleEntity.getIdx())).thenReturn(adminScheduleDTO);
        Long deleteIdx = adminScheduleJpaRepository.deleteSchedule(adminScheduleEntity.getIdx());

        // then
        assertThat(mockAdminScheduleJpaRepository.findOneSchedule(adminScheduleEntity.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockAdminScheduleJpaRepository, times(1)).findOneSchedule(adminScheduleEntity.getIdx());
        verify(mockAdminScheduleJpaRepository, atLeastOnce()).findOneSchedule(adminScheduleEntity.getIdx());
        verifyNoMoreInteractions(mockAdminScheduleJpaRepository);

        InOrder inOrder = inOrder(mockAdminScheduleJpaRepository);
        inOrder.verify(mockAdminScheduleJpaRepository).findOneSchedule(adminScheduleEntity.getIdx());
    }

    @Test
    @DisplayName("모델스케줄삭제BDD테스트")
    void 모델스케줄삭제BDD테스트() {
        // given
        em.persist(adminScheduleEntity);
        adminScheduleDTO = ScheduleMapper.INSTANCE.toDto(adminScheduleEntity);

        // when
        given(mockAdminScheduleJpaRepository.findOneSchedule(adminScheduleEntity.getIdx())).willReturn(adminScheduleDTO);
        Long deleteIdx = adminScheduleJpaRepository.deleteSchedule(adminScheduleEntity.getIdx());

        // then
        assertThat(mockAdminScheduleJpaRepository.findOneSchedule(adminScheduleEntity.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        then(mockAdminScheduleJpaRepository).should(times(1)).findOneSchedule(adminScheduleEntity.getIdx());
        then(mockAdminScheduleJpaRepository).should(atLeastOnce()).findOneSchedule(adminScheduleEntity.getIdx());
        then(mockAdminScheduleJpaRepository).shouldHaveNoMoreInteractions();
    }
}