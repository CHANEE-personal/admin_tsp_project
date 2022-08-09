package com.tsp.new_tsp_admin.api.support.service;

import com.tsp.new_tsp_admin.api.domain.support.AdminSupportDTO;
import com.tsp.new_tsp_admin.api.domain.support.AdminSupportEntity;
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

import static com.tsp.new_tsp_admin.api.domain.support.AdminSupportEntity.*;
import static com.tsp.new_tsp_admin.api.support.mapper.SupportMapper.INSTANCE;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("지원모델 Repository Test")
class AdminSupportJpaRepositoryTest {
    @Mock AdminSupportJpaRepository mockAdminSupportJpaRepository;
    private final AdminSupportJpaRepository adminSupportJpaRepository;
    private final EntityManager em;

    private AdminSupportEntity adminSupportEntity;
    private AdminSupportDTO adminSupportDTO;

    void createSupport() {
        adminSupportEntity = builder()
                .supportName("조찬희")
                .supportHeight(170)
                .supportMessage("조찬희")
                .supportPhone("010-9466-2702")
                .supportSize3("31-24-31")
                .build();

        adminSupportDTO = INSTANCE.toDto(adminSupportEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createSupport();
    }

    @Test
    @DisplayName("지원모델 리스트 조회 테스트")
    void 지원모델리스트조회테스트() {
        // given
        Map<String, Object> supportMap = new HashMap<>();
        supportMap.put("jpaStartPage", 1);
        supportMap.put("size", 3);

        // then
        assertThat(adminSupportJpaRepository.findSupportsList(supportMap)).isNotEmpty();
    }

    @Test
    @Disabled
    @DisplayName("지원모델 상세 조회 테스트")
    void 지원모델상세조회테스트() {
        // given
        adminSupportEntity = builder().idx(1).build();

        // when
        adminSupportDTO = adminSupportJpaRepository.findOneSupportModel(adminSupportEntity);

        // then
        assertThat(adminSupportDTO.getIdx()).isEqualTo(1);
    }

    @Test
    @DisplayName("지원모델 Mockito 조회 테스트")
    void 지원모델Mockito조회테스트() {
        // given
        Map<String, Object> supportMap = new HashMap<>();
        supportMap.put("jpaStartPage", 1);
        supportMap.put("size", 3);

        List<AdminSupportDTO> supportList = new ArrayList<>();
        supportList.add(AdminSupportDTO.builder().idx(1).supportName("조찬희").supportPhone("010-9466-2702").build());

        // when
        when(mockAdminSupportJpaRepository.findSupportsList(supportMap)).thenReturn(supportList);
        List<AdminSupportDTO> supportInfo = mockAdminSupportJpaRepository.findSupportsList(supportMap);

        // then
        assertThat(supportInfo.get(0).getIdx()).isEqualTo(supportList.get(0).getIdx());
        assertThat(supportInfo.get(0).getSupportName()).isEqualTo(supportList.get(0).getSupportName());

        // verify
        verify(mockAdminSupportJpaRepository, times(1)).findSupportsList(supportMap);
        verify(mockAdminSupportJpaRepository, atLeastOnce()).findSupportsList(supportMap);
        verifyNoMoreInteractions(mockAdminSupportJpaRepository);

        InOrder inOrder = inOrder(mockAdminSupportJpaRepository);
        inOrder.verify(mockAdminSupportJpaRepository).findSupportsList(supportMap);
    }

    @Test
    @DisplayName("지원모델 BDD 조회 테스트")
    void 지원모델BDD조회테스트() {
        // given
        Map<String, Object> supportMap = new HashMap<>();
        supportMap.put("jpaStartPage", 1);
        supportMap.put("size", 3);

        List<AdminSupportDTO> supportList = new ArrayList<>();
        supportList.add(AdminSupportDTO.builder().idx(1).supportName("조찬희").supportPhone("010-9466-2702").build());

        // when
        given(mockAdminSupportJpaRepository.findSupportsList(supportMap)).willReturn(supportList);
        List<AdminSupportDTO> newSupportList = mockAdminSupportJpaRepository.findSupportsList(supportMap);

        // then
        assertThat(newSupportList.get(0).getIdx()).isEqualTo(supportList.get(0).getIdx());
        assertThat(newSupportList.get(0).getSupportName()).isEqualTo(supportList.get(0).getSupportName());

        // verify
        then(mockAdminSupportJpaRepository).should(times(1)).findSupportsList(supportMap);
        then(mockAdminSupportJpaRepository).should(atLeastOnce()).findSupportsList(supportMap);
        then(mockAdminSupportJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("지원모델 수정 Mockito 테스트")
    void 지원모델수정Mockito테스트() {
        // given
        em.persist(adminSupportEntity);
        Integer idx = em.find(AdminSupportEntity.class, this.adminSupportEntity.getIdx()).getIdx();

        adminSupportEntity = builder()
                .idx(idx)
                .supportName("test")
                .supportPhone("010-9466-2702")
                .supportHeight(170)
                .supportSize3("31-24-31")
                .supportMessage("test")
                .supportInstagram("https://instagram.com")
                .build();

        adminSupportJpaRepository.updateSupportModel(adminSupportEntity);

        adminSupportDTO = INSTANCE.toDto(adminSupportEntity);

        // when
        when(mockAdminSupportJpaRepository.findOneSupportModel(adminSupportEntity)).thenReturn(adminSupportDTO);
        AdminSupportDTO supportInfo = mockAdminSupportJpaRepository.findOneSupportModel(adminSupportEntity);

        // then
        assertThat(supportInfo.getSupportName()).isEqualTo("test");
        assertThat(supportInfo.getSupportMessage()).isEqualTo("test");
        assertThat(supportInfo.getSupportHeight()).isEqualTo(170);

        // verify
        verify(mockAdminSupportJpaRepository, times(1)).findOneSupportModel(adminSupportEntity);
        verify(mockAdminSupportJpaRepository, atLeastOnce()).findOneSupportModel(adminSupportEntity);
        verifyNoMoreInteractions(mockAdminSupportJpaRepository);

        InOrder inOrder = inOrder(mockAdminSupportJpaRepository);
        inOrder.verify(mockAdminSupportJpaRepository).findOneSupportModel(adminSupportEntity);
    }

    @Test
    @DisplayName("지원모델 수정 BDD 테스트")
    void 지원모델수정BDD테스트() {
        // given
        em.persist(adminSupportEntity);
        Integer idx = em.find(AdminSupportEntity.class, this.adminSupportEntity.getIdx()).getIdx();

        adminSupportEntity = builder()
                .idx(idx)
                .supportName("test")
                .supportPhone("010-9466-2702")
                .supportHeight(170)
                .supportSize3("31-24-31")
                .supportMessage("test")
                .supportInstagram("https://instagram.com")
                .build();

        adminSupportJpaRepository.updateSupportModel(adminSupportEntity);

        adminSupportDTO = INSTANCE.toDto(adminSupportEntity);

        // when
        given(mockAdminSupportJpaRepository.findOneSupportModel(adminSupportEntity)).willReturn(adminSupportDTO);
        AdminSupportDTO supportInfo = mockAdminSupportJpaRepository.findOneSupportModel(adminSupportEntity);

        // then
        assertThat(supportInfo.getSupportName()).isEqualTo("test");
        assertThat(supportInfo.getSupportMessage()).isEqualTo("test");
        assertThat(supportInfo.getSupportHeight()).isEqualTo(170);

        // verify
        then(mockAdminSupportJpaRepository).should(times(1)).findOneSupportModel(adminSupportEntity);
        then(mockAdminSupportJpaRepository).should(atLeastOnce()).findOneSupportModel(adminSupportEntity);
        then(mockAdminSupportJpaRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("지원 모델 삭제 테스트")
    void 지원모델삭제테스트() {
        // given
        em.persist(adminSupportEntity);

        Integer entityIdx = adminSupportEntity.getIdx();
        Integer deleteIdx = adminSupportJpaRepository.deleteSupportModel(adminSupportEntity.getIdx());

        // then
        assertThat(deleteIdx).isEqualTo(entityIdx);
    }
}