package com.tsp.new_tsp_admin.api.user.service.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserDTO;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

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

        // given
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("categoryCd", 1);
        userMap.put("jpaStartPage", 1);
        userMap.put("size", 3);

        List<AdminUserDTO> userList = adminUserJpaRepository.findUserList(userMap);

        assertThat(userList.size()).isGreaterThan(0);
    }

    @Test
    public void 유저상세조회테스트() throws Exception {

        AdminUserEntity adminUserEntity = builder().idx(2).userId("admin01").build();

        AdminUserEntity adminUser = adminUserJpaRepository.findOneUser(adminUserEntity.getUserId());

        assertAll(() -> assertThat(adminUser.getIdx()).isEqualTo(2),
                () -> {
                    assertThat(adminUser.getUserId()).isEqualTo("admin01");
                    assertNotNull(adminUser.getUserId());
                },
                () -> {
                    assertNotNull(adminUser.getUserToken());
                });
    }

    @Test
    public void 유저로그인테스트() throws Exception {
        AdminUserEntity adminUserEntity = builder()
                .userId("admin01")
                .password("pass1234")
                .build();

        assertThat(adminUserJpaRepository.adminLogin(adminUserEntity)).isEqualTo("Y");
    }

    @Test
    public void 유저토큰저장테스트() throws Exception {
        AdminUserEntity adminUserEntity = builder()
                .idx(2)
                .userToken("test___eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTY1MTkyNDU0NSwiaWF0IjoxNjUxODg4NTQ1fQ.H3ntnpBve8trpCiwgdF8wlZsXa51FJmMWzIVf")
                .build();

        assertThat(adminUserJpaRepository.insertUserTokenByEm(adminUserEntity)).isEqualTo(adminUserEntity.getIdx());
        assertNotNull(adminUserEntity.getUserToken());
    }

    @Test
    public void 유저회원가입테스트() throws Exception {
        AdminUserEntity adminUserEntity = builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .build();

        adminUserJpaRepository.insertAdminUser(adminUserEntity);

        when(mockAdminUserJpaRepository.findOneUser(adminUserEntity.getUserId())).thenReturn(adminUserEntity);

        assertThat(mockAdminUserJpaRepository.findOneUser(adminUserEntity.getUserId()).getUserId()).isEqualTo("test");
        assertThat(mockAdminUserJpaRepository.findOneUser(adminUserEntity.getUserId()).getName()).isEqualTo("test");
        assertThat(mockAdminUserJpaRepository.findOneUser(adminUserEntity.getUserId()).getEmail()).isEqualTo("test@test.com");
    }

    @Test
    public void 유저탈퇴테스트() throws Exception {
        AdminUserEntity adminUserEntity = builder().idx(2).build();

        assertThat(adminUserJpaRepository.deleteAdminUser(adminUserEntity)).isEqualTo(adminUserEntity.getIdx());
    }
}