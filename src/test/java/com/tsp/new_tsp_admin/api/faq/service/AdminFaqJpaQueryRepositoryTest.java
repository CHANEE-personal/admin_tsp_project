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

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@DisplayName("FAQ Repository Test")
class AdminFaqJpaQueryRepositoryTest {
    @Mock
    private AdminFaqJpaQueryRepository mockAdminFaqJpaQueryRepository;
    private final AdminFaqJpaQueryRepository adminFaqJpaQueryRepository;
    private final AdminFaqJpaRepository adminFaqJpaRepository;

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
        assertThat(adminFaqJpaQueryRepository.findFaqList(faqMap)).isNotEmpty();
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
        when(mockAdminFaqJpaQueryRepository.findFaqList(faqMap)).thenReturn(faqList);
        List<AdminFaqDTO> newFaqList = mockAdminFaqJpaQueryRepository.findFaqList(faqMap);

        // then
        assertThat(newFaqList.get(0).getIdx()).isEqualTo(faqList.get(0).getIdx());
        assertThat(newFaqList.get(0).getTitle()).isEqualTo(faqList.get(0).getTitle());
        assertThat(newFaqList.get(0).getDescription()).isEqualTo(faqList.get(0).getDescription());
        assertThat(newFaqList.get(0).getVisible()).isEqualTo(faqList.get(0).getVisible());

        // verify
        verify(mockAdminFaqJpaQueryRepository, times(1)).findFaqList(faqMap);
        verify(mockAdminFaqJpaQueryRepository, atLeastOnce()).findFaqList(faqMap);
        verifyNoMoreInteractions(mockAdminFaqJpaQueryRepository);

        InOrder inOrder = inOrder(mockAdminFaqJpaQueryRepository);
        inOrder.verify(mockAdminFaqJpaQueryRepository).findFaqList(faqMap);
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
        given(mockAdminFaqJpaQueryRepository.findFaqList(faqMap)).willReturn(faqList);
        List<AdminFaqDTO> newNoticeList = mockAdminFaqJpaQueryRepository.findFaqList(faqMap);

        // then
        assertThat(newNoticeList.get(0).getIdx()).isEqualTo(faqList.get(0).getIdx());
        assertThat(newNoticeList.get(0).getTitle()).isEqualTo(faqList.get(0).getTitle());
        assertThat(newNoticeList.get(0).getDescription()).isEqualTo(faqList.get(0).getDescription());
        assertThat(newNoticeList.get(0).getVisible()).isEqualTo(faqList.get(0).getVisible());

        // verify
        then(mockAdminFaqJpaQueryRepository).should(times(1)).findFaqList(faqMap);
        then(mockAdminFaqJpaQueryRepository).should(atLeastOnce()).findFaqList(faqMap);
        then(mockAdminFaqJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("이전 or 다음 FAQ 상세 조회 테스트")
    void 이전or다음FAQ상세조회테스트() {
        // given
        adminFaqEntity = AdminFaqEntity.builder().idx(2L).build();

        // 이전 FAQ
        assertThat(adminFaqJpaQueryRepository.findPrevOneFaq(adminFaqEntity.getIdx()).getIdx()).isEqualTo(1);
        // 다음 FAQ
        assertThat(adminFaqJpaQueryRepository.findNextOneFaq(adminFaqEntity.getIdx()).getIdx()).isEqualTo(3);
    }

    @Test
    @DisplayName("이전 FAQ 상세 조회 Mockito 테스트")
    void 이전FAQ상세조회Mockito테스트() {
        // given
        adminFaqEntity = AdminFaqEntity.builder().idx(2L).build();

        // when
        adminFaqDTO = adminFaqJpaQueryRepository.findPrevOneFaq(adminFaqEntity.getIdx());

        when(mockAdminFaqJpaQueryRepository.findPrevOneFaq(adminFaqEntity.getIdx())).thenReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaQueryRepository.findPrevOneFaq(adminFaqEntity.getIdx());

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(1);

        // verify
        verify(mockAdminFaqJpaQueryRepository, times(1)).findPrevOneFaq(adminFaqEntity.getIdx());
        verify(mockAdminFaqJpaQueryRepository, atLeastOnce()).findPrevOneFaq(adminFaqEntity.getIdx());
        verifyNoMoreInteractions(mockAdminFaqJpaQueryRepository);

        InOrder inOrder = inOrder(mockAdminFaqJpaQueryRepository);
        inOrder.verify(mockAdminFaqJpaQueryRepository).findPrevOneFaq(adminFaqEntity.getIdx());
    }

    @Test
    @DisplayName("이전 FAQ 상세 조회 BDD 테스트")
    void 이전FAQ상세조회BDD테스트() {
        // given
        adminFaqEntity = AdminFaqEntity.builder().idx(2L).build();

        // when
        adminFaqDTO = adminFaqJpaQueryRepository.findPrevOneFaq(adminFaqEntity.getIdx());

        given(mockAdminFaqJpaQueryRepository.findPrevOneFaq(adminFaqEntity.getIdx())).willReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaQueryRepository.findPrevOneFaq(adminFaqEntity.getIdx());

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(1);

        // verify
        then(mockAdminFaqJpaQueryRepository).should(times(1)).findPrevOneFaq(adminFaqEntity.getIdx());
        then(mockAdminFaqJpaQueryRepository).should(atLeastOnce()).findPrevOneFaq(adminFaqEntity.getIdx());
        then(mockAdminFaqJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("다음 FAQ 상세 조회 Mockito 테스트")
    void 다음FAQ상세조회Mockito테스트() {
        // given
        adminFaqEntity = AdminFaqEntity.builder().idx(2L).build();

        // when
        adminFaqDTO = adminFaqJpaQueryRepository.findNextOneFaq(adminFaqEntity.getIdx());

        when(mockAdminFaqJpaQueryRepository.findNextOneFaq(adminFaqEntity.getIdx())).thenReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaQueryRepository.findNextOneFaq(adminFaqEntity.getIdx());

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(1);

        // verify
        verify(mockAdminFaqJpaQueryRepository, times(1)).findNextOneFaq(adminFaqEntity.getIdx());
        verify(mockAdminFaqJpaQueryRepository, atLeastOnce()).findNextOneFaq(adminFaqEntity.getIdx());
        verifyNoMoreInteractions(mockAdminFaqJpaQueryRepository);

        InOrder inOrder = inOrder(mockAdminFaqJpaQueryRepository);
        inOrder.verify(mockAdminFaqJpaQueryRepository).findNextOneFaq(adminFaqEntity.getIdx());
    }

    @Test
    @DisplayName("다음 FAQ 상세 조회 BDD 테스트")
    void 다음FAQ상세조회BDD테스트() {
        // given
        adminFaqEntity = AdminFaqEntity.builder().idx(2L).build();

        // when
        adminFaqDTO = adminFaqJpaQueryRepository.findNextOneFaq(adminFaqEntity.getIdx());

        given(mockAdminFaqJpaQueryRepository.findNextOneFaq(adminFaqEntity.getIdx())).willReturn(adminFaqDTO);
        AdminFaqDTO faqInfo = mockAdminFaqJpaQueryRepository.findNextOneFaq(adminFaqEntity.getIdx());

        // then
        assertThat(faqInfo.getIdx()).isEqualTo(1);

        // verify
        then(mockAdminFaqJpaQueryRepository).should(times(1)).findNextOneFaq(adminFaqEntity.getIdx());
        then(mockAdminFaqJpaQueryRepository).should(atLeastOnce()).findNextOneFaq(adminFaqEntity.getIdx());
        then(mockAdminFaqJpaQueryRepository).shouldHaveNoMoreInteractions();
    }
}