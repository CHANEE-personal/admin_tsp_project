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

        adminFaqDTO = AdminFaqEntity.toDto(adminFaqEntity);
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
        assertThat(adminFaqJpaRepository.findFaqList(faqMap)).isNotEmpty();
    }

    @Test
    @Disabled
    @DisplayName("FAQ상세조회테스트")
    void FAQ상세조회테스트() {
        // given
        adminFaqEntity = AdminFaqEntity.builder().idx(1L).build();

        // when
        adminFaqDTO = adminFaqJpaRepository.findOneFaq(adminFaqEntity.getIdx());

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
        faqList.add(AdminFaqDTO.builder().idx(1L).title("FAQ 테스트")
                .description("FAQ 테스트").build());

        // when
        when(mockAdminFaqJpaRepository.findFaqList(faqMap)).thenReturn(faqList);
        List<AdminFaqDTO> newFaqList = mockAdminFaqJpaRepository.findFaqList(faqMap);

        // then
        assertThat(newFaqList.get(0).getIdx()).isEqualTo(faqList.get(0).getIdx());
        assertThat(newFaqList.get(0).getTitle()).isEqualTo(faqList.get(0).getTitle());
        assertThat(newFaqList.get(0).getDescription()).isEqualTo(faqList.get(0).getDescription());
        assertThat(newFaqList.get(0).getVisible()).isEqualTo(faqList.get(0).getVisible());

        // verify
        verify(mockAdminFaqJpaRepository, times(1)).findFaqList(faqMap);
        verify(mockAdminFaqJpaRepository, atLeastOnce()).findFaqList(faqMap);
        verifyNoMoreInteractions(mockAdminFaqJpaRepository);

        InOrder inOrder = inOrder(mockAdminFaqJpaRepository);
        inOrder.verify(mockAdminFaqJpaRepository).findFaqList(faqMap);
    }

    @Test
    @DisplayName("FAQBDD조회테스트")
    void FAQBDD조회테스트() {
        // given
        Map<String, Object> faqMap = new HashMap<>();
        faqMap.put("jpaStartPage", 1);
        faqMap.put("size", 3);

        List<AdminFaqDTO> faqList = new ArrayList<>();
        faqList.add(AdminFaqDTO.builder().idx(1L).title("FAQ 테스트")
                .description("FAQ 테스트").build());

        // when
        given(mockAdminFaqJpaRepository.findFaqList(faqMap)).willReturn(faqList);
        List<AdminFaqDTO> newNoticeList = mockAdminFaqJpaRepository.findFaqList(faqMap);

        // then
        assertThat(newNoticeList.get(0).getIdx()).isEqualTo(faqList.get(0).getIdx());
        assertThat(newNoticeList.get(0).getTitle()).isEqualTo(faqList.get(0).getTitle());
        assertThat(newNoticeList.get(0).getDescription()).isEqualTo(faqList.get(0).getDescription());
        assertThat(newNoticeList.get(0).getVisible()).isEqualTo(faqList.get(0).getVisible());

        // verify
        then(mockAdminFaqJpaRepository).should(times(1)).findFaqList(faqMap);
        then(mockAdminFaqJpaRepository).should(atLeastOnce()).findFaqList(faqMap);
        then(mockAdminFaqJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("FAQ상세Mockito조회테스트")
    void FAQ상세Mockito조회테스트() {
        // given
        AdminFaqEntity adminFaqEntity = AdminFaqEntity.builder()
                .idx(1L)
                .title("FAQ 테스트")
                .description("FAQ 테스트")
                .visible("Y")
                .build();

        adminFaqDTO = AdminFaqEntity.toDto(adminFaqEntity);

        // when
        when(mockAdminFaqJpaRepository.findOneFaq(adminFaqEntity.getIdx())).thenReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaRepository.findOneFaq(adminFaqEntity.getIdx());

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(1);
        assertThat(faqInfo.getTitle()).isEqualTo("FAQ 테스트");
        assertThat(faqInfo.getDescription()).isEqualTo("FAQ 테스트");
        assertThat(faqInfo.getVisible()).isEqualTo("Y");

        // verify
        verify(mockAdminFaqJpaRepository, times(1)).findOneFaq(adminFaqEntity.getIdx());
        verify(mockAdminFaqJpaRepository, atLeastOnce()).findOneFaq(adminFaqEntity.getIdx());
        verifyNoMoreInteractions(mockAdminFaqJpaRepository);

        InOrder inOrder = inOrder(mockAdminFaqJpaRepository);
        inOrder.verify(mockAdminFaqJpaRepository).findOneFaq(adminFaqEntity.getIdx());
    }

    @Test
    @DisplayName("FAQ상세BDD조회테스트")
    void FAQ상세BDD조회테스트() {
        // given
        AdminFaqEntity adminFaqEntity = AdminFaqEntity.builder()
                .idx(1L)
                .title("FAQ 테스트")
                .description("FAQ 테스트")
                .visible("Y")
                .build();

        adminFaqDTO = AdminFaqEntity.toDto(adminFaqEntity);

        // when
        given(mockAdminFaqJpaRepository.findOneFaq(adminFaqEntity.getIdx())).willReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaRepository.findOneFaq(adminFaqEntity.getIdx());

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(1);
        assertThat(faqInfo.getTitle()).isEqualTo("FAQ 테스트");
        assertThat(faqInfo.getDescription()).isEqualTo("FAQ 테스트");
        assertThat(faqInfo.getVisible()).isEqualTo("Y");

        // verify
        then(mockAdminFaqJpaRepository).should(times(1)).findOneFaq(adminFaqEntity.getIdx());
        then(mockAdminFaqJpaRepository).should(atLeastOnce()).findOneFaq(adminFaqEntity.getIdx());
        then(mockAdminFaqJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 or 다음 FAQ 상세 조회 테스트")
    void 이전or다음FAQ상세조회테스트() {
        // given
        adminFaqEntity = AdminFaqEntity.builder().idx(2L).build();

        // when
        adminFaqDTO = adminFaqJpaRepository.findOneFaq(adminFaqEntity.getIdx());

        // 이전 FAQ
        assertThat(adminFaqJpaRepository.findPrevOneFaq(adminFaqEntity.getIdx()).getIdx()).isEqualTo(1);
        // 다음 FAQ
        assertThat(adminFaqJpaRepository.findNextOneFaq(adminFaqEntity.getIdx()).getIdx()).isEqualTo(3);
    }

    @Test
    @DisplayName("이전 FAQ 상세 조회 Mockito 테스트")
    void 이전FAQ상세조회Mockito테스트() {
        // given
        adminFaqEntity = AdminFaqEntity.builder().idx(2L).build();

        // when
        adminFaqDTO = adminFaqJpaRepository.findPrevOneFaq(adminFaqEntity.getIdx());

        when(mockAdminFaqJpaRepository.findPrevOneFaq(adminFaqEntity.getIdx())).thenReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaRepository.findPrevOneFaq(adminFaqEntity.getIdx());

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(1);

        // verify
        verify(mockAdminFaqJpaRepository, times(1)).findPrevOneFaq(adminFaqEntity.getIdx());
        verify(mockAdminFaqJpaRepository, atLeastOnce()).findPrevOneFaq(adminFaqEntity.getIdx());
        verifyNoMoreInteractions(mockAdminFaqJpaRepository);

        InOrder inOrder = inOrder(mockAdminFaqJpaRepository);
        inOrder.verify(mockAdminFaqJpaRepository).findPrevOneFaq(adminFaqEntity.getIdx());
    }

    @Test
    @DisplayName("이전 FAQ 상세 조회 BDD 테스트")
    void 이전FAQ상세조회BDD테스트() {
        // given
        adminFaqEntity = AdminFaqEntity.builder().idx(2L).build();

        // when
        adminFaqDTO = adminFaqJpaRepository.findPrevOneFaq(adminFaqEntity.getIdx());

        given(mockAdminFaqJpaRepository.findPrevOneFaq(adminFaqEntity.getIdx())).willReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaRepository.findPrevOneFaq(adminFaqEntity.getIdx());

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(1);

        // verify
        then(mockAdminFaqJpaRepository).should(times(1)).findPrevOneFaq(adminFaqEntity.getIdx());
        then(mockAdminFaqJpaRepository).should(atLeastOnce()).findPrevOneFaq(adminFaqEntity.getIdx());
        then(mockAdminFaqJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 FAQ 상세 조회 Mockito 테스트")
    void 다음FAQ상세조회Mockito테스트() {
        // given
        adminFaqEntity = AdminFaqEntity.builder().idx(2L).build();

        // when
        adminFaqDTO = adminFaqJpaRepository.findNextOneFaq(adminFaqEntity.getIdx());

        when(mockAdminFaqJpaRepository.findNextOneFaq(adminFaqEntity.getIdx())).thenReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaRepository.findNextOneFaq(adminFaqEntity.getIdx());

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(1);

        // verify
        verify(mockAdminFaqJpaRepository, times(1)).findNextOneFaq(adminFaqEntity.getIdx());
        verify(mockAdminFaqJpaRepository, atLeastOnce()).findNextOneFaq(adminFaqEntity.getIdx());
        verifyNoMoreInteractions(mockAdminFaqJpaRepository);

        InOrder inOrder = inOrder(mockAdminFaqJpaRepository);
        inOrder.verify(mockAdminFaqJpaRepository).findNextOneFaq(adminFaqEntity.getIdx());
    }

    @Test
    @DisplayName("다음 FAQ 상세 조회 BDD 테스트")
    void 다음FAQ상세조회BDD테스트() {
        // given
        adminFaqEntity = AdminFaqEntity.builder().idx(2L).build();

        // when
        adminFaqDTO = adminFaqJpaRepository.findNextOneFaq(adminFaqEntity.getIdx());

        given(mockAdminFaqJpaRepository.findNextOneFaq(adminFaqEntity.getIdx())).willReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaRepository.findNextOneFaq(adminFaqEntity.getIdx());

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(1);

        // verify
        then(mockAdminFaqJpaRepository).should(times(1)).findNextOneFaq(adminFaqEntity.getIdx());
        then(mockAdminFaqJpaRepository).should(atLeastOnce()).findNextOneFaq(adminFaqEntity.getIdx());
        then(mockAdminFaqJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("FAQ등록Mockito테스트")
    void FAQ등록Mockito테스트() {
        // given
        adminFaqJpaRepository.insertFaq(adminFaqEntity);

        // when
        when(mockAdminFaqJpaRepository.findOneFaq(adminFaqEntity.getIdx())).thenReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaRepository.findOneFaq(adminFaqEntity.getIdx());

        // then
        assertThat(faqInfo.getTitle()).isEqualTo("FAQ 테스트");
        assertThat(faqInfo.getDescription()).isEqualTo("FAQ 테스트");
        assertThat(faqInfo.getVisible()).isEqualTo("Y");

        // verify
        verify(mockAdminFaqJpaRepository, times(1)).findOneFaq(adminFaqEntity.getIdx());
        verify(mockAdminFaqJpaRepository, atLeastOnce()).findOneFaq(adminFaqEntity.getIdx());
        verifyNoMoreInteractions(mockAdminFaqJpaRepository);

        InOrder inOrder = inOrder(mockAdminFaqJpaRepository);
        inOrder.verify(mockAdminFaqJpaRepository).findOneFaq(adminFaqEntity.getIdx());
    }

    @Test
    @DisplayName("FAQ등록BDD테스트")
    void FAQ등록BDD테스트() {
        // given
        adminFaqJpaRepository.insertFaq(adminFaqEntity);

        // when
        given(mockAdminFaqJpaRepository.findOneFaq(adminFaqEntity.getIdx())).willReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaRepository.findOneFaq(adminFaqEntity.getIdx());

        // then
        assertThat(faqInfo.getTitle()).isEqualTo("FAQ 테스트");
        assertThat(faqInfo.getDescription()).isEqualTo("FAQ 테스트");
        assertThat(faqInfo.getVisible()).isEqualTo("Y");

        // verify
        then(mockAdminFaqJpaRepository).should(times(1)).findOneFaq(adminFaqEntity.getIdx());
        then(mockAdminFaqJpaRepository).should(atLeastOnce()).findOneFaq(adminFaqEntity.getIdx());
        then(mockAdminFaqJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("FAQ수정Mockito테스트")
    void FAQ수정Mockito테스트() {
        // given
        Long idx = adminFaqJpaRepository.insertFaq(adminFaqEntity).getIdx();

        adminFaqEntity = AdminFaqEntity.builder()
                .idx(idx)
                .title("FAQ 테스트1")
                .description("FAQ 테스트1")
                .visible("Y")
                .build();

        AdminFaqDTO adminFaqDTO = AdminFaqEntity.toDto(adminFaqEntity);

        adminFaqJpaRepository.updateFaq(adminFaqEntity);

        // when
        when(mockAdminFaqJpaRepository.findOneFaq(adminFaqEntity.getIdx())).thenReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaRepository.findOneFaq(adminFaqEntity.getIdx());

        // then
        assertThat(faqInfo.getTitle()).isEqualTo("FAQ 테스트1");
        assertThat(faqInfo.getDescription()).isEqualTo("FAQ 테스트1");

        // verify
        verify(mockAdminFaqJpaRepository, times(1)).findOneFaq(adminFaqEntity.getIdx());
        verify(mockAdminFaqJpaRepository, atLeastOnce()).findOneFaq(adminFaqEntity.getIdx());
        verifyNoMoreInteractions(mockAdminFaqJpaRepository);

        InOrder inOrder = inOrder(mockAdminFaqJpaRepository);
        inOrder.verify(mockAdminFaqJpaRepository).findOneFaq(adminFaqEntity.getIdx());
    }

    @Test
    @DisplayName("FAQ수정BDD테스트")
    void FAQ수정BDD테스트() {
        // given
        Long idx = adminFaqJpaRepository.insertFaq(adminFaqEntity).getIdx();

        adminFaqEntity = AdminFaqEntity.builder()
                .idx(idx)
                .title("FAQ 테스트1")
                .description("FAQ 테스트1")
                .visible("Y")
                .build();

        AdminFaqDTO adminFaqDTO = AdminFaqEntity.toDto(adminFaqEntity);

        adminFaqJpaRepository.updateFaq(adminFaqEntity);

        // when
        when(mockAdminFaqJpaRepository.findOneFaq(adminFaqEntity.getIdx())).thenReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaRepository.findOneFaq(adminFaqEntity.getIdx());

        // then
        assertThat(faqInfo.getTitle()).isEqualTo("FAQ 테스트1");
        assertThat(faqInfo.getDescription()).isEqualTo("FAQ 테스트1");

        // verify
        then(mockAdminFaqJpaRepository).should(times(1)).findOneFaq(adminFaqEntity.getIdx());
        then(mockAdminFaqJpaRepository).should(atLeastOnce()).findOneFaq(adminFaqEntity.getIdx());
        then(mockAdminFaqJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("FAQ삭제테스트")
    void FAQ삭제테스트() {
        // given
        em.persist(adminFaqEntity);

        Long entityIdx = adminFaqEntity.getIdx();
        Long idx = adminFaqJpaRepository.deleteFaq(adminFaqEntity.getIdx());

        // then
        assertThat(entityIdx).isEqualTo(idx);
    }

    @Test
    @DisplayName("FAQ삭제Mockito테스트")
    void FAQ삭제Mockito테스트() {
        // given
        em.persist(adminFaqEntity);
        adminFaqDTO = AdminFaqEntity.toDto(adminFaqEntity);

        // when
        when(mockAdminFaqJpaRepository.findOneFaq(adminFaqEntity.getIdx())).thenReturn(adminFaqDTO);
        Long deleteIdx = adminFaqJpaRepository.deleteFaq(adminFaqEntity.getIdx());

        // then
        assertThat(mockAdminFaqJpaRepository.findOneFaq(adminFaqEntity.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        verify(mockAdminFaqJpaRepository, times(1)).findOneFaq(adminFaqEntity.getIdx());
        verify(mockAdminFaqJpaRepository, atLeastOnce()).findOneFaq(adminFaqEntity.getIdx());
        verifyNoMoreInteractions(mockAdminFaqJpaRepository);

        InOrder inOrder = inOrder(mockAdminFaqJpaRepository);
        inOrder.verify(mockAdminFaqJpaRepository).findOneFaq(adminFaqEntity.getIdx());
    }

    @Test
    @DisplayName("FAQ삭제BDD테스트")
    void FAQ삭제BDD테스트() {
        // given
        em.persist(adminFaqEntity);
        adminFaqDTO = AdminFaqEntity.toDto(adminFaqEntity);

        // when
        when(mockAdminFaqJpaRepository.findOneFaq(adminFaqEntity.getIdx())).thenReturn(adminFaqDTO);
        Long deleteIdx = adminFaqJpaRepository.deleteFaq(adminFaqEntity.getIdx());

        // then
        assertThat(mockAdminFaqJpaRepository.findOneFaq(adminFaqEntity.getIdx()).getIdx()).isEqualTo(deleteIdx);

        // verify
        then(mockAdminFaqJpaRepository).should(times(1)).findOneFaq(adminFaqEntity.getIdx());
        then(mockAdminFaqJpaRepository).should(atLeastOnce()).findOneFaq(adminFaqEntity.getIdx());
        then(mockAdminFaqJpaRepository).shouldHaveNoMoreInteractions();
    }
}