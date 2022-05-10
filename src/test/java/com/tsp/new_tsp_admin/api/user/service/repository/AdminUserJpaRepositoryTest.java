package com.tsp.new_tsp_admin.api.user.service.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
@DisplayName("회원 Repository Test")
class AdminUserJpaRepositoryTest {

    @Autowired
    private AdminUserJpaRepository adminUserJpaRepository;

    @Mock
    private AdminUserJpaRepository mockAdminUserJpaRepository;

    @Autowired
    private EntityManager em;
    JPAQueryFactory queryFactory;

    @BeforeEach
    public void init() { queryFactory = new JPAQueryFactory(em); }

    @Test
    public void 유저조회테스트() throws Exception {

        List<AdminUserEntity> userList = adminUserJpaRepository.findAll();

        assertThat(userList.size()).isGreaterThan(0);
    }

    @Test
    public void 유저상세조회테스트() throws Exception {

        AdminUserEntity adminUserEntity = AdminUserEntity.builder().idx(1).userId("admin").build();

        AdminUserEntity adminUserEntity1 = adminUserJpaRepository.findAdminUserEntityByUserId(adminUserEntity.getUserId());

        assertAll(() -> assertThat(adminUserEntity1.getIdx()).isEqualTo(1),
                () -> {
                    assertThat(adminUserEntity1.getUserId()).isEqualTo("admin");
                    assertNotNull(adminUserEntity1.getUserId());
                });
    }
}