package com.tsp.new_tsp_admin.api.support.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.domain.support.AdminSupportDTO;
import com.tsp.new_tsp_admin.api.domain.support.AdminSupportEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.tsp.new_tsp_admin.api.domain.support.AdminSupportEntity.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("지원모델 Repository Test")
class AdminSupportJpaRepositoryTest {
    @Autowired
    private AdminSupportJpaRepository adminSupportJpaRepository;

    @Mock
    AdminSupportJpaRepository mockAdminJpaRepository;

    @Autowired
    private EntityManager em;
    JPAQueryFactory queryFactory;

    private AdminSupportEntity adminSupportEntity;
    private AdminSupportDTO adminSupportDTO;

    public void createSupport() {
        AdminSupportEntity adminSupportEntity = builder()
                .supportName("조찬희")
                .supportHeight(170)
                .supportMessage("조찬희")
                .supportPhone("010-9466-2702")
                .supportSize3("31-24-31")
                .build();

        AdminSupportDTO adminSupportDTO = AdminSupportDTO.builder()
                .supportName("조찬희")
                .supportHeight(170)
                .supportMessage("조찬희")
                .supportPhone("010-9466-2702")
                .supportSize3("31-24-31")
                .build();
    }

    @BeforeEach
    public void init() {
        queryFactory = new JPAQueryFactory(em);
        createSupport();
    }

    @Test
    @DisplayName("지원모델 리스트 조회 테스트")
    public void 지원모델리스트조회테스트() {

        // given
        Map<String, Object> supportMap = new HashMap<>();
        supportMap.put("jpaStartPage", 1);
        supportMap.put("size", 3);

        // then
        assertThat(adminSupportJpaRepository.findSupportsList(supportMap).size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("지원모델 상세 조회 테스트")
    public void 지원모델상세조회테스트() {

        // given
        adminSupportEntity = builder().idx(1).build();

        // when
        adminSupportDTO = adminSupportJpaRepository.findOneSupportModel(adminSupportEntity);

        assertThat(adminSupportDTO.getIdx()).isEqualTo(1);
    }

    @Test
    @DisplayName("지원모델 BDD 조회 테스트")
    public void 지원모델BDD조회테스트() {

        // given
        ConcurrentHashMap<String, Object> supportMap = new ConcurrentHashMap<>();
        supportMap.put("jpaStartPage", 1);
        supportMap.put("size", 3);

        List<AdminSupportDTO> supportList = new ArrayList<>();
        supportList.add(AdminSupportDTO.builder().idx(1).supportName("조찬희").supportPhone("010-9466-2702").build());

        // when
        given(mockAdminJpaRepository.findSupportsList(supportMap)).willReturn(supportList);

        // then
        assertThat(mockAdminJpaRepository.findSupportsList(supportMap).get(0).getIdx()).isEqualTo(supportList.get(0).getIdx());
        assertThat(mockAdminJpaRepository.findSupportsList(supportMap).get(0).getSupportName()).isEqualTo(supportList.get(0).getSupportName());
    }

}