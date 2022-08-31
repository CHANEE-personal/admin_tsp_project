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

import java.util.*;

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
                .modelScheduleTime(new Date())
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
    @DisplayName("모델 스케줄 Mockito 조회 테스트")
    void 모델스케줄Mockito조회테스트() {
        // given
        Map<String, Object> scheduleMap = new HashMap<>();
        scheduleMap.put("jpaStartPage", 1);
        scheduleMap.put("size", 3);

        List<AdminScheduleDTO> scheduleList = new ArrayList<>();
        scheduleList.add(AdminScheduleDTO.builder().idx(1).modelIdx(adminModelEntity.getIdx())
                .modelSchedule("스케줄 테스트").modelScheduleTime(new Date()).build());

        // when
        when(mockAdminScheduleJpaRepository.findScheduleList(scheduleMap)).thenReturn(scheduleList);
        List<AdminScheduleDTO> newScheduleList = mockAdminScheduleJpaRepository.findScheduleList(scheduleMap);

        // then
        assertThat(newScheduleList.get(0).getIdx()).isEqualTo(scheduleList.get(0).getIdx());
        assertThat(newScheduleList.get(0).getModelIdx()).isEqualTo(scheduleList.get(0).getModelIdx());
        assertThat(newScheduleList.get(0).getModelSchedule()).isEqualTo(scheduleList.get(0).getModelSchedule());
        assertThat(newScheduleList.get(0).getModelScheduleTime()).isEqualTo(scheduleList.get(0).getModelScheduleTime());

        // verify
        verify(mockAdminScheduleJpaRepository, times(1)).findScheduleList(scheduleMap);
        verify(mockAdminScheduleJpaRepository, atLeastOnce()).findScheduleList(scheduleMap);
        verifyNoMoreInteractions(mockAdminScheduleJpaRepository);

        InOrder inOrder = inOrder(mockAdminScheduleJpaRepository);
        inOrder.verify(mockAdminScheduleJpaRepository).findScheduleList(scheduleMap);
    }

    @Test
    @DisplayName("모델 스케줄 BDD 조회 테스트")
    void 모델스케줄BDD조회테스트() {
        // given
        Map<String, Object> scheduleMap = new HashMap<>();
        scheduleMap.put("jpaStartPage", 1);
        scheduleMap.put("size", 3);

        List<AdminScheduleDTO> scheduleList = new ArrayList<>();
        scheduleList.add(AdminScheduleDTO.builder().idx(1).modelIdx(adminModelEntity.getIdx())
                .modelSchedule("스케줄 테스트").modelScheduleTime(new Date()).build());

        // when
        given(mockAdminScheduleJpaRepository.findScheduleList(scheduleMap)).willReturn(scheduleList);
        List<AdminScheduleDTO> newScheduleList = mockAdminScheduleJpaRepository.findScheduleList(scheduleMap);

        // then
        assertThat(newScheduleList.get(0).getIdx()).isEqualTo(scheduleList.get(0).getIdx());
        assertThat(newScheduleList.get(0).getModelIdx()).isEqualTo(scheduleList.get(0).getModelIdx());
        assertThat(newScheduleList.get(0).getModelSchedule()).isEqualTo(scheduleList.get(0).getModelSchedule());
        assertThat(newScheduleList.get(0).getModelScheduleTime()).isEqualTo(scheduleList.get(0).getModelScheduleTime());

        // verify
        then(mockAdminScheduleJpaRepository).should(times(1)).findScheduleList(scheduleMap);
        then(mockAdminScheduleJpaRepository).should(atLeastOnce()).findScheduleList(scheduleMap);
        then(mockAdminScheduleJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델스케줄상세Mockito조회테스트")
    void 모델스케줄상세Mockito조회테스트() {
        // given
        AdminScheduleEntity adminScheduleEntity = AdminScheduleEntity.builder()
                .idx(1)
                .modelIdx(adminModelEntity.getIdx())
                .modelSchedule("스케줄 테스트")
                .modelScheduleTime(new Date())
                .visible("Y")
                .build();

        adminScheduleDTO = ScheduleMapper.INSTANCE.toDto(adminScheduleEntity);

        // when
        when(mockAdminScheduleJpaRepository.findOneSchedule(adminScheduleEntity)).thenReturn(adminScheduleDTO);
        AdminScheduleDTO scheduleInfo = mockAdminScheduleJpaRepository.findOneSchedule(adminScheduleEntity);

        // then
        assertThat(scheduleInfo.getIdx()).isEqualTo(1);
        assertThat(scheduleInfo.getModelIdx()).isEqualTo(adminModelEntity.getIdx());
        assertThat(scheduleInfo.getModelSchedule()).isEqualTo("스케줄 테스트");
        assertThat(scheduleInfo.getVisible()).isEqualTo("Y");

        // verify
        verify(mockAdminScheduleJpaRepository, times(1)).findOneSchedule(adminScheduleEntity);
        verify(mockAdminScheduleJpaRepository, atLeastOnce()).findOneSchedule(adminScheduleEntity);
        verifyNoMoreInteractions(mockAdminScheduleJpaRepository);

        InOrder inOrder = inOrder(mockAdminScheduleJpaRepository);
        inOrder.verify(mockAdminScheduleJpaRepository).findOneSchedule(adminScheduleEntity);
    }

    @Test
    @DisplayName("모델스케줄상세BDD조회테스트")
    void 모델스케줄상세BDD조회테스트() {
        // given
        AdminScheduleEntity adminScheduleEntity = AdminScheduleEntity.builder()
                .idx(1)
                .modelIdx(adminModelEntity.getIdx())
                .modelSchedule("스케줄 테스트")
                .modelScheduleTime(new Date())
                .visible("Y")
                .build();

        adminScheduleDTO = ScheduleMapper.INSTANCE.toDto(adminScheduleEntity);

        // when
        given(mockAdminScheduleJpaRepository.findOneSchedule(adminScheduleEntity)).willReturn(adminScheduleDTO);
        AdminScheduleDTO scheduleInfo = mockAdminScheduleJpaRepository.findOneSchedule(adminScheduleEntity);

        // then
        assertThat(scheduleInfo.getIdx()).isEqualTo(1);
        assertThat(scheduleInfo.getModelIdx()).isEqualTo(adminModelEntity.getIdx());
        assertThat(scheduleInfo.getModelSchedule()).isEqualTo("스케줄 테스트");
        assertThat(scheduleInfo.getVisible()).isEqualTo("Y");

        // verify
        then(mockAdminScheduleJpaRepository).should(times(1)).findOneSchedule(adminScheduleEntity);
        then(mockAdminScheduleJpaRepository).should(atLeastOnce()).findOneSchedule(adminScheduleEntity);
        then(mockAdminScheduleJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델스케줄등록Mockito테스트")
    void 모델스케줄등록Mockito테스트() {
        // given
        adminScheduleJpaRepository.insertSchedule(adminScheduleEntity);

        // when
        when(mockAdminScheduleJpaRepository.findOneSchedule(adminScheduleEntity)).thenReturn(adminScheduleDTO);
        AdminScheduleDTO scheduleInfo = mockAdminScheduleJpaRepository.findOneSchedule(adminScheduleEntity);

        // then
        assertThat(scheduleInfo.getModelIdx()).isEqualTo(adminModelEntity.getIdx());
        assertThat(scheduleInfo.getModelSchedule()).isEqualTo("스케줄 테스트");
        assertThat(scheduleInfo.getModelScheduleTime()).isNotNull();

        // verify
        verify(mockAdminScheduleJpaRepository, times(1)).findOneSchedule(adminScheduleEntity);
        verify(mockAdminScheduleJpaRepository, atLeastOnce()).findOneSchedule(adminScheduleEntity);
        verifyNoMoreInteractions(mockAdminScheduleJpaRepository);

        InOrder inOrder = inOrder(mockAdminScheduleJpaRepository);
        inOrder.verify(mockAdminScheduleJpaRepository).findOneSchedule(adminScheduleEntity);
    }

    @Test
    @DisplayName("모델스케줄등록BDD테스트")
    void 모델스케줄등록BDD테스트() {
        // given
        adminScheduleJpaRepository.insertSchedule(adminScheduleEntity);

        // when
        given(mockAdminScheduleJpaRepository.findOneSchedule(adminScheduleEntity)).willReturn(adminScheduleDTO);
        AdminScheduleDTO scheduleInfo = mockAdminScheduleJpaRepository.findOneSchedule(adminScheduleEntity);

        // then
        assertThat(scheduleInfo.getModelIdx()).isEqualTo(adminModelEntity.getIdx());
        assertThat(scheduleInfo.getModelSchedule()).isEqualTo("스케줄 테스트");
        assertThat(scheduleInfo.getModelScheduleTime()).isNotNull();

        // verify
        then(mockAdminScheduleJpaRepository).should(times(1)).findOneSchedule(adminScheduleEntity);
        then(mockAdminScheduleJpaRepository).should(atLeastOnce()).findOneSchedule(adminScheduleEntity);
        then(mockAdminScheduleJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델스케줄수정Mockito테스트")
    void 모델스케줄수정Mockito테스트() {
        // given
        Integer idx = adminScheduleJpaRepository.insertSchedule(adminScheduleEntity).getIdx();

        adminScheduleEntity = AdminScheduleEntity.builder()
                .idx(idx)
                .modelIdx(adminModelEntity.getIdx())
                .modelSchedule("스케줄 수정 테스트")
                .modelScheduleTime(new Date())
                .visible("Y")
                .build();

        AdminScheduleDTO adminScheduleDTO = ScheduleMapper.INSTANCE.toDto(adminScheduleEntity);

        adminScheduleJpaRepository.updateSchedule(adminScheduleEntity);

        // when
        when(mockAdminScheduleJpaRepository.findOneSchedule(adminScheduleEntity)).thenReturn(adminScheduleDTO);
        AdminScheduleDTO scheduleInfo = mockAdminScheduleJpaRepository.findOneSchedule(adminScheduleEntity);

        // then
        assertThat(scheduleInfo.getModelIdx()).isEqualTo(adminModelEntity.getIdx());
        assertThat(scheduleInfo.getModelSchedule()).isEqualTo("스케줄 수정 테스트");

        // verify
        verify(mockAdminScheduleJpaRepository, times(1)).findOneSchedule(adminScheduleEntity);
        verify(mockAdminScheduleJpaRepository, atLeastOnce()).findOneSchedule(adminScheduleEntity);
        verifyNoMoreInteractions(mockAdminScheduleJpaRepository);

        InOrder inOrder = inOrder(mockAdminScheduleJpaRepository);
        inOrder.verify(mockAdminScheduleJpaRepository).findOneSchedule(adminScheduleEntity);
    }

    @Test
    @DisplayName("모델스케줄수정BDD테스트")
    void 모델스케줄수정BDD테스트() {
        // given
        Integer idx = adminScheduleJpaRepository.insertSchedule(adminScheduleEntity).getIdx();

        adminScheduleEntity = AdminScheduleEntity.builder()
                .idx(idx)
                .modelIdx(adminModelEntity.getIdx())
                .modelSchedule("스케줄 수정 테스트")
                .modelScheduleTime(new Date())
                .visible("Y")
                .build();

        AdminScheduleDTO adminScheduleDTO = ScheduleMapper.INSTANCE.toDto(adminScheduleEntity);

        adminScheduleJpaRepository.updateSchedule(adminScheduleEntity);

        // when
        given(mockAdminScheduleJpaRepository.findOneSchedule(adminScheduleEntity)).willReturn(adminScheduleDTO);
        AdminScheduleDTO scheduleInfo = mockAdminScheduleJpaRepository.findOneSchedule(adminScheduleEntity);

        // then
        assertThat(scheduleInfo.getModelIdx()).isEqualTo(adminModelEntity.getIdx());
        assertThat(scheduleInfo.getModelSchedule()).isEqualTo("스케줄 수정 테스트");

        // verify
        then(mockAdminScheduleJpaRepository).should(times(1)).findOneSchedule(adminScheduleEntity);
        then(mockAdminScheduleJpaRepository).should(atLeastOnce()).findOneSchedule(adminScheduleEntity);
        then(mockAdminScheduleJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("모델스케줄삭제Mockito테스트")
    void 모델스케줄삭제Mockito테스트() {
        // given
        em.persist(adminScheduleEntity);
        adminScheduleDTO = ScheduleMapper.INSTANCE.toDto(adminScheduleEntity);

        // when
        when(mockAdminScheduleJpaRepository.findOneSchedule(adminScheduleEntity)).thenReturn(adminScheduleDTO);
        Integer deleteIdx = adminScheduleJpaRepository.deleteSchedule(adminScheduleEntity.getIdx());

        // then
        assertThat(mockAdminScheduleJpaRepository.findOneSchedule(adminScheduleEntity).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockAdminScheduleJpaRepository, times(1)).findOneSchedule(adminScheduleEntity);
        verify(mockAdminScheduleJpaRepository, atLeastOnce()).findOneSchedule(adminScheduleEntity);
        verifyNoMoreInteractions(mockAdminScheduleJpaRepository);

        InOrder inOrder = inOrder(mockAdminScheduleJpaRepository);
        inOrder.verify(mockAdminScheduleJpaRepository).findOneSchedule(adminScheduleEntity);
    }

    @Test
    @DisplayName("모델스케줄삭제BDD테스트")
    void 모델스케줄삭제BDD테스트() {
        // given
        em.persist(adminScheduleEntity);
        adminScheduleDTO = ScheduleMapper.INSTANCE.toDto(adminScheduleEntity);

        // when
        given(mockAdminScheduleJpaRepository.findOneSchedule(adminScheduleEntity)).willReturn(adminScheduleDTO);
        Integer deleteIdx = adminScheduleJpaRepository.deleteSchedule(adminScheduleEntity.getIdx());

        // then
        assertThat(mockAdminScheduleJpaRepository.findOneSchedule(adminScheduleEntity).getIdx()).isEqualTo(deleteIdx);

        // verify
        then(mockAdminScheduleJpaRepository).should(times(1)).findOneSchedule(adminScheduleEntity);
        then(mockAdminScheduleJpaRepository).should(atLeastOnce()).findOneSchedule(adminScheduleEntity);
        then(mockAdminScheduleJpaRepository).shouldHaveNoMoreInteractions();
    }
}