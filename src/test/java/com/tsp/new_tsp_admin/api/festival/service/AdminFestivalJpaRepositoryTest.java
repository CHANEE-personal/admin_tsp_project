package com.tsp.new_tsp_admin.api.festival.service;

import com.tsp.new_tsp_admin.api.domain.festival.AdminFestivalDTO;
import com.tsp.new_tsp_admin.api.domain.festival.AdminFestivalEntity;
import lombok.RequiredArgsConstructor;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("모델 Repository Test")
class AdminFestivalJpaRepositoryTest {

    @Mock
    private AdminFestivalJpaRepository mockAdminFestivalJpaRepository;
    private final AdminFestivalJpaRepository adminFestivalJpaRepository;
    private final EntityManager em;

    private AdminFestivalEntity adminFestivalEntity;
    private AdminFestivalDTO adminFestivalDTO;

    void createFestival() {
        // 등록
        LocalDateTime dateTime = LocalDateTime.now();

        adminFestivalEntity = AdminFestivalEntity.builder()
                .festivalTitle("축제 제목")
                .festivalDescription("축제 내용")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth())
                .festivalTime(dateTime)
                .build();

        em.persist(adminFestivalEntity);

        adminFestivalDTO = AdminFestivalEntity.toDto(adminFestivalEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createFestival();
    }

    @Test
    @DisplayName("축제 리스트 조회 테스트")
    void 축제리스트조회테스트() {
        Map<String, Object> festivalMap = new HashMap<>();
        festivalMap.put("jpaStartPage", 1);
        festivalMap.put("size", 3);

        assertThat(adminFestivalJpaRepository.findFestivalList(festivalMap)).isNotEmpty();
    }

    @Test
    @DisplayName("축제 리스트 조회 Mockito 테스트")
    void 축제리스트조회Mockito테스트() {
        Map<String, Object> festivalMap = new HashMap<>();
        festivalMap.put("jpaStartPage", 1);
        festivalMap.put("size", 3);

        List<AdminFestivalDTO> festivalList = new ArrayList<>();
        festivalList.add(adminFestivalDTO);

        // when
        when(mockAdminFestivalJpaRepository.findFestivalList(festivalMap)).thenReturn(festivalList);
        List<AdminFestivalDTO> findFestivalList = mockAdminFestivalJpaRepository.findFestivalList(festivalMap);

        // then
        assertThat(findFestivalList.get(0).getFestivalTitle()).isEqualTo("축제 제목");
        assertThat(findFestivalList.get(0).getFestivalDescription()).isEqualTo("축제 내용");

        // verify
        verify(mockAdminFestivalJpaRepository, times(1)).findFestivalList(festivalMap);
        verify(mockAdminFestivalJpaRepository, atLeastOnce()).findFestivalList(festivalMap);
        verifyNoMoreInteractions(mockAdminFestivalJpaRepository);

        InOrder inOrder = inOrder(mockAdminFestivalJpaRepository);
        inOrder.verify(mockAdminFestivalJpaRepository).findFestivalList(festivalMap);
    }

    @Test
    @DisplayName("축제 상세 조회 테스트")
    void 축제상세조회테스트() {
        AdminFestivalDTO oneFestival = adminFestivalJpaRepository.findOneFestival(adminFestivalDTO.getIdx());
        assertThat(oneFestival.getFestivalTitle()).isEqualTo("축제 제목");
        assertThat(oneFestival.getFestivalDescription()).isEqualTo("축제 내용");
    }

    @Test
    @DisplayName("축제 상세 조회 Mockito 테스트")
    void 축제상세조회Mockito테스트() {
        // when
        when(mockAdminFestivalJpaRepository.findOneFestival(adminFestivalDTO.getIdx())).thenReturn(adminFestivalDTO);
        AdminFestivalDTO oneFestival = mockAdminFestivalJpaRepository.findOneFestival(adminFestivalDTO.getIdx());

        // then
        assertThat(oneFestival.getFestivalTitle()).isEqualTo("축제 제목");
        assertThat(oneFestival.getFestivalDescription()).isEqualTo("축제 내용");


        // verify
        verify(mockAdminFestivalJpaRepository, times(1)).findOneFestival(adminFestivalDTO.getIdx());
        verify(mockAdminFestivalJpaRepository, atLeastOnce()).findOneFestival(adminFestivalDTO.getIdx());
        verifyNoMoreInteractions(mockAdminFestivalJpaRepository);

        InOrder inOrder = inOrder(mockAdminFestivalJpaRepository);
        inOrder.verify(mockAdminFestivalJpaRepository).findOneFestival(adminFestivalDTO.getIdx());
    }

    @Test
    @DisplayName("축제 등록 테스트")
    void 축제등록테스트() {
        // 등록
        LocalDateTime dateTime = LocalDateTime.now();

        AdminFestivalEntity insertFestivalEntity = AdminFestivalEntity.builder()
                .festivalTitle("축제 등록 제목")
                .festivalDescription("축제 등록 내용")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth())
                .festivalTime(dateTime)
                .build();

        AdminFestivalDTO oneFestival = adminFestivalJpaRepository.changeFestival(insertFestivalEntity);
        assertThat(oneFestival.getFestivalTitle()).isEqualTo("축제 등록 제목");
        assertThat(oneFestival.getFestivalDescription()).isEqualTo("축제 등록 내용");
    }

    @Test
    @DisplayName("축제 등록 Mockito 테스트")
    void 축제등록Mockito테스트() {
        LocalDateTime dateTime = LocalDateTime.now();

        AdminFestivalEntity insertFestivalEntity = AdminFestivalEntity.builder()
                .festivalTitle("축제 등록 제목")
                .festivalDescription("축제 등록 내용")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth())
                .festivalTime(dateTime)
                .build();

        AdminFestivalDTO insertFestivalDTO = AdminFestivalEntity.toDto(insertFestivalEntity);

        // when
        when(mockAdminFestivalJpaRepository.changeFestival(insertFestivalEntity)).thenReturn(insertFestivalDTO);
        AdminFestivalDTO oneFestival = mockAdminFestivalJpaRepository.changeFestival(insertFestivalEntity);

        // then
        assertThat(oneFestival.getIdx()).isEqualTo(insertFestivalDTO.getIdx());
        assertThat(oneFestival.getFestivalTitle()).isEqualTo("축제 등록 제목");
        assertThat(oneFestival.getFestivalDescription()).isEqualTo("축제 등록 내용");

        // verify
        verify(mockAdminFestivalJpaRepository, times(1)).changeFestival(insertFestivalEntity);
        verify(mockAdminFestivalJpaRepository, atLeastOnce()).changeFestival(insertFestivalEntity);
        verifyNoMoreInteractions(mockAdminFestivalJpaRepository);

        InOrder inOrder = inOrder(mockAdminFestivalJpaRepository);
        inOrder.verify(mockAdminFestivalJpaRepository).changeFestival(insertFestivalEntity);
    }

    @Test
    @DisplayName("축제 수정 테스트")
    void 축제수정테스트() {
        LocalDateTime dateTime = LocalDateTime.now();

        AdminFestivalEntity updateFestivalEntity = AdminFestivalEntity.builder()
                .idx(adminFestivalDTO.getIdx())
                .festivalTitle("축제 수정 제목")
                .festivalDescription("축제 수정 내용")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth())
                .festivalTime(dateTime)
                .build();

        AdminFestivalDTO updateFestival = adminFestivalJpaRepository.changeFestival(updateFestivalEntity);
        assertThat(updateFestival.getFestivalTitle()).isEqualTo("축제 수정 제목");
        assertThat(updateFestival.getFestivalDescription()).isEqualTo("축제 수정 내용");
    }

    @Test
    @DisplayName("축제 수정 Mockito 테스트")
    void 축제수정Mockito테스트() {
        LocalDateTime dateTime = LocalDateTime.now();

        AdminFestivalEntity updateFestivalEntity = AdminFestivalEntity.builder()
                .idx(adminFestivalDTO.getIdx())
                .festivalTitle("축제 수정 제목")
                .festivalDescription("축제 수정 내용")
                .festivalMonth(dateTime.getMonthValue())
                .festivalDay(dateTime.getDayOfMonth())
                .festivalTime(dateTime)
                .build();

        AdminFestivalDTO updateFestivalDTO = AdminFestivalEntity.toDto(updateFestivalEntity);

        // when
        when(mockAdminFestivalJpaRepository.changeFestival(updateFestivalEntity)).thenReturn(updateFestivalDTO);
        AdminFestivalDTO oneFestival = mockAdminFestivalJpaRepository.changeFestival(updateFestivalEntity);

        // then
        assertThat(oneFestival.getIdx()).isEqualTo(updateFestivalDTO.getIdx());
        assertThat(oneFestival.getFestivalTitle()).isEqualTo("축제 수정 제목");
        assertThat(oneFestival.getFestivalDescription()).isEqualTo("축제 수정 내용");

        // verify
        verify(mockAdminFestivalJpaRepository, times(1)).changeFestival(updateFestivalEntity);
        verify(mockAdminFestivalJpaRepository, atLeastOnce()).changeFestival(updateFestivalEntity);
        verifyNoMoreInteractions(mockAdminFestivalJpaRepository);

        InOrder inOrder = inOrder(mockAdminFestivalJpaRepository);
        inOrder.verify(mockAdminFestivalJpaRepository).changeFestival(updateFestivalEntity);
    }

    @Test
    @DisplayName("축제 삭제 테스트")
    void 축제삭제테스트() {
        Long deleteIdx = adminFestivalJpaRepository.deleteFestival(adminFestivalDTO.getIdx());
        assertThat(deleteIdx).isEqualTo(adminFestivalDTO.getIdx());
    }

    @Test
    @DisplayName("축제 삭제 Mockito 테스트")
    void 축제삭제Mockito테스트() {
        // when
        when(mockAdminFestivalJpaRepository.deleteFestival(adminFestivalDTO.getIdx())).thenReturn(adminFestivalDTO.getIdx());
        Long deleteIdx = mockAdminFestivalJpaRepository.deleteFestival(adminFestivalDTO.getIdx());

        // then
        assertThat(deleteIdx).isEqualTo(adminFestivalDTO.getIdx());

        // verify
        verify(mockAdminFestivalJpaRepository, times(1)).deleteFestival(adminFestivalDTO.getIdx());
        verify(mockAdminFestivalJpaRepository, atLeastOnce()).deleteFestival(adminFestivalDTO.getIdx());
        verifyNoMoreInteractions(mockAdminFestivalJpaRepository);

        InOrder inOrder = inOrder(mockAdminFestivalJpaRepository);
        inOrder.verify(mockAdminFestivalJpaRepository).deleteFestival(adminFestivalDTO.getIdx());
    }
}