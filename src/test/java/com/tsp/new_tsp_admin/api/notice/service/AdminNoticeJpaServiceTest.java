package com.tsp.new_tsp_admin.api.notice.service;

import com.tsp.new_tsp_admin.api.domain.notice.AdminNoticeDTO;
import com.tsp.new_tsp_admin.api.domain.notice.AdminNoticeEntity;
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
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_admin.api.notice.mapper.NoticeMapper.INSTANCE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("공지사항 Service Test")
class AdminNoticeJpaServiceTest {
    @Mock
    private AdminNoticeJpaService mockAdminNoticeJpaService;
    private final AdminNoticeJpaService adminNoticeJpaService;

    private AdminNoticeEntity adminNoticeEntity;
    private AdminNoticeDTO adminNoticeDTO;

    void createProduction() {
        adminNoticeEntity = AdminNoticeEntity.builder()
                .title("공지사항 테스트")
                .description("공지사항 테스트")
                .visible("Y")
                .build();

        adminNoticeDTO = INSTANCE.toDto(adminNoticeEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createProduction();
    }

    @Test
    @DisplayName("공지사항 리스트 조회 테스트")
    void 공지사항리스트조회테스트() throws Exception {
        // given
        Map<String, Object> noticeMap = new HashMap<>();
        noticeMap.put("jpaStartPage", 1);
        noticeMap.put("size", 3);

        // then
        assertThat(adminNoticeJpaService.findNoticesList(noticeMap)).isNotEmpty();
    }

    @Test
    @DisplayName("공지사항 리스트 조회 Mockito 테스트")
    void 공지사항리스트조회Mockito테스트() throws Exception {
        // given
        Map<String, Object> noticeMap = new HashMap<>();
        noticeMap.put("jpaStartPage", 1);
        noticeMap.put("size", 3);

        List<AdminNoticeDTO> returnNoticeList = new ArrayList<>();

        returnNoticeList.add(AdminNoticeDTO.builder().idx(1).title("공지사항테스트").description("공지사항테스트").visible("Y").build());
        returnNoticeList.add(AdminNoticeDTO.builder().idx(2).title("productionTest").description("productionTest").visible("Y").build());

        // when
        when(mockAdminNoticeJpaService.findNoticesList(noticeMap)).thenReturn(returnNoticeList);
        List<AdminNoticeDTO> noticeList = mockAdminNoticeJpaService.findNoticesList(noticeMap);

        // then
        assertAll(
                () -> assertThat(noticeList).isNotEmpty(),
                () -> assertThat(noticeList).hasSize(2)
        );

        assertThat(noticeList.get(0).getIdx()).isEqualTo(returnNoticeList.get(0).getIdx());
        assertThat(noticeList.get(0).getTitle()).isEqualTo(returnNoticeList.get(0).getTitle());
        assertThat(noticeList.get(0).getDescription()).isEqualTo(returnNoticeList.get(0).getDescription());
        assertThat(noticeList.get(0).getVisible()).isEqualTo(returnNoticeList.get(0).getVisible());

        // verify
        verify(mockAdminNoticeJpaService, times(1)).findNoticesList(noticeMap);
        verify(mockAdminNoticeJpaService, atLeastOnce()).findNoticesList(noticeMap);
        verifyNoMoreInteractions(mockAdminNoticeJpaService);

        InOrder inOrder = inOrder(mockAdminNoticeJpaService);
        inOrder.verify(mockAdminNoticeJpaService).findNoticesList(noticeMap);
    }

    @Test
    @DisplayName("공지사항 리스트 조회 BDD 테스트")
    void 공지사항리스트조회BDD테스트() throws Exception {
        // given
        Map<String, Object> noticeMap = new HashMap<>();
        noticeMap.put("jpaStartPage", 1);
        noticeMap.put("size", 3);

        List<AdminNoticeDTO> returnNoticeList = new ArrayList<>();

        returnNoticeList.add(AdminNoticeDTO.builder().idx(1).title("공지사항테스트").description("공지사항테스트").visible("Y").build());
        returnNoticeList.add(AdminNoticeDTO.builder().idx(2).title("productionTest").description("productionTest").visible("Y").build());

        // when
        given(mockAdminNoticeJpaService.findNoticesList(noticeMap)).willReturn(returnNoticeList);
        List<AdminNoticeDTO> noticeList = mockAdminNoticeJpaService.findNoticesList(noticeMap);

        // then
        assertAll(
                () -> assertThat(noticeList).isNotEmpty(),
                () -> assertThat(noticeList).hasSize(2)
        );

        assertThat(noticeList.get(0).getIdx()).isEqualTo(returnNoticeList.get(0).getIdx());
        assertThat(noticeList.get(0).getTitle()).isEqualTo(returnNoticeList.get(0).getTitle());
        assertThat(noticeList.get(0).getDescription()).isEqualTo(returnNoticeList.get(0).getDescription());
        assertThat(noticeList.get(0).getVisible()).isEqualTo(returnNoticeList.get(0).getVisible());

        // verify
        then(mockAdminNoticeJpaService).should(times(1)).findNoticesList(noticeMap);
        then(mockAdminNoticeJpaService).should(atLeastOnce()).findNoticesList(noticeMap);
        then(mockAdminNoticeJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("공지사항상세Mockito조회테스트")
    void 공지사항상세Mockito조회테스트() throws Exception {
        // given
        AdminNoticeEntity adminNoticeEntity = AdminNoticeEntity.builder()
                .idx(1)
                .title("공지사항 테스트")
                .description("공지사항 테스트")
                .visible("Y")
                .build();

        adminNoticeDTO = INSTANCE.toDto(adminNoticeEntity);

        // when
        when(mockAdminNoticeJpaService.findOneNotice(adminNoticeEntity)).thenReturn(adminNoticeDTO);
        AdminNoticeDTO noticeInfo = mockAdminNoticeJpaService.findOneNotice(adminNoticeEntity);

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(1);
        assertThat(noticeInfo.getTitle()).isEqualTo("공지사항 테스트");
        assertThat(noticeInfo.getDescription()).isEqualTo("공지사항 테스트");
        assertThat(noticeInfo.getVisible()).isEqualTo("Y");

        // verify
        verify(mockAdminNoticeJpaService, times(1)).findOneNotice(adminNoticeEntity);
        verify(mockAdminNoticeJpaService, atLeastOnce()).findOneNotice(adminNoticeEntity);
        verifyNoMoreInteractions(mockAdminNoticeJpaService);

        InOrder inOrder = inOrder(mockAdminNoticeJpaService);
        inOrder.verify(mockAdminNoticeJpaService).findOneNotice(adminNoticeEntity);
    }

    @Test
    @DisplayName("공지사항상세BDD조회테스트")
    void 공지사항상세BDD조회테스트() throws Exception {
        // given
        AdminNoticeEntity adminNoticeEntity = AdminNoticeEntity.builder()
                .idx(1)
                .title("공지사항 테스트")
                .description("공지사항 테스트")
                .visible("Y")
                .build();

        adminNoticeDTO = INSTANCE.toDto(adminNoticeEntity);

        // when
        given(mockAdminNoticeJpaService.findOneNotice(adminNoticeEntity)).willReturn(adminNoticeDTO);
        AdminNoticeDTO noticeInfo = mockAdminNoticeJpaService.findOneNotice(adminNoticeEntity);

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(1);
        assertThat(noticeInfo.getTitle()).isEqualTo("공지사항 테스트");
        assertThat(noticeInfo.getDescription()).isEqualTo("공지사항 테스트");
        assertThat(noticeInfo.getVisible()).isEqualTo("Y");

        // verify
        then(mockAdminNoticeJpaService).should(times(1)).findOneNotice(adminNoticeEntity);
        then(mockAdminNoticeJpaService).should(atLeastOnce()).findOneNotice(adminNoticeEntity);
        then(mockAdminNoticeJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 or 다음 공지사항 상세 조회 테스트")
    void 이전or다음공지사항상세조회테스트() throws Exception {
        // given
        adminNoticeEntity = AdminNoticeEntity.builder().idx(2).build();

        // when
        adminNoticeDTO = adminNoticeJpaService.findOneNotice(adminNoticeEntity);

        // 이전 공지사항
        assertThat(adminNoticeJpaService.findPrevOneNotice(adminNoticeEntity).getIdx()).isEqualTo(1);
        // 다음 공지사항
        assertThat(adminNoticeJpaService.findNextOneNotice(adminNoticeEntity).getIdx()).isEqualTo(3);
    }

    @Test
    @DisplayName("이전 공지사항 상세 조회 Mockito 테스트")
    void 이전공지사항상세조회Mockito테스트() throws Exception {
        // given
        adminNoticeEntity = AdminNoticeEntity.builder().idx(2).build();

        // when
        adminNoticeDTO = adminNoticeJpaService.findPrevOneNotice(adminNoticeEntity);

        when(mockAdminNoticeJpaService.findPrevOneNotice(adminNoticeEntity)).thenReturn(adminNoticeDTO);
        AdminNoticeDTO noticeInfo = mockAdminNoticeJpaService.findPrevOneNotice(adminNoticeEntity);

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(1);

        // verify
        verify(mockAdminNoticeJpaService, times(1)).findPrevOneNotice(adminNoticeEntity);
        verify(mockAdminNoticeJpaService, atLeastOnce()).findPrevOneNotice(adminNoticeEntity);
        verifyNoMoreInteractions(mockAdminNoticeJpaService);

        InOrder inOrder = inOrder(mockAdminNoticeJpaService);
        inOrder.verify(mockAdminNoticeJpaService).findPrevOneNotice(adminNoticeEntity);
    }

    @Test
    @DisplayName("이전 공지사항 상세 조회 BDD 테스트")
    void 이전공지사항상세조회BDD테스트() throws Exception {
        // given
        adminNoticeEntity = AdminNoticeEntity.builder().idx(2).build();

        // when
        adminNoticeDTO = adminNoticeJpaService.findPrevOneNotice(adminNoticeEntity);

        given(mockAdminNoticeJpaService.findPrevOneNotice(adminNoticeEntity)).willReturn(adminNoticeDTO);
        AdminNoticeDTO noticeInfo = mockAdminNoticeJpaService.findPrevOneNotice(adminNoticeEntity);

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(1);

        // verify
        then(mockAdminNoticeJpaService).should(times(1)).findPrevOneNotice(adminNoticeEntity);
        then(mockAdminNoticeJpaService).should(atLeastOnce()).findPrevOneNotice(adminNoticeEntity);
        then(mockAdminNoticeJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 공지사항 상세 조회 Mockito 테스트")
    void 다음공지사항상세조회Mockito테스트() throws Exception {
        // given
        adminNoticeEntity = AdminNoticeEntity.builder().idx(2).build();

        // when
        adminNoticeDTO = adminNoticeJpaService.findNextOneNotice(adminNoticeEntity);

        when(mockAdminNoticeJpaService.findNextOneNotice(adminNoticeEntity)).thenReturn(adminNoticeDTO);
        AdminNoticeDTO noticeInfo = mockAdminNoticeJpaService.findNextOneNotice(adminNoticeEntity);

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(3);

        // verify
        verify(mockAdminNoticeJpaService, times(1)).findNextOneNotice(adminNoticeEntity);
        verify(mockAdminNoticeJpaService, atLeastOnce()).findNextOneNotice(adminNoticeEntity);
        verifyNoMoreInteractions(mockAdminNoticeJpaService);

        InOrder inOrder = inOrder(mockAdminNoticeJpaService);
        inOrder.verify(mockAdminNoticeJpaService).findNextOneNotice(adminNoticeEntity);
    }

    @Test
    @DisplayName("다음 공지사항 상세 조회 BDD 테스트")
    void 다음공지사항상세조회BDD테스트() throws Exception {
        // given
        adminNoticeEntity = AdminNoticeEntity.builder().idx(2).build();

        // when
        adminNoticeDTO = adminNoticeJpaService.findNextOneNotice(adminNoticeEntity);

        given(mockAdminNoticeJpaService.findNextOneNotice(adminNoticeEntity)).willReturn(adminNoticeDTO);
        AdminNoticeDTO noticeInfo = mockAdminNoticeJpaService.findNextOneNotice(adminNoticeEntity);

        // then
        assertThat(noticeInfo.getIdx()).isEqualTo(3);

        // verify
        then(mockAdminNoticeJpaService).should(times(1)).findNextOneNotice(adminNoticeEntity);
        then(mockAdminNoticeJpaService).should(atLeastOnce()).findNextOneNotice(adminNoticeEntity);
        then(mockAdminNoticeJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("공지사항등록Mockito테스트")
    void 공지사항등록Mockito테스트() throws Exception {
        // given
        adminNoticeJpaService.insertNotice(adminNoticeEntity);

        // when
        when(mockAdminNoticeJpaService.findOneNotice(adminNoticeEntity)).thenReturn(adminNoticeDTO);
        AdminNoticeDTO noticeInfo = mockAdminNoticeJpaService.findOneNotice(adminNoticeEntity);

        // then
        assertThat(noticeInfo.getTitle()).isEqualTo("공지사항 테스트");
        assertThat(noticeInfo.getDescription()).isEqualTo("공지사항 테스트");
        assertThat(noticeInfo.getVisible()).isEqualTo("Y");

        // verify
        verify(mockAdminNoticeJpaService, times(1)).findOneNotice(adminNoticeEntity);
        verify(mockAdminNoticeJpaService, atLeastOnce()).findOneNotice(adminNoticeEntity);
        verifyNoMoreInteractions(mockAdminNoticeJpaService);

        InOrder inOrder = inOrder(mockAdminNoticeJpaService);
        inOrder.verify(mockAdminNoticeJpaService).findOneNotice(adminNoticeEntity);
    }

    @Test
    @DisplayName("공지사항등록BDD테스트")
    void 공지사항등록BDD테스트() throws Exception {
        // given
        adminNoticeJpaService.insertNotice(adminNoticeEntity);

        // when
        given(mockAdminNoticeJpaService.findOneNotice(adminNoticeEntity)).willReturn(adminNoticeDTO);
        AdminNoticeDTO noticeInfo = mockAdminNoticeJpaService.findOneNotice(adminNoticeEntity);

        // then
        assertThat(noticeInfo.getTitle()).isEqualTo("공지사항 테스트");
        assertThat(noticeInfo.getDescription()).isEqualTo("공지사항 테스트");
        assertThat(noticeInfo.getVisible()).isEqualTo("Y");

        // verify
        then(mockAdminNoticeJpaService).should(times(1)).findOneNotice(adminNoticeEntity);
        then(mockAdminNoticeJpaService).should(atLeastOnce()).findOneNotice(adminNoticeEntity);
        then(mockAdminNoticeJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("공지사항수정Mockito테스트")
    void 공지사항수정Mockito테스트() throws Exception {
        // given
        Integer idx = adminNoticeJpaService.insertNotice(adminNoticeEntity).getIdx();

        adminNoticeEntity = AdminNoticeEntity.builder()
                .idx(idx)
                .title("공지사항 테스트1")
                .description("공지사항 테스트1")
                .visible("Y")
                .build();

        AdminNoticeDTO adminNoticeDTO = INSTANCE.toDto(adminNoticeEntity);

        adminNoticeJpaService.updateNotice(adminNoticeEntity);

        // when
        when(mockAdminNoticeJpaService.findOneNotice(adminNoticeEntity)).thenReturn(adminNoticeDTO);
        AdminNoticeDTO noticeInfo = mockAdminNoticeJpaService.findOneNotice(adminNoticeEntity);

        // then
        assertThat(noticeInfo.getTitle()).isEqualTo("공지사항 테스트1");
        assertThat(noticeInfo.getDescription()).isEqualTo("공지사항 테스트1");

        // verify
        verify(mockAdminNoticeJpaService, times(1)).findOneNotice(adminNoticeEntity);
        verify(mockAdminNoticeJpaService, atLeastOnce()).findOneNotice(adminNoticeEntity);
        verifyNoMoreInteractions(mockAdminNoticeJpaService);

        InOrder inOrder = inOrder(mockAdminNoticeJpaService);
        inOrder.verify(mockAdminNoticeJpaService).findOneNotice(adminNoticeEntity);
    }

    @Test
    @DisplayName("공지사항수정BDD테스트")
    void 공지사항수정BDD테스트() throws Exception {
        // given
        Integer idx = adminNoticeJpaService.insertNotice(adminNoticeEntity).getIdx();

        adminNoticeEntity = AdminNoticeEntity.builder()
                .idx(idx)
                .title("공지사항 테스트1")
                .description("공지사항 테스트1")
                .visible("Y")
                .build();

        AdminNoticeDTO adminNoticeDTO = INSTANCE.toDto(adminNoticeEntity);

        adminNoticeJpaService.updateNotice(adminNoticeEntity);

        // when
        when(mockAdminNoticeJpaService.findOneNotice(adminNoticeEntity)).thenReturn(adminNoticeDTO);
        AdminNoticeDTO noticeInfo = mockAdminNoticeJpaService.findOneNotice(adminNoticeEntity);

        // then
        assertThat(noticeInfo.getTitle()).isEqualTo("공지사항 테스트1");
        assertThat(noticeInfo.getDescription()).isEqualTo("공지사항 테스트1");

        // verify
        then(mockAdminNoticeJpaService).should(times(1)).findOneNotice(adminNoticeEntity);
        then(mockAdminNoticeJpaService).should(atLeastOnce()).findOneNotice(adminNoticeEntity);
        then(mockAdminNoticeJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("공지사항상단고정Mockito테스트")
    void 공지사항상단고정Mockito테스트() throws Exception {
        // given
        Integer idx = adminNoticeJpaService.insertNotice(adminNoticeEntity).getIdx();

        Boolean fixed = adminNoticeJpaService.toggleFixed(idx).getTopFixed();

        adminNoticeEntity = AdminNoticeEntity.builder()
                .idx(idx)
                .title("공지사항 테스트1")
                .description("공지사항 테스트1")
                .topFixed(fixed)
                .visible("Y")
                .build();

        AdminNoticeDTO adminNoticeDTO = INSTANCE.toDto(adminNoticeEntity);

        // when
        when(mockAdminNoticeJpaService.findOneNotice(adminNoticeEntity)).thenReturn(adminNoticeDTO);
        AdminNoticeDTO noticeInfo = mockAdminNoticeJpaService.findOneNotice(adminNoticeEntity);

        // then
        assertThat(noticeInfo.getTopFixed()).isTrue();

        // verify
        verify(mockAdminNoticeJpaService, times(1)).findOneNotice(adminNoticeEntity);
        verify(mockAdminNoticeJpaService, atLeastOnce()).findOneNotice(adminNoticeEntity);
        verifyNoMoreInteractions(mockAdminNoticeJpaService);

        InOrder inOrder = inOrder(mockAdminNoticeJpaService);
        inOrder.verify(mockAdminNoticeJpaService).findOneNotice(adminNoticeEntity);
    }

    @Test
    @DisplayName("공지사항상단고정BDD테스트")
    void 공지사항상단고정BDD테스트() throws Exception {
        // given
        Integer idx = adminNoticeJpaService.insertNotice(adminNoticeEntity).getIdx();

        Boolean fixed = adminNoticeJpaService.toggleFixed(idx).getTopFixed();

        adminNoticeEntity = AdminNoticeEntity.builder()
                .idx(idx)
                .title("공지사항 테스트1")
                .description("공지사항 테스트1")
                .topFixed(fixed)
                .visible("Y")
                .build();

        AdminNoticeDTO adminNoticeDTO = INSTANCE.toDto(adminNoticeEntity);

        // when
        given(mockAdminNoticeJpaService.findOneNotice(adminNoticeEntity)).willReturn(adminNoticeDTO);
        AdminNoticeDTO noticeInfo = mockAdminNoticeJpaService.findOneNotice(adminNoticeEntity);

        // then
        assertThat(noticeInfo.getTopFixed()).isTrue();

        // verify
        then(mockAdminNoticeJpaService).should(times(1)).findOneNotice(adminNoticeEntity);
        then(mockAdminNoticeJpaService).should(atLeastOnce()).findOneNotice(adminNoticeEntity);
        then(mockAdminNoticeJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("공지사항삭제테스트")
    void 공지사항삭제테스트() throws Exception {
        // given
        adminNoticeJpaService.insertNotice(adminNoticeEntity);

        Integer entityIdx = adminNoticeEntity.getIdx();
        Integer idx = adminNoticeJpaService.deleteNotice(adminNoticeEntity.getIdx());

        // then
        assertThat(entityIdx).isEqualTo(idx);
    }

    @Test
    @DisplayName("공지사항삭제Mockito테스트")
    void 공지사항삭제Mockito테스트() throws Exception {
        // given
        adminNoticeJpaService.insertNotice(adminNoticeEntity);
        adminNoticeDTO = INSTANCE.toDto(adminNoticeEntity);

        // when
        when(mockAdminNoticeJpaService.findOneNotice(adminNoticeEntity)).thenReturn(adminNoticeDTO);
        Integer deleteIdx = adminNoticeJpaService.deleteNotice(adminNoticeEntity.getIdx());

        // then
        assertThat(mockAdminNoticeJpaService.findOneNotice(adminNoticeEntity).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockAdminNoticeJpaService, times(1)).findOneNotice(adminNoticeEntity);
        verify(mockAdminNoticeJpaService, atLeastOnce()).findOneNotice(adminNoticeEntity);
        verifyNoMoreInteractions(mockAdminNoticeJpaService);

        InOrder inOrder = inOrder(mockAdminNoticeJpaService);
        inOrder.verify(mockAdminNoticeJpaService).findOneNotice(adminNoticeEntity);
    }

    @Test
    @DisplayName("공지사항삭제BDD테스트")
    void 공지사항삭제BDD테스트() throws Exception {
        // given
        adminNoticeJpaService.insertNotice(adminNoticeEntity);
        adminNoticeDTO = INSTANCE.toDto(adminNoticeEntity);

        // when
        when(mockAdminNoticeJpaService.findOneNotice(adminNoticeEntity)).thenReturn(adminNoticeDTO);
        Integer deleteIdx = adminNoticeJpaService.deleteNotice(adminNoticeEntity.getIdx());

        // then
        assertThat(mockAdminNoticeJpaService.findOneNotice(adminNoticeEntity).getIdx()).isEqualTo(deleteIdx);

        // verify
        then(mockAdminNoticeJpaService).should(times(1)).findOneNotice(adminNoticeEntity);
        then(mockAdminNoticeJpaService).should(atLeastOnce()).findOneNotice(adminNoticeEntity);
        then(mockAdminNoticeJpaService).shouldHaveNoMoreInteractions();
    }
}