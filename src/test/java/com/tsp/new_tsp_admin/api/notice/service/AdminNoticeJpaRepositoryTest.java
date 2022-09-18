package com.tsp.new_tsp_admin.api.notice.service;

import com.tsp.new_tsp_admin.api.domain.notice.AdminNoticeDTO;
import com.tsp.new_tsp_admin.api.domain.notice.AdminNoticeEntity;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_admin.api.notice.mapper.NoticeMapper.INSTANCE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
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
@DisplayName("공지사항 Repository Test")
class AdminNoticeJpaRepositoryTest {

    @Mock
    private AdminNoticeJpaRepository mockAdminNoticeJpaRepository;
    private final AdminNoticeJpaRepository adminNoticeJpaRepository;
    private final EntityManager em;

    private AdminNoticeEntity adminNoticeEntity;
    private AdminNoticeDTO adminNoticeDTO;

    void createNotice() {
        adminNoticeEntity = AdminNoticeEntity.builder()
                .title("공지사항 테스트")
                .description("공지사항 테스트")
                .visible("Y")
                .viewCount(1)
                .build();

        adminNoticeDTO = INSTANCE.toDto(adminNoticeEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createNotice();
    }

    @Test
    @Disabled
    @DisplayName("공지사항리스트조회테스트")
    void 공지사항리스트조회테스트() {
        // given
        Map<String, Object> noticeMap = new HashMap<>();
        noticeMap.put("jpaStartPage", 1);
        noticeMap.put("size", 3);

        // then
        assertThat(adminNoticeJpaRepository.findNoticesList(noticeMap)).isNotEmpty();
    }

    @Test
    @Disabled
    @DisplayName("공지사항상세조회테스트")
    void 공지사항상세조회테스트() {
        // given
        adminNoticeEntity = AdminNoticeEntity.builder().idx(1).build();

        // when
        adminNoticeDTO = adminNoticeJpaRepository.findOneNotice(adminNoticeEntity);

        // then
        assertAll(() -> assertThat(adminNoticeDTO.getIdx()).isEqualTo(1),
                () -> {
                    assertThat(adminNoticeDTO.getTitle()).isEqualTo("테스트1");
                    assertNotNull(adminNoticeDTO.getTitle());
                },
                () -> {
                    assertThat(adminNoticeDTO.getDescription()).isEqualTo("테스트1");
                    assertNotNull(adminNoticeDTO.getDescription());
                },
                () -> {
                    assertThat(adminNoticeDTO.getVisible()).isEqualTo("Y");
                    assertNotNull(adminNoticeDTO.getVisible());
                });
    }

    @Test
    @DisplayName("공지사항Mockito조회테스트")
    void 공지사항Mockito조회테스트() {
        // given
        Map<String, Object> noticeMap = new HashMap<>();
        noticeMap.put("jpaStartPage", 1);
        noticeMap.put("size", 3);

        List<AdminNoticeDTO> noticeList = new ArrayList<>();
        noticeList.add(AdminNoticeDTO.builder().idx(1).title("공지사항 테스트")
                .description("공지사항 테스트").build());

        // when
        when(mockAdminNoticeJpaRepository.findNoticesList(noticeMap)).thenReturn(noticeList);
        List<AdminNoticeDTO> newNoticeList = mockAdminNoticeJpaRepository.findNoticesList(noticeMap);

        // then
        assertThat(newNoticeList.get(0).getIdx()).isEqualTo(noticeList.get(0).getIdx());
        assertThat(newNoticeList.get(0).getTitle()).isEqualTo(noticeList.get(0).getTitle());
        assertThat(newNoticeList.get(0).getDescription()).isEqualTo(noticeList.get(0).getDescription());
        assertThat(newNoticeList.get(0).getVisible()).isEqualTo(noticeList.get(0).getVisible());

        // verify
        verify(mockAdminNoticeJpaRepository, times(1)).findNoticesList(noticeMap);
        verify(mockAdminNoticeJpaRepository, atLeastOnce()).findNoticesList(noticeMap);
        verifyNoMoreInteractions(mockAdminNoticeJpaRepository);

        InOrder inOrder = inOrder(mockAdminNoticeJpaRepository);
        inOrder.verify(mockAdminNoticeJpaRepository).findNoticesList(noticeMap);
    }

    @Test
    @DisplayName("공지사항BDD조회테스트")
    void 공지사항BDD조회테스트() {
        // given
        Map<String, Object> noticeMap = new HashMap<>();
        noticeMap.put("jpaStartPage", 1);
        noticeMap.put("size", 3);

        List<AdminNoticeDTO> noticeList = new ArrayList<>();
        noticeList.add(AdminNoticeDTO.builder().idx(1).title("공지사항 테스트")
                .description("공지사항 테스트").build());

        // when
        given(mockAdminNoticeJpaRepository.findNoticesList(noticeMap)).willReturn(noticeList);
        List<AdminNoticeDTO> newNoticeList = mockAdminNoticeJpaRepository.findNoticesList(noticeMap);

        // then
        assertThat(newNoticeList.get(0).getIdx()).isEqualTo(noticeList.get(0).getIdx());
        assertThat(newNoticeList.get(0).getTitle()).isEqualTo(noticeList.get(0).getTitle());
        assertThat(newNoticeList.get(0).getDescription()).isEqualTo(noticeList.get(0).getDescription());
        assertThat(newNoticeList.get(0).getVisible()).isEqualTo(noticeList.get(0).getVisible());

        // verify
        then(mockAdminNoticeJpaRepository).should(times(1)).findNoticesList(noticeMap);
        then(mockAdminNoticeJpaRepository).should(atLeastOnce()).findNoticesList(noticeMap);
        then(mockAdminNoticeJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("공지사항상세Mockito조회테스트")
    void 공지사항상세Mockito조회테스트() {
        // given
        AdminNoticeEntity adminNoticeEntity = AdminNoticeEntity.builder()
                .idx(1)
                .title("공지사항 테스트")
                .description("공지사항 테스트")
                .visible("Y")
                .build();

        adminNoticeDTO = INSTANCE.toDto(adminNoticeEntity);

        // when
        when(mockAdminNoticeJpaRepository.findOneNotice(adminNoticeEntity)).thenReturn(adminNoticeDTO);
        AdminNoticeDTO noticeInfo = mockAdminNoticeJpaRepository.findOneNotice(adminNoticeEntity);

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(1);
        assertThat(noticeInfo.getTitle()).isEqualTo("공지사항 테스트");
        assertThat(noticeInfo.getDescription()).isEqualTo("공지사항 테스트");
        assertThat(noticeInfo.getVisible()).isEqualTo("Y");

        // verify
        verify(mockAdminNoticeJpaRepository, times(1)).findOneNotice(adminNoticeEntity);
        verify(mockAdminNoticeJpaRepository, atLeastOnce()).findOneNotice(adminNoticeEntity);
        verifyNoMoreInteractions(mockAdminNoticeJpaRepository);

        InOrder inOrder = inOrder(mockAdminNoticeJpaRepository);
        inOrder.verify(mockAdminNoticeJpaRepository).findOneNotice(adminNoticeEntity);
    }

    @Test
    @DisplayName("공지사항상세BDD조회테스트")
    void 공지사항상세BDD조회테스트() {
        // given
        AdminNoticeEntity adminNoticeEntity = AdminNoticeEntity.builder()
                .idx(1)
                .title("공지사항 테스트")
                .description("공지사항 테스트")
                .visible("Y")
                .build();

        adminNoticeDTO = INSTANCE.toDto(adminNoticeEntity);

        // when
        given(mockAdminNoticeJpaRepository.findOneNotice(adminNoticeEntity)).willReturn(adminNoticeDTO);
        AdminNoticeDTO noticeInfo = mockAdminNoticeJpaRepository.findOneNotice(adminNoticeEntity);

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(1);
        assertThat(noticeInfo.getTitle()).isEqualTo("공지사항 테스트");
        assertThat(noticeInfo.getDescription()).isEqualTo("공지사항 테스트");
        assertThat(noticeInfo.getVisible()).isEqualTo("Y");

        // verify
        then(mockAdminNoticeJpaRepository).should(times(1)).findOneNotice(adminNoticeEntity);
        then(mockAdminNoticeJpaRepository).should(atLeastOnce()).findOneNotice(adminNoticeEntity);
        then(mockAdminNoticeJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 or 다음 공지사항 상세 조회 테스트")
    void 이전or다음공지사항상세조회테스트() {
        // given
        adminNoticeEntity = AdminNoticeEntity.builder().idx(2).build();

        // when
        adminNoticeDTO = adminNoticeJpaRepository.findOneNotice(adminNoticeEntity);

        // 이전 공지사항
        assertThat(adminNoticeJpaRepository.findPrevOneNotice(adminNoticeEntity).getIdx()).isEqualTo(1);
        // 다음 공지사항
        assertThat(adminNoticeJpaRepository.findNextOneNotice(adminNoticeEntity).getIdx()).isEqualTo(3);
    }

    @Test
    @DisplayName("이전 공지사항 상세 조회 Mockito 테스트")
    void 이전공지사항상세조회Mockito테스트() {
        // given
        adminNoticeEntity = AdminNoticeEntity.builder().idx(2).build();

        // when
        adminNoticeDTO = adminNoticeJpaRepository.findPrevOneNotice(adminNoticeEntity);

        when(mockAdminNoticeJpaRepository.findPrevOneNotice(adminNoticeEntity)).thenReturn(adminNoticeDTO);
        AdminNoticeDTO noticeInfo = mockAdminNoticeJpaRepository.findPrevOneNotice(adminNoticeEntity);

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(1);

        // verify
        verify(mockAdminNoticeJpaRepository, times(1)).findPrevOneNotice(adminNoticeEntity);
        verify(mockAdminNoticeJpaRepository, atLeastOnce()).findPrevOneNotice(adminNoticeEntity);
        verifyNoMoreInteractions(mockAdminNoticeJpaRepository);

        InOrder inOrder = inOrder(mockAdminNoticeJpaRepository);
        inOrder.verify(mockAdminNoticeJpaRepository).findPrevOneNotice(adminNoticeEntity);
    }

    @Test
    @DisplayName("이전 공지사항 상세 조회 BDD 테스트")
    void 이전공지사항상세조회BDD테스트() {
        // given
        adminNoticeEntity = AdminNoticeEntity.builder().idx(2).build();

        // when
        adminNoticeDTO = adminNoticeJpaRepository.findPrevOneNotice(adminNoticeEntity);

        given(mockAdminNoticeJpaRepository.findPrevOneNotice(adminNoticeEntity)).willReturn(adminNoticeDTO);
        AdminNoticeDTO noticeInfo = mockAdminNoticeJpaRepository.findPrevOneNotice(adminNoticeEntity);

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(1);

        // verify
        then(mockAdminNoticeJpaRepository).should(times(1)).findPrevOneNotice(adminNoticeEntity);
        then(mockAdminNoticeJpaRepository).should(atLeastOnce()).findPrevOneNotice(adminNoticeEntity);
        then(mockAdminNoticeJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 공지사항 상세 조회 Mockito 테스트")
    void 다음공지사항상세조회Mockito테스트() {
        // given
        adminNoticeEntity = AdminNoticeEntity.builder().idx(2).build();

        // when
        adminNoticeDTO = adminNoticeJpaRepository.findNextOneNotice(adminNoticeEntity);

        when(mockAdminNoticeJpaRepository.findNextOneNotice(adminNoticeEntity)).thenReturn(adminNoticeDTO);
        AdminNoticeDTO noticeInfo = mockAdminNoticeJpaRepository.findNextOneNotice(adminNoticeEntity);

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(3);

        // verify
        verify(mockAdminNoticeJpaRepository, times(1)).findNextOneNotice(adminNoticeEntity);
        verify(mockAdminNoticeJpaRepository, atLeastOnce()).findNextOneNotice(adminNoticeEntity);
        verifyNoMoreInteractions(mockAdminNoticeJpaRepository);

        InOrder inOrder = inOrder(mockAdminNoticeJpaRepository);
        inOrder.verify(mockAdminNoticeJpaRepository).findNextOneNotice(adminNoticeEntity);
    }

    @Test
    @DisplayName("다음 공지사항 상세 조회 BDD 테스트")
    void 다음공지사항상세조회BDD테스트() {
        // given
        adminNoticeEntity = AdminNoticeEntity.builder().idx(2).build();

        // when
        adminNoticeDTO = adminNoticeJpaRepository.findNextOneNotice(adminNoticeEntity);

        given(mockAdminNoticeJpaRepository.findNextOneNotice(adminNoticeEntity)).willReturn(adminNoticeDTO);
        AdminNoticeDTO noticeInfo = mockAdminNoticeJpaRepository.findNextOneNotice(adminNoticeEntity);

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(3);

        // verify
        then(mockAdminNoticeJpaRepository).should(times(1)).findNextOneNotice(adminNoticeEntity);
        then(mockAdminNoticeJpaRepository).should(atLeastOnce()).findNextOneNotice(adminNoticeEntity);
        then(mockAdminNoticeJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("공지사항등록Mockito테스트")
    void 공지사항등록Mockito테스트() {
        // given
        adminNoticeJpaRepository.insertNotice(adminNoticeEntity);

        // when
        when(mockAdminNoticeJpaRepository.findOneNotice(adminNoticeEntity)).thenReturn(adminNoticeDTO);
        AdminNoticeDTO noticeInfo = mockAdminNoticeJpaRepository.findOneNotice(adminNoticeEntity);

        // then
        assertThat(noticeInfo.getTitle()).isEqualTo("공지사항 테스트");
        assertThat(noticeInfo.getDescription()).isEqualTo("공지사항 테스트");
        assertThat(noticeInfo.getVisible()).isEqualTo("Y");

        // verify
        verify(mockAdminNoticeJpaRepository, times(1)).findOneNotice(adminNoticeEntity);
        verify(mockAdminNoticeJpaRepository, atLeastOnce()).findOneNotice(adminNoticeEntity);
        verifyNoMoreInteractions(mockAdminNoticeJpaRepository);

        InOrder inOrder = inOrder(mockAdminNoticeJpaRepository);
        inOrder.verify(mockAdminNoticeJpaRepository).findOneNotice(adminNoticeEntity);
    }

    @Test
    @DisplayName("공지사항등록BDD테스트")
    void 공지사항등록BDD테스트() {
        // given
        adminNoticeJpaRepository.insertNotice(adminNoticeEntity);

        // when
        given(mockAdminNoticeJpaRepository.findOneNotice(adminNoticeEntity)).willReturn(adminNoticeDTO);
        AdminNoticeDTO noticeInfo = mockAdminNoticeJpaRepository.findOneNotice(adminNoticeEntity);

        // then
        assertThat(noticeInfo.getTitle()).isEqualTo("공지사항 테스트");
        assertThat(noticeInfo.getDescription()).isEqualTo("공지사항 테스트");
        assertThat(noticeInfo.getVisible()).isEqualTo("Y");

        // verify
        then(mockAdminNoticeJpaRepository).should(times(1)).findOneNotice(adminNoticeEntity);
        then(mockAdminNoticeJpaRepository).should(atLeastOnce()).findOneNotice(adminNoticeEntity);
        then(mockAdminNoticeJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("공지사항수정Mockito테스트")
    void 공지사항수정Mockito테스트() {
        // given
        Integer idx = adminNoticeJpaRepository.insertNotice(adminNoticeEntity).getIdx();

        adminNoticeEntity = AdminNoticeEntity.builder()
                .idx(idx)
                .title("공지사항 테스트1")
                .description("공지사항 테스트1")
                .visible("Y")
                .build();

        AdminNoticeDTO adminNoticeDTO = INSTANCE.toDto(adminNoticeEntity);

        adminNoticeJpaRepository.updateNotice(adminNoticeEntity);

        // when
        when(mockAdminNoticeJpaRepository.findOneNotice(adminNoticeEntity)).thenReturn(adminNoticeDTO);
        AdminNoticeDTO noticeInfo = mockAdminNoticeJpaRepository.findOneNotice(adminNoticeEntity);

        // then
        assertThat(noticeInfo.getTitle()).isEqualTo("공지사항 테스트1");
        assertThat(noticeInfo.getDescription()).isEqualTo("공지사항 테스트1");

        // verify
        verify(mockAdminNoticeJpaRepository, times(1)).findOneNotice(adminNoticeEntity);
        verify(mockAdminNoticeJpaRepository, atLeastOnce()).findOneNotice(adminNoticeEntity);
        verifyNoMoreInteractions(mockAdminNoticeJpaRepository);

        InOrder inOrder = inOrder(mockAdminNoticeJpaRepository);
        inOrder.verify(mockAdminNoticeJpaRepository).findOneNotice(adminNoticeEntity);
    }

    @Test
    @DisplayName("공지사항수정BDD테스트")
    void 공지사항수정BDD테스트() {
        // given
        Integer idx = adminNoticeJpaRepository.insertNotice(adminNoticeEntity).getIdx();

        adminNoticeEntity = AdminNoticeEntity.builder()
                .idx(idx)
                .title("공지사항 테스트1")
                .description("공지사항 테스트1")
                .visible("Y")
                .build();

        AdminNoticeDTO adminNoticeDTO = INSTANCE.toDto(adminNoticeEntity);

        adminNoticeJpaRepository.updateNotice(adminNoticeEntity);

        // when
        when(mockAdminNoticeJpaRepository.findOneNotice(adminNoticeEntity)).thenReturn(adminNoticeDTO);
        AdminNoticeDTO noticeInfo = mockAdminNoticeJpaRepository.findOneNotice(adminNoticeEntity);

        // then
        assertThat(noticeInfo.getTitle()).isEqualTo("공지사항 테스트1");
        assertThat(noticeInfo.getDescription()).isEqualTo("공지사항 테스트1");

        // verify
        then(mockAdminNoticeJpaRepository).should(times(1)).findOneNotice(adminNoticeEntity);
        then(mockAdminNoticeJpaRepository).should(atLeastOnce()).findOneNotice(adminNoticeEntity);
        then(mockAdminNoticeJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("공지사항삭제테스트")
    void 공지사항삭제테스트() {
        // given
        em.persist(adminNoticeEntity);

        Integer entityIdx = adminNoticeEntity.getIdx();
        Integer idx = adminNoticeJpaRepository.deleteNotice(adminNoticeEntity.getIdx());

        // then
        assertThat(entityIdx).isEqualTo(idx);
    }

    @Test
    @DisplayName("공지사항삭제Mockito테스트")
    void 공지사항삭제Mockito테스트() {
        // given
        em.persist(adminNoticeEntity);
        adminNoticeDTO = INSTANCE.toDto(adminNoticeEntity);

        // when
        when(mockAdminNoticeJpaRepository.findOneNotice(adminNoticeEntity)).thenReturn(adminNoticeDTO);
        Integer deleteIdx = adminNoticeJpaRepository.deleteNotice(adminNoticeEntity.getIdx());

        // then
        assertThat(mockAdminNoticeJpaRepository.findOneNotice(adminNoticeEntity).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockAdminNoticeJpaRepository, times(1)).findOneNotice(adminNoticeEntity);
        verify(mockAdminNoticeJpaRepository, atLeastOnce()).findOneNotice(adminNoticeEntity);
        verifyNoMoreInteractions(mockAdminNoticeJpaRepository);

        InOrder inOrder = inOrder(mockAdminNoticeJpaRepository);
        inOrder.verify(mockAdminNoticeJpaRepository).findOneNotice(adminNoticeEntity);
    }

    @Test
    @DisplayName("공지사항삭제BDD테스트")
    void 공지사항삭제BDD테스트() {
        // given
        em.persist(adminNoticeEntity);
        adminNoticeDTO = INSTANCE.toDto(adminNoticeEntity);

        // when
        given(mockAdminNoticeJpaRepository.findOneNotice(adminNoticeEntity)).willReturn(adminNoticeDTO);
        Integer deleteIdx = adminNoticeJpaRepository.deleteNotice(adminNoticeEntity.getIdx());

        // then
        assertThat(mockAdminNoticeJpaRepository.findOneNotice(adminNoticeEntity).getIdx()).isEqualTo(deleteIdx);

        // verify
        then(mockAdminNoticeJpaRepository).should(times(1)).findOneNotice(adminNoticeEntity);
        then(mockAdminNoticeJpaRepository).should(atLeastOnce()).findOneNotice(adminNoticeEntity);
        then(mockAdminNoticeJpaRepository).shouldHaveNoMoreInteractions();
    }
}