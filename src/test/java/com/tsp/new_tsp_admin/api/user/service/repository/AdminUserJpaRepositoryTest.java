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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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

    private AdminUserEntity adminUserEntity;
    private AdminUserDTO adminUserDTO;

    public void createUser() {
        adminUserEntity = builder().idx(2).userId("admin01").build();
        adminUserDTO = AdminUserDTO.builder().idx(2).build();
    }

    @BeforeEach
    public void init() {
        queryFactory = new JPAQueryFactory(em);
        createUser();
    }

    @Test
    @DisplayName("유저 조회 테스트")
    public void 유저조회테스트() {

        // given
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("jpaStartPage", 1);
        userMap.put("size", 3);

        assertThat(adminUserJpaRepository.findUserList(userMap).size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("유저 BDD 조회 테스트")
    public void 유저BDD조회테스트() {
        // given
        ConcurrentHashMap<String, Object> userMap = new ConcurrentHashMap<>();
        userMap.put("jpaStartPage", 1);
        userMap.put("size", 3);

        List<AdminUserDTO> userList = new ArrayList<>();
        AdminUserDTO adminUserDTO = AdminUserDTO.builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .build();
        userList.add(adminUserDTO);

        // when
        given(mockAdminUserJpaRepository.findUserList(userMap)).willReturn(userList);

        // then
        assertThat(mockAdminUserJpaRepository.findUserList(userMap).get(0).getUserId()).isEqualTo(userList.get(0).getUserId());
        assertThat(mockAdminUserJpaRepository.findUserList(userMap).get(0).getPassword()).isEqualTo(userList.get(0).getPassword());
        assertThat(mockAdminUserJpaRepository.findUserList(userMap).get(0).getName()).isEqualTo(userList.get(0).getName());
        assertThat(mockAdminUserJpaRepository.findUserList(userMap).get(0).getEmail()).isEqualTo(userList.get(0).getEmail());

        // verify
        verify(mockAdminUserJpaRepository, times(4)).findUserList(userMap);
        verify(mockAdminUserJpaRepository, atLeastOnce()).findUserList(userMap);
    }

    @Test
    @DisplayName("유저 상세 조회 테스트")
    public void 유저상세조회테스트() {

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
    @DisplayName("유저 로그인 테스트")
    public void 유저로그인테스트() {
        AdminUserEntity adminUserEntity = builder()
                .userId("admin01")
                .password("pass1234")
                .build();

        assertThat(adminUserJpaRepository.adminLogin(adminUserEntity)).isEqualTo("Y");
    }

    @Test
    @DisplayName("유저 토큰 저장 테스트")
    public void 유저토큰저장테스트() {
        AdminUserEntity adminUserEntity = builder()
                .idx(2)
                .userToken("test___eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTY1MTkyNDU0NSwiaWF0IjoxNjUxODg4NTQ1fQ.H3ntnpBve8trpCiwgdF8wlZsXa51FJmMWzIVf")
                .build();

        assertThat(adminUserJpaRepository.insertUserTokenByEm(adminUserEntity)).isEqualTo(adminUserEntity.getIdx());
        assertNotNull(adminUserEntity.getUserToken());
    }

    @Test
    @DisplayName("유저 회원가입 테스트")
    public void 유저회원가입테스트() {
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
    @DisplayName("유저 회원탈퇴 테스트")
    public void 유저탈퇴테스트() {
        assertThat(adminUserJpaRepository.deleteAdminUser(adminUserEntity).getIdx()).isEqualTo(adminUserDTO.getIdx());
    }
}