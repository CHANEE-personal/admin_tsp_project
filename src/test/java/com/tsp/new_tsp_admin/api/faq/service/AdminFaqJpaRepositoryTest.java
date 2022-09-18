package com.tsp.new_tsp_admin.api.faq.service;

import com.tsp.new_tsp_admin.api.domain.faq.AdminFaqDTO;
import com.tsp.new_tsp_admin.api.domain.faq.AdminFaqEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import static com.tsp.new_tsp_admin.api.faq.mapper.FaqMapper.INSTANCE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertAll;
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
@DisplayName("FAQ Repository Test")
class AdminFaqJpaRepositoryTest {
    @Mock
    private AdminFaqJpaRepository mockAdminFaqJpaRepository;
    private final AdminFaqJpaRepository adminFaqJpaRepository;
    private final EntityManager em;

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
    @Disabled
    @DisplayName("FAQ리스트조회테스트")
    void FAQ리스트조회테스트() {
        // given
        Map<String, Object> faqMap = new HashMap<>();
        faqMap.put("jpaStartPage", 1);
        faqMap.put("size", 3);

        // then
        assertThat(adminFaqJpaRepository.findFaqsList(faqMap)).isNotEmpty();
    }

    @Test
    @Disabled
    @DisplayName("FAQ상세조회테스트")
    void FAQ상세조회테스트() {
        // given
        adminFaqEntity = AdminFaqEntity.builder().idx(1).build();

        // when
        adminFaqDTO = adminFaqJpaRepository.findOneFaq(adminFaqEntity);

        // then
        assertAll(() -> assertThat(adminFaqDTO.getIdx()).isEqualTo(1),
                () -> {
                    assertThat(adminFaqDTO.getTitle()).isEqualTo("테스트1");
                    assertNotNull(adminFaqDTO.getTitle());
                },
                () -> {
                    assertThat(adminFaqDTO.getDescription()).isEqualTo("테스트1");
                    assertNotNull(adminFaqDTO.getDescription());
                },
                () -> {
                    assertThat(adminFaqDTO.getVisible()).isEqualTo("Y");
                    assertNotNull(adminFaqDTO.getVisible());
                });
    }

    @Test
    @DisplayName("FAQMockito조회테스트")
    void FAQMockito조회테스트() {
        // given
        Map<String, Object> faqMap = new HashMap<>();
        faqMap.put("jpaStartPage", 1);
        faqMap.put("size", 3);

        List<AdminFaqDTO> faqList = new ArrayList<>();
        faqList.add(AdminFaqDTO.builder().idx(1).title("FAQ 테스트")
                .description("FAQ 테스트").build());

        // when
        when(mockAdminFaqJpaRepository.findFaqsList(faqMap)).thenReturn(faqList);
        List<AdminFaqDTO> newFaqList = mockAdminFaqJpaRepository.findFaqsList(faqMap);

        // then
        assertThat(newFaqList.get(0).getIdx()).isEqualTo(faqList.get(0).getIdx());
        assertThat(newFaqList.get(0).getTitle()).isEqualTo(faqList.get(0).getTitle());
        assertThat(newFaqList.get(0).getDescription()).isEqualTo(faqList.get(0).getDescription());
        assertThat(newFaqList.get(0).getVisible()).isEqualTo(faqList.get(0).getVisible());

        // verify
        verify(mockAdminFaqJpaRepository, times(1)).findFaqsList(faqMap);
        verify(mockAdminFaqJpaRepository, atLeastOnce()).findFaqsList(faqMap);
        verifyNoMoreInteractions(mockAdminFaqJpaRepository);

        InOrder inOrder = inOrder(mockAdminFaqJpaRepository);
        inOrder.verify(mockAdminFaqJpaRepository).findFaqsList(faqMap);
    }

    @Test
    @DisplayName("FAQBDD조회테스트")
    void FAQBDD조회테스트() {
        // given
        Map<String, Object> faqMap = new HashMap<>();
        faqMap.put("jpaStartPage", 1);
        faqMap.put("size", 3);

        List<AdminFaqDTO> faqList = new ArrayList<>();
        faqList.add(AdminFaqDTO.builder().idx(1).title("FAQ 테스트")
                .description("FAQ 테스트").build());

        // when
        given(mockAdminFaqJpaRepository.findFaqsList(faqMap)).willReturn(faqList);
        List<AdminFaqDTO> newNoticeList = mockAdminFaqJpaRepository.findFaqsList(faqMap);

        // then
        assertThat(newNoticeList.get(0).getIdx()).isEqualTo(faqList.get(0).getIdx());
        assertThat(newNoticeList.get(0).getTitle()).isEqualTo(faqList.get(0).getTitle());
        assertThat(newNoticeList.get(0).getDescription()).isEqualTo(faqList.get(0).getDescription());
        assertThat(newNoticeList.get(0).getVisible()).isEqualTo(faqList.get(0).getVisible());

        // verify
        then(mockAdminFaqJpaRepository).should(times(1)).findFaqsList(faqMap);
        then(mockAdminFaqJpaRepository).should(atLeastOnce()).findFaqsList(faqMap);
        then(mockAdminFaqJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("FAQ상세Mockito조회테스트")
    void FAQ상세Mockito조회테스트() {
        // given
        AdminFaqEntity adminFaqEntity = AdminFaqEntity.builder()
                .idx(1)
                .title("FAQ 테스트")
                .description("FAQ 테스트")
                .visible("Y")
                .build();

        adminFaqDTO = INSTANCE.toDto(adminFaqEntity);

        // when
        when(mockAdminFaqJpaRepository.findOneFaq(adminFaqEntity)).thenReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaRepository.findOneFaq(adminFaqEntity);

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(1);
        assertThat(faqInfo.getTitle()).isEqualTo("FAQ 테스트");
        assertThat(faqInfo.getDescription()).isEqualTo("FAQ 테스트");
        assertThat(faqInfo.getVisible()).isEqualTo("Y");

        // verify
        verify(mockAdminFaqJpaRepository, times(1)).findOneFaq(adminFaqEntity);
        verify(mockAdminFaqJpaRepository, atLeastOnce()).findOneFaq(adminFaqEntity);
        verifyNoMoreInteractions(mockAdminFaqJpaRepository);

        InOrder inOrder = inOrder(mockAdminFaqJpaRepository);
        inOrder.verify(mockAdminFaqJpaRepository).findOneFaq(adminFaqEntity);
    }

    @Test
    @DisplayName("FAQ상세BDD조회테스트")
    void FAQ상세BDD조회테스트() {
        // given
        AdminFaqEntity adminFaqEntity = AdminFaqEntity.builder()
                .idx(1)
                .title("FAQ 테스트")
                .description("FAQ 테스트")
                .visible("Y")
                .build();

        adminFaqDTO = INSTANCE.toDto(adminFaqEntity);

        // when
        given(mockAdminFaqJpaRepository.findOneFaq(adminFaqEntity)).willReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaRepository.findOneFaq(adminFaqEntity);

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(1);
        assertThat(faqInfo.getTitle()).isEqualTo("FAQ 테스트");
        assertThat(faqInfo.getDescription()).isEqualTo("FAQ 테스트");
        assertThat(faqInfo.getVisible()).isEqualTo("Y");

        // verify
        then(mockAdminFaqJpaRepository).should(times(1)).findOneFaq(adminFaqEntity);
        then(mockAdminFaqJpaRepository).should(atLeastOnce()).findOneFaq(adminFaqEntity);
        then(mockAdminFaqJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 or 다음 FAQ 상세 조회 테스트")
    void 이전or다음FAQ상세조회테스트() {
        // given
        adminFaqEntity = AdminFaqEntity.builder().idx(2).build();

        // when
        adminFaqDTO = adminFaqJpaRepository.findOneFaq(adminFaqEntity);

        // 이전 FAQ
        assertThat(adminFaqJpaRepository.findPrevOneFaq(adminFaqEntity).getIdx()).isEqualTo(1);
        // 다음 FAQ
        assertThat(adminFaqJpaRepository.findNextOneFaq(adminFaqEntity).getIdx()).isEqualTo(3);
    }

    @Test
    @DisplayName("이전 FAQ 상세 조회 Mockito 테스트")
    void 이전FAQ상세조회Mockito테스트() {
        // given
        adminFaqEntity = AdminFaqEntity.builder().idx(2).build();

        // when
        adminFaqDTO = adminFaqJpaRepository.findPrevOneFaq(adminFaqEntity);

        when(mockAdminFaqJpaRepository.findPrevOneFaq(adminFaqEntity)).thenReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaRepository.findPrevOneFaq(adminFaqEntity);

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(1);

        // verify
        verify(mockAdminFaqJpaRepository, times(1)).findPrevOneFaq(adminFaqEntity);
        verify(mockAdminFaqJpaRepository, atLeastOnce()).findPrevOneFaq(adminFaqEntity);
        verifyNoMoreInteractions(mockAdminFaqJpaRepository);

        InOrder inOrder = inOrder(mockAdminFaqJpaRepository);
        inOrder.verify(mockAdminFaqJpaRepository).findPrevOneFaq(adminFaqEntity);
    }

    @Test
    @DisplayName("이전 FAQ 상세 조회 BDD 테스트")
    void 이전FAQ상세조회BDD테스트() {
        // given
        adminFaqEntity = AdminFaqEntity.builder().idx(2).build();

        // when
        adminFaqDTO = adminFaqJpaRepository.findPrevOneFaq(adminFaqEntity);

        given(mockAdminFaqJpaRepository.findPrevOneFaq(adminFaqEntity)).willReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaRepository.findPrevOneFaq(adminFaqEntity);

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(1);

        // verify
        then(mockAdminFaqJpaRepository).should(times(1)).findPrevOneFaq(adminFaqEntity);
        then(mockAdminFaqJpaRepository).should(atLeastOnce()).findPrevOneFaq(adminFaqEntity);
        then(mockAdminFaqJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 공지사항 상세 조회 Mockito 테스트")
    void 다음공지사항상세조회Mockito테스트() {
        // given
        adminFaqEntity = AdminFaqEntity.builder().idx(2).build();

        // when
        adminFaqDTO = adminFaqJpaRepository.findNextOneFaq(adminFaqEntity);

        when(mockAdminFaqJpaRepository.findNextOneFaq(adminFaqEntity)).thenReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaRepository.findNextOneFaq(adminFaqEntity);

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(1);

        // verify
        verify(mockAdminFaqJpaRepository, times(1)).findNextOneFaq(adminFaqEntity);
        verify(mockAdminFaqJpaRepository, atLeastOnce()).findNextOneFaq(adminFaqEntity);
        verifyNoMoreInteractions(mockAdminFaqJpaRepository);

        InOrder inOrder = inOrder(mockAdminFaqJpaRepository);
        inOrder.verify(mockAdminFaqJpaRepository).findNextOneFaq(adminFaqEntity);
    }

    @Test
    @DisplayName("다음 공지사항 상세 조회 BDD 테스트")
    void 다음공지사항상세조회BDD테스트() {
        // given
        adminFaqEntity = AdminFaqEntity.builder().idx(2).build();

        // when
        adminFaqDTO = adminFaqJpaRepository.findNextOneFaq(adminFaqEntity);

        given(mockAdminFaqJpaRepository.findNextOneFaq(adminFaqEntity)).willReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaRepository.findNextOneFaq(adminFaqEntity);

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(1);

        // verify
        then(mockAdminFaqJpaRepository).should(times(1)).findNextOneFaq(adminFaqEntity);
        then(mockAdminFaqJpaRepository).should(atLeastOnce()).findNextOneFaq(adminFaqEntity);
        then(mockAdminFaqJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("FAQ등록Mockito테스트")
    void FAQ등록Mockito테스트() {
        // given
        adminFaqJpaRepository.insertFaq(adminFaqEntity);

        // when
        when(mockAdminFaqJpaRepository.findOneFaq(adminFaqEntity)).thenReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaRepository.findOneFaq(adminFaqEntity);

        // then
        assertThat(faqInfo.getTitle()).isEqualTo("FAQ 테스트");
        assertThat(faqInfo.getDescription()).isEqualTo("FAQ 테스트");
        assertThat(faqInfo.getVisible()).isEqualTo("Y");

        // verify
        verify(mockAdminFaqJpaRepository, times(1)).findOneFaq(adminFaqEntity);
        verify(mockAdminFaqJpaRepository, atLeastOnce()).findOneFaq(adminFaqEntity);
        verifyNoMoreInteractions(mockAdminFaqJpaRepository);

        InOrder inOrder = inOrder(mockAdminFaqJpaRepository);
        inOrder.verify(mockAdminFaqJpaRepository).findOneFaq(adminFaqEntity);
    }

    @Test
    @DisplayName("FAQ등록BDD테스트")
    void FAQ등록BDD테스트() {
        // given
        adminFaqJpaRepository.insertFaq(adminFaqEntity);

        // when
        given(mockAdminFaqJpaRepository.findOneFaq(adminFaqEntity)).willReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaRepository.findOneFaq(adminFaqEntity);

        // then
        assertThat(faqInfo.getTitle()).isEqualTo("FAQ 테스트");
        assertThat(faqInfo.getDescription()).isEqualTo("FAQ 테스트");
        assertThat(faqInfo.getVisible()).isEqualTo("Y");

        // verify
        then(mockAdminFaqJpaRepository).should(times(1)).findOneFaq(adminFaqEntity);
        then(mockAdminFaqJpaRepository).should(atLeastOnce()).findOneFaq(adminFaqEntity);
        then(mockAdminFaqJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("FAQ수정Mockito테스트")
    void FAQ수정Mockito테스트() {
        // given
        Integer idx = adminFaqJpaRepository.insertFaq(adminFaqEntity).getIdx();

        adminFaqEntity = AdminFaqEntity.builder()
                .idx(idx)
                .title("FAQ 테스트1")
                .description("FAQ 테스트1")
                .visible("Y")
                .build();

        AdminFaqDTO adminFaqDTO = INSTANCE.toDto(adminFaqEntity);

        adminFaqJpaRepository.updateFaq(adminFaqEntity);

        // when
        when(mockAdminFaqJpaRepository.findOneFaq(adminFaqEntity)).thenReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaRepository.findOneFaq(adminFaqEntity);

        // then
        assertThat(faqInfo.getTitle()).isEqualTo("FAQ 테스트1");
        assertThat(faqInfo.getDescription()).isEqualTo("FAQ 테스트1");

        // verify
        verify(mockAdminFaqJpaRepository, times(1)).findOneFaq(adminFaqEntity);
        verify(mockAdminFaqJpaRepository, atLeastOnce()).findOneFaq(adminFaqEntity);
        verifyNoMoreInteractions(mockAdminFaqJpaRepository);

        InOrder inOrder = inOrder(mockAdminFaqJpaRepository);
        inOrder.verify(mockAdminFaqJpaRepository).findOneFaq(adminFaqEntity);
    }

    @Test
    @DisplayName("FAQ수정BDD테스트")
    void FAQ수정BDD테스트() {
        // given
        Integer idx = adminFaqJpaRepository.insertFaq(adminFaqEntity).getIdx();

        adminFaqEntity = AdminFaqEntity.builder()
                .idx(idx)
                .title("FAQ 테스트1")
                .description("FAQ 테스트1")
                .visible("Y")
                .build();

        AdminFaqDTO adminFaqDTO = INSTANCE.toDto(adminFaqEntity);

        adminFaqJpaRepository.updateFaq(adminFaqEntity);

        // when
        when(mockAdminFaqJpaRepository.findOneFaq(adminFaqEntity)).thenReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaRepository.findOneFaq(adminFaqEntity);

        // then
        assertThat(faqInfo.getTitle()).isEqualTo("FAQ 테스트1");
        assertThat(faqInfo.getDescription()).isEqualTo("FAQ 테스트1");

        // verify
        then(mockAdminFaqJpaRepository).should(times(1)).findOneFaq(adminFaqEntity);
        then(mockAdminFaqJpaRepository).should(atLeastOnce()).findOneFaq(adminFaqEntity);
        then(mockAdminFaqJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("FAQ삭제테스트")
    void FAQ삭제테스트() {
        // given
        em.persist(adminFaqEntity);

        Integer entityIdx = adminFaqEntity.getIdx();
        Integer idx = adminFaqJpaRepository.deleteFaq(adminFaqEntity.getIdx());

        // then
        assertThat(entityIdx).isEqualTo(idx);
    }

    @Test
    @DisplayName("FAQ삭제Mockito테스트")
    void FAQ삭제Mockito테스트() {
        // given
        em.persist(adminFaqEntity);
        adminFaqDTO = INSTANCE.toDto(adminFaqEntity);

        // when
        when(mockAdminFaqJpaRepository.findOneFaq(adminFaqEntity)).thenReturn(adminFaqDTO);
        Integer deleteIdx = adminFaqJpaRepository.deleteFaq(adminFaqEntity.getIdx());

        // then
        assertThat(mockAdminFaqJpaRepository.findOneFaq(adminFaqEntity).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockAdminFaqJpaRepository, times(1)).findOneFaq(adminFaqEntity);
        verify(mockAdminFaqJpaRepository, atLeastOnce()).findOneFaq(adminFaqEntity);
        verifyNoMoreInteractions(mockAdminFaqJpaRepository);

        InOrder inOrder = inOrder(mockAdminFaqJpaRepository);
        inOrder.verify(mockAdminFaqJpaRepository).findOneFaq(adminFaqEntity);
    }

    @Test
    @DisplayName("FAQ삭제BDD테스트")
    void FAQ삭제BDD테스트() {
        // given
        em.persist(adminFaqEntity);
        adminFaqDTO = INSTANCE.toDto(adminFaqEntity);

        // when
        when(mockAdminFaqJpaRepository.findOneFaq(adminFaqEntity)).thenReturn(adminFaqDTO);
        Integer deleteIdx = adminFaqJpaRepository.deleteFaq(adminFaqEntity.getIdx());

        // then
        assertThat(mockAdminFaqJpaRepository.findOneFaq(adminFaqEntity).getIdx()).isEqualTo(deleteIdx);

        // verify
        then(mockAdminFaqJpaRepository).should(times(1)).findOneFaq(adminFaqEntity);
        then(mockAdminFaqJpaRepository).should(atLeastOnce()).findOneFaq(adminFaqEntity);
        then(mockAdminFaqJpaRepository).shouldHaveNoMoreInteractions();
    }
}