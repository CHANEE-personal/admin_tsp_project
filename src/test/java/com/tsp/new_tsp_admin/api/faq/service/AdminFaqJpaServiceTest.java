package com.tsp.new_tsp_admin.api.faq.service;

import com.tsp.new_tsp_admin.api.domain.faq.AdminFaqDTO;
import com.tsp.new_tsp_admin.api.domain.faq.AdminFaqEntity;
import com.tsp.new_tsp_admin.api.faq.mapper.FaqMapper;
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

import static com.tsp.new_tsp_admin.api.faq.mapper.FaqMapper.INSTANCE;
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
@DisplayName("FAQ Service Test")
class AdminFaqJpaServiceTest {
    @Mock AdminFaqJpaService mockAdminFaqJpaService;
    private final AdminFaqJpaService adminFaqJpaService;

    private AdminFaqEntity adminFaqEntity;
    private AdminFaqDTO adminFaqDTO;

    void createFaq() {
        adminFaqEntity = AdminFaqEntity.builder()
                .title("FAQ 테스트")
                .description("FAQ 테스트")
                .visible("Y")
                .build();

        adminFaqDTO = INSTANCE.toDto(adminFaqEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createFaq();
    }

    @Test
    @DisplayName("FAQ 리스트 조회 테스트")
    void FAQ리스트조회테스트() throws Exception {
        // given
        Map<String, Object> faqMap = new HashMap<>();
        faqMap.put("jpaStartPage", 1);
        faqMap.put("size", 3);

        // then
        assertThat(adminFaqJpaService.findFaqsList(faqMap)).isNotEmpty();
    }

    @Test
    @DisplayName("FAQ 리스트 조회 Mockito 테스트")
    void FAQ리스트조회Mockito테스트() throws Exception {
        // given
        Map<String, Object> faqMap = new HashMap<>();
        faqMap.put("jpaStartPage", 1);
        faqMap.put("size", 3);

        List<AdminFaqDTO> returnFaqList = new ArrayList<>();

        returnFaqList.add(AdminFaqDTO.builder().idx(1).title("FAQ테스트").description("FAQ테스트").visible("Y").build());
        returnFaqList.add(AdminFaqDTO.builder().idx(2).title("faqTest").description("faqTest").visible("Y").build());

        // when
        when(mockAdminFaqJpaService.findFaqsList(faqMap)).thenReturn(returnFaqList);
        List<AdminFaqDTO> faqList = mockAdminFaqJpaService.findFaqsList(faqMap);

        // then
        assertAll(
                () -> assertThat(faqList).isNotEmpty(),
                () -> assertThat(faqList).hasSize(2)
        );

        assertThat(faqList.get(0).getIdx()).isEqualTo(returnFaqList.get(0).getIdx());
        assertThat(faqList.get(0).getTitle()).isEqualTo(returnFaqList.get(0).getTitle());
        assertThat(faqList.get(0).getDescription()).isEqualTo(returnFaqList.get(0).getDescription());
        assertThat(faqList.get(0).getVisible()).isEqualTo(returnFaqList.get(0).getVisible());

        // verify
        verify(mockAdminFaqJpaService, times(1)).findFaqsList(faqMap);
        verify(mockAdminFaqJpaService, atLeastOnce()).findFaqsList(faqMap);
        verifyNoMoreInteractions(mockAdminFaqJpaService);

        InOrder inOrder = inOrder(mockAdminFaqJpaService);
        inOrder.verify(mockAdminFaqJpaService).findFaqsList(faqMap);
    }

    @Test
    @DisplayName("FAQ 리스트 조회 BDD 테스트")
    void FAQ리스트조회BDD테스트() throws Exception {
        // given
        Map<String, Object> faqMap = new HashMap<>();
        faqMap.put("jpaStartPage", 1);
        faqMap.put("size", 3);

        List<AdminFaqDTO> returnFaqList = new ArrayList<>();

        returnFaqList.add(AdminFaqDTO.builder().idx(1).title("FAQ테스트").description("FAQ테스트").visible("Y").build());
        returnFaqList.add(AdminFaqDTO.builder().idx(2).title("faqTest").description("faqTest").visible("Y").build());

        // when
        given(mockAdminFaqJpaService.findFaqsList(faqMap)).willReturn(returnFaqList);
        List<AdminFaqDTO> faqList = mockAdminFaqJpaService.findFaqsList(faqMap);

        // then
        assertAll(
                () -> assertThat(faqList).isNotEmpty(),
                () -> assertThat(faqList).hasSize(2)
        );

        assertThat(faqList.get(0).getIdx()).isEqualTo(returnFaqList.get(0).getIdx());
        assertThat(faqList.get(0).getTitle()).isEqualTo(returnFaqList.get(0).getTitle());
        assertThat(faqList.get(0).getDescription()).isEqualTo(returnFaqList.get(0).getDescription());
        assertThat(faqList.get(0).getVisible()).isEqualTo(returnFaqList.get(0).getVisible());

        // verify
        then(mockAdminFaqJpaService).should(times(1)).findFaqsList(faqMap);
        then(mockAdminFaqJpaService).should(atLeastOnce()).findFaqsList(faqMap);
        then(mockAdminFaqJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("FAQ상세Mockito조회테스트")
    void FAQ상세Mockito조회테스트() throws Exception {
        // given
        AdminFaqEntity adminFaqEntity = AdminFaqEntity.builder()
                .idx(1)
                .title("FAQ 테스트")
                .description("FAQ 테스트")
                .visible("Y")
                .build();

        adminFaqDTO = FaqMapper.INSTANCE.toDto(adminFaqEntity);

        // when
        when(mockAdminFaqJpaService.findOneFaq(adminFaqEntity)).thenReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaService.findOneFaq(adminFaqEntity);

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(1);
        assertThat(faqInfo.getTitle()).isEqualTo("FAQ 테스트");
        assertThat(faqInfo.getDescription()).isEqualTo("FAQ 테스트");
        assertThat(faqInfo.getVisible()).isEqualTo("Y");

        // verify
        verify(mockAdminFaqJpaService, times(1)).findOneFaq(adminFaqEntity);
        verify(mockAdminFaqJpaService, atLeastOnce()).findOneFaq(adminFaqEntity);
        verifyNoMoreInteractions(mockAdminFaqJpaService);

        InOrder inOrder = inOrder(mockAdminFaqJpaService);
        inOrder.verify(mockAdminFaqJpaService).findOneFaq(adminFaqEntity);
    }

    @Test
    @DisplayName("FAQ상세BDD조회테스트")
    void FAQ상세BDD조회테스트() throws Exception {
        // given
        AdminFaqEntity adminFaqEntity = AdminFaqEntity.builder()
                .idx(1)
                .title("FAQ 테스트")
                .description("FAQ 테스트")
                .visible("Y")
                .build();

        adminFaqDTO = FaqMapper.INSTANCE.toDto(adminFaqEntity);

        // when
        given(mockAdminFaqJpaService.findOneFaq(adminFaqEntity)).willReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaService.findOneFaq(adminFaqEntity);

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(1);
        assertThat(faqInfo.getTitle()).isEqualTo("FAQ 테스트");
        assertThat(faqInfo.getDescription()).isEqualTo("FAQ 테스트");
        assertThat(faqInfo.getVisible()).isEqualTo("Y");

        // verify
        then(mockAdminFaqJpaService).should(times(1)).findOneFaq(adminFaqEntity);
        then(mockAdminFaqJpaService).should(atLeastOnce()).findOneFaq(adminFaqEntity);
        then(mockAdminFaqJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 or 다음 FAQ 상세 조회 테스트")
    void 이전or다음FAQ상세조회테스트() throws Exception {
        // given
        adminFaqEntity = AdminFaqEntity.builder().idx(2).build();

        // when
        adminFaqDTO = adminFaqJpaService.findOneFaq(adminFaqEntity);

        // 이전 FAQ
        assertThat(adminFaqJpaService.findPrevOneFaq(adminFaqEntity).getIdx()).isEqualTo(1);
        // 다음 FAQ
        assertThat(adminFaqJpaService.findNextOneFaq(adminFaqEntity).getIdx()).isEqualTo(3);
    }

    @Test
    @DisplayName("이전 FAQ 상세 조회 Mockito 테스트")
    void 이전FAQ상세조회Mockito테스트() throws Exception {
        // given
        adminFaqEntity = AdminFaqEntity.builder().idx(2).build();

        // when
        adminFaqDTO = adminFaqJpaService.findPrevOneFaq(adminFaqEntity);

        when(mockAdminFaqJpaService.findPrevOneFaq(adminFaqEntity)).thenReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaService.findPrevOneFaq(adminFaqEntity);

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(1);

        // verify
        verify(mockAdminFaqJpaService, times(1)).findPrevOneFaq(adminFaqEntity);
        verify(mockAdminFaqJpaService, atLeastOnce()).findPrevOneFaq(adminFaqEntity);
        verifyNoMoreInteractions(mockAdminFaqJpaService);

        InOrder inOrder = inOrder(mockAdminFaqJpaService);
        inOrder.verify(mockAdminFaqJpaService).findPrevOneFaq(adminFaqEntity);
    }

    @Test
    @DisplayName("이전 FAQ 상세 조회 BDD 테스트")
    void 이전FAQ상세조회BDD테스트() throws Exception {
        // given
        adminFaqEntity = AdminFaqEntity.builder().idx(2).build();

        // when
        adminFaqDTO = adminFaqJpaService.findPrevOneFaq(adminFaqEntity);

        given(mockAdminFaqJpaService.findPrevOneFaq(adminFaqEntity)).willReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaService.findPrevOneFaq(adminFaqEntity);

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(1);

        // verify
        then(mockAdminFaqJpaService).should(times(1)).findPrevOneFaq(adminFaqEntity);
        then(mockAdminFaqJpaService).should(atLeastOnce()).findPrevOneFaq(adminFaqEntity);
        then(mockAdminFaqJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 FAQ 상세 조회 Mockito 테스트")
    void 다음FAQ상세조회Mockito테스트() throws Exception {
        // given
        adminFaqEntity = AdminFaqEntity.builder().idx(2).build();

        // when
        adminFaqDTO = adminFaqJpaService.findNextOneFaq(adminFaqEntity);

        when(mockAdminFaqJpaService.findNextOneFaq(adminFaqEntity)).thenReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaService.findNextOneFaq(adminFaqEntity);

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(1);

        // verify
        verify(mockAdminFaqJpaService, times(1)).findNextOneFaq(adminFaqEntity);
        verify(mockAdminFaqJpaService, atLeastOnce()).findNextOneFaq(adminFaqEntity);
        verifyNoMoreInteractions(mockAdminFaqJpaService);

        InOrder inOrder = inOrder(mockAdminFaqJpaService);
        inOrder.verify(mockAdminFaqJpaService).findNextOneFaq(adminFaqEntity);
    }

    @Test
    @DisplayName("다음 FAQ 상세 조회 BDD 테스트")
    void 다음FAQ상세조회BDD테스트() throws Exception {
        // given
        adminFaqEntity = AdminFaqEntity.builder().idx(2).build();

        // when
        adminFaqDTO = adminFaqJpaService.findNextOneFaq(adminFaqEntity);

        given(mockAdminFaqJpaService.findNextOneFaq(adminFaqEntity)).willReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaService.findNextOneFaq(adminFaqEntity);

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(1);

        // verify
        then(mockAdminFaqJpaService).should(times(1)).findNextOneFaq(adminFaqEntity);
        then(mockAdminFaqJpaService).should(atLeastOnce()).findNextOneFaq(adminFaqEntity);
        then(mockAdminFaqJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("FAQ등록Mockito테스트")
    void FAQ등록Mockito테스트() throws Exception {
        // given
        adminFaqJpaService.insertFaq(adminFaqEntity);

        // when
        when(mockAdminFaqJpaService.findOneFaq(adminFaqEntity)).thenReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaService.findOneFaq(adminFaqEntity);

        // then
        assertThat(faqInfo.getTitle()).isEqualTo("FAQ 테스트");
        assertThat(faqInfo.getDescription()).isEqualTo("FAQ 테스트");
        assertThat(faqInfo.getVisible()).isEqualTo("Y");

        // verify
        verify(mockAdminFaqJpaService, times(1)).findOneFaq(adminFaqEntity);
        verify(mockAdminFaqJpaService, atLeastOnce()).findOneFaq(adminFaqEntity);
        verifyNoMoreInteractions(mockAdminFaqJpaService);

        InOrder inOrder = inOrder(mockAdminFaqJpaService);
        inOrder.verify(mockAdminFaqJpaService).findOneFaq(adminFaqEntity);
    }

    @Test
    @DisplayName("FAQ등록BDD테스트")
    void FAQ등록BDD테스트() throws Exception {
        // given
        adminFaqJpaService.insertFaq(adminFaqEntity);

        // when
        given(mockAdminFaqJpaService.findOneFaq(adminFaqEntity)).willReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaService.findOneFaq(adminFaqEntity);

        // then
        assertThat(faqInfo.getTitle()).isEqualTo("FAQ 테스트");
        assertThat(faqInfo.getDescription()).isEqualTo("FAQ 테스트");
        assertThat(faqInfo.getVisible()).isEqualTo("Y");

        // verify
        then(mockAdminFaqJpaService).should(times(1)).findOneFaq(adminFaqEntity);
        then(mockAdminFaqJpaService).should(atLeastOnce()).findOneFaq(adminFaqEntity);
        then(mockAdminFaqJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("FAQ수정Mockito테스트")
    void FAQ수정Mockito테스트() throws Exception {
        // given
        Integer idx = adminFaqJpaService.insertFaq(adminFaqEntity).getIdx();

        adminFaqEntity = AdminFaqEntity.builder()
                .idx(idx)
                .title("FAQ 테스트1")
                .description("FAQ 테스트1")
                .visible("Y")
                .build();

        AdminFaqDTO adminFaqDTO = FaqMapper.INSTANCE.toDto(adminFaqEntity);

        adminFaqJpaService.updateFaq(adminFaqEntity);

        // when
        when(mockAdminFaqJpaService.findOneFaq(adminFaqEntity)).thenReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaService.findOneFaq(adminFaqEntity);

        // then
        assertThat(faqInfo.getTitle()).isEqualTo("FAQ 테스트1");
        assertThat(faqInfo.getDescription()).isEqualTo("FAQ 테스트1");

        // verify
        verify(mockAdminFaqJpaService, times(1)).findOneFaq(adminFaqEntity);
        verify(mockAdminFaqJpaService, atLeastOnce()).findOneFaq(adminFaqEntity);
        verifyNoMoreInteractions(mockAdminFaqJpaService);

        InOrder inOrder = inOrder(mockAdminFaqJpaService);
        inOrder.verify(mockAdminFaqJpaService).findOneFaq(adminFaqEntity);
    }

    @Test
    @DisplayName("FAQ수정BDD테스트")
    void FAQ수정BDD테스트() throws Exception {
        // given
        Integer idx = adminFaqJpaService.insertFaq(adminFaqEntity).getIdx();

        adminFaqEntity = AdminFaqEntity.builder()
                .idx(idx)
                .title("FAQ 테스트1")
                .description("FAQ 테스트1")
                .visible("Y")
                .build();

        AdminFaqDTO adminFaqDTO = FaqMapper.INSTANCE.toDto(adminFaqEntity);

        adminFaqJpaService.updateFaq(adminFaqEntity);

        // when
        when(mockAdminFaqJpaService.findOneFaq(adminFaqEntity)).thenReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaService.findOneFaq(adminFaqEntity);

        // then
        assertThat(faqInfo.getTitle()).isEqualTo("FAQ 테스트1");
        assertThat(faqInfo.getDescription()).isEqualTo("FAQ 테스트1");

        // verify
        then(mockAdminFaqJpaService).should(times(1)).findOneFaq(adminFaqEntity);
        then(mockAdminFaqJpaService).should(atLeastOnce()).findOneFaq(adminFaqEntity);
        then(mockAdminFaqJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("FAQ삭제테스트")
    void FAQ삭제테스트() throws Exception {
        // given
        adminFaqJpaService.insertFaq(adminFaqEntity);

        Integer entityIdx = adminFaqEntity.getIdx();
        Integer idx = adminFaqJpaService.deleteFaq(adminFaqEntity.getIdx());

        // then
        assertThat(entityIdx).isEqualTo(idx);
    }

    @Test
    @DisplayName("FAQ삭제Mockito테스트")
    void FAQ삭제Mockito테스트() throws Exception {
        // given
        adminFaqJpaService.insertFaq(adminFaqEntity);
        adminFaqDTO = FaqMapper.INSTANCE.toDto(adminFaqEntity);

        // when
        when(mockAdminFaqJpaService.findOneFaq(adminFaqEntity)).thenReturn(adminFaqDTO);
        Integer deleteIdx = adminFaqJpaService.deleteFaq(adminFaqEntity.getIdx());

        // then
        assertThat(mockAdminFaqJpaService.findOneFaq(adminFaqEntity).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockAdminFaqJpaService, times(1)).findOneFaq(adminFaqEntity);
        verify(mockAdminFaqJpaService, atLeastOnce()).findOneFaq(adminFaqEntity);
        verifyNoMoreInteractions(mockAdminFaqJpaService);

        InOrder inOrder = inOrder(mockAdminFaqJpaService);
        inOrder.verify(mockAdminFaqJpaService).findOneFaq(adminFaqEntity);
    }

    @Test
    @DisplayName("FAQ삭제BDD테스트")
    void FAQ삭제BDD테스트() throws Exception {
        // given
        adminFaqJpaService.insertFaq(adminFaqEntity);
        adminFaqDTO = FaqMapper.INSTANCE.toDto(adminFaqEntity);

        // when
        when(mockAdminFaqJpaService.findOneFaq(adminFaqEntity)).thenReturn(adminFaqDTO);
        Integer deleteIdx = adminFaqJpaService.deleteFaq(adminFaqEntity.getIdx());

        // then
        assertThat(mockAdminFaqJpaService.findOneFaq(adminFaqEntity).getIdx()).isEqualTo(deleteIdx);

        // verify
        then(mockAdminFaqJpaService).should(times(1)).findOneFaq(adminFaqEntity);
        then(mockAdminFaqJpaService).should(atLeastOnce()).findOneFaq(adminFaqEntity);
        then(mockAdminFaqJpaService).shouldHaveNoMoreInteractions();
    }
}