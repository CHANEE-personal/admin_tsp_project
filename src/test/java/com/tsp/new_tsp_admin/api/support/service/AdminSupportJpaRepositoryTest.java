package com.tsp.new_tsp_admin.api.support.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.domain.support.AdminSupportDTO;
import com.tsp.new_tsp_admin.api.domain.support.AdminSupportEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.event.EventListener;
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
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("지원모델 Repository Test")
class AdminSupportJpaRepositoryTest {
    @Autowired
    private AdminSupportJpaRepository adminSupportJpaRepository;
    @Mock
    AdminSupportJpaRepository mockAdminSupportJpaRepository;
    @Autowired
    private EntityManager em;
    JPAQueryFactory queryFactory;

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
        queryFactory = new JPAQueryFactory(em);
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

        // then
        assertThat(mockAdminSupportJpaRepository.findSupportsList(supportMap).get(0).getIdx()).isEqualTo(supportList.get(0).getIdx());
        assertThat(mockAdminSupportJpaRepository.findSupportsList(supportMap).get(0).getSupportName()).isEqualTo(supportList.get(0).getSupportName());
    }

    @Test
    @DisplayName("지원모델 수정 테스트")
    void 지원모델수정테스트() {
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

        // then
        assertThat(mockAdminSupportJpaRepository.findOneSupportModel(adminSupportEntity).getSupportName()).isEqualTo("test");
        assertThat(mockAdminSupportJpaRepository.findOneSupportModel(adminSupportEntity).getSupportMessage()).isEqualTo("test");
        assertThat(mockAdminSupportJpaRepository.findOneSupportModel(adminSupportEntity).getSupportHeight()).isEqualTo(170);

        // verify
        verify(mockAdminSupportJpaRepository, times(3)).findOneSupportModel(adminSupportEntity);
        verify(mockAdminSupportJpaRepository, atLeastOnce()).findOneSupportModel(adminSupportEntity);
        verifyNoMoreInteractions(mockAdminSupportJpaRepository);
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