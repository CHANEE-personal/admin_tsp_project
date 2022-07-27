package com.tsp.new_tsp_admin.api.user.service.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserDTO;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import static com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity.*;
import static com.tsp.new_tsp_admin.api.user.mapper.UserMapper.INSTANCE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
@DisplayName("회원 Repository Test")
class AdminUserJpaRepositoryTest {
    @Mock private AdminUserJpaRepository mockAdminUserJpaRepository;
    private final AdminUserJpaRepository adminUserJpaRepository;
    private final EntityManager em;

    protected JPAQueryFactory queryFactory;
    private AdminUserEntity adminUserEntity;
    private AdminUserDTO adminUserDTO;

    void createUser() {
        adminUserEntity = builder().idx(2).userId("admin01").build();
        adminUserDTO = INSTANCE.toDto(adminUserEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        queryFactory = new JPAQueryFactory(em);
        createUser();
    }

    @Test
    @DisplayName("유저 조회 테스트")
    void 유저조회테스트() {
        // given
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("jpaStartPage", 1);
        userMap.put("size", 3);

        assertThat(adminUserJpaRepository.findUsersList(userMap)).isNotEmpty();
    }

    @Test
    @DisplayName("유저 BDD 조회 테스트")
    void 유저BDD조회테스트() {
        // given
        Map<String, Object> userMap = new HashMap<>();
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
//        given(mockAdminUserJpaRepository.findUsersList(userMap)).willReturn(userList);
        when(mockAdminUserJpaRepository.findUsersList(userMap)).thenReturn(userList);

        // then
        assertThat(mockAdminUserJpaRepository.findUsersList(userMap).get(0).getUserId()).isEqualTo(userList.get(0).getUserId());
        assertThat(mockAdminUserJpaRepository.findUsersList(userMap).get(0).getPassword()).isEqualTo(userList.get(0).getPassword());
        assertThat(mockAdminUserJpaRepository.findUsersList(userMap).get(0).getName()).isEqualTo(userList.get(0).getName());
        assertThat(mockAdminUserJpaRepository.findUsersList(userMap).get(0).getEmail()).isEqualTo(userList.get(0).getEmail());

        // verify
        verify(mockAdminUserJpaRepository, times(4)).findUsersList(userMap);
        verify(mockAdminUserJpaRepository, atLeastOnce()).findUsersList(userMap);
        verifyNoMoreInteractions(mockAdminUserJpaRepository);
    }

    @Test
    @DisplayName("유저 상세 조회 테스트")
    void 유저상세조회테스트() {
        // given
        AdminUserEntity adminUser = adminUserJpaRepository.findOneUser(adminUserEntity.getUserId());

        // then
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
    @DisplayName("유저 상세 BDD 조회 테스트")
    void 유저상세BDD조회테스트() {
        // given
        adminUserEntity = builder().idx(1).userId("test").build();

        AdminUserEntity bddUserEntity = builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .build();

        // when
//        given(mockAdminUserJpaRepository.findOneUser(adminUserEntity.getUserId())).willReturn(bddUserEntity);
        when(mockAdminUserJpaRepository.findOneUser(adminUserEntity.getUserId())).thenReturn(bddUserEntity);

        // then
        assertThat(mockAdminUserJpaRepository.findOneUser(adminUserEntity.getUserId()).getUserId()).isEqualTo(bddUserEntity.getUserId());
        assertThat(mockAdminUserJpaRepository.findOneUser(adminUserEntity.getUserId()).getPassword()).isEqualTo(bddUserEntity.getPassword());
        assertThat(mockAdminUserJpaRepository.findOneUser(adminUserEntity.getUserId()).getName()).isEqualTo(bddUserEntity.getName());
        assertThat(mockAdminUserJpaRepository.findOneUser(adminUserEntity.getUserId()).getEmail()).isEqualTo(bddUserEntity.getEmail());

        // verify
        verify(mockAdminUserJpaRepository, times(4)).findOneUser(bddUserEntity.getUserId());
        verify(mockAdminUserJpaRepository, atLeastOnce()).findOneUser(bddUserEntity.getUserId());
        verifyNoMoreInteractions(mockAdminUserJpaRepository);
    }

    @Test
    @DisplayName("유저 로그인 테스트")
    void 유저로그인테스트() {
        // given
        AdminUserEntity adminUserEntity = builder()
                .userId("admin02")
                .password("pass1234")
                .build();

        // then
        assertThat(adminUserJpaRepository.adminLogin(adminUserEntity)).isEqualTo("Y");
    }

    @Test
    @DisplayName("유저 토큰을 이용한 회원 조회")
    void 유저토큰을이용한회원조회테스트() {
        // given
        AdminUserEntity adminUserEntity = builder()
                .userId("admin01")
                .idx(2)
                .userToken("test___eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTY1MTkyNDU0NSwiaWF0IjoxNjUxODg4NTQ1fQ.H3ntnpBve8trpCiwgdF8wlZsXa51FJmMWzIVf")
                .build();

        // when
        adminUserJpaRepository.insertUserTokenByEm(adminUserEntity);
        // then
        assertThat(adminUserJpaRepository.findOneUserByToken(adminUserEntity.getUserToken())).isEqualTo(adminUserEntity.getUserId());
    }

    @Test
    @DisplayName("유저 토큰 저장 테스트")
    void 유저토큰저장테스트() {
        // given
        AdminUserEntity adminUserEntity = builder()
                .idx(2)
                .userToken("test___eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTY1MTkyNDU0NSwiaWF0IjoxNjUxODg4NTQ1fQ.H3ntnpBve8trpCiwgdF8wlZsXa51FJmMWzIVf")
                .build();

        // then
        assertThat(adminUserJpaRepository.insertUserTokenByEm(adminUserEntity)).isEqualTo(adminUserEntity.getIdx());
        assertNotNull(adminUserEntity.getUserToken());
        assertNotNull(adminUserEntity.getUserRefreshToken());
    }

    @Test
    @DisplayName("유저 회원가입 테스트")
    void 유저회원가입테스트() {
        // given
        AdminUserEntity adminUserEntity = builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .build();

        adminUserJpaRepository.insertAdminUser(adminUserEntity);

        // when
        when(mockAdminUserJpaRepository.findOneUser(adminUserEntity.getUserId())).thenReturn(adminUserEntity);

        // then
        assertThat(mockAdminUserJpaRepository.findOneUser(adminUserEntity.getUserId()).getUserId()).isEqualTo("test");
        assertThat(mockAdminUserJpaRepository.findOneUser(adminUserEntity.getUserId()).getPassword()).isEqualTo("test");
        assertThat(mockAdminUserJpaRepository.findOneUser(adminUserEntity.getUserId()).getName()).isEqualTo("test");
        assertThat(mockAdminUserJpaRepository.findOneUser(adminUserEntity.getUserId()).getEmail()).isEqualTo("test@test.com");

        // verify
        verify(mockAdminUserJpaRepository, atLeastOnce()).findOneUser(adminUserEntity.getUserId());
        verifyNoMoreInteractions(mockAdminUserJpaRepository);
    }

    @Test
    @DisplayName("유저 회원수정 테스트")
    void 유저회원수정테스트() {
        // given
        AdminUserEntity adminUserEntity = builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .build();

        Integer idx = adminUserJpaRepository.insertAdminUser(adminUserEntity).getIdx();

        AdminUserEntity newAdminUserEntity = builder()
                .idx(idx)
                .userId("test1")
                .password("test1")
                .name("test1")
                .email("test1@test.com")
                .visible("Y")
                .build();

        adminUserJpaRepository.updateAdminUser(newAdminUserEntity);

        // when
        when(mockAdminUserJpaRepository.findOneUser(newAdminUserEntity.getUserId())).thenReturn(newAdminUserEntity);

        // then
        assertThat(mockAdminUserJpaRepository.findOneUser(newAdminUserEntity.getUserId()).getUserId()).isEqualTo("test1");
        assertThat(mockAdminUserJpaRepository.findOneUser(newAdminUserEntity.getUserId()).getName()).isEqualTo("test1");

        // verify
        verify(mockAdminUserJpaRepository, times(2)).findOneUser(newAdminUserEntity.getUserId());
        verify(mockAdminUserJpaRepository, atLeastOnce()).findOneUser(newAdminUserEntity.getUserId());
        verifyNoMoreInteractions(mockAdminUserJpaRepository);
    }

    @Test
    @DisplayName("유저 회원탈퇴 테스트")
    void 유저탈퇴테스트() {
        assertThat(adminUserJpaRepository.deleteAdminUser(adminUserEntity.getIdx())).isEqualTo(adminUserDTO.getIdx());
    }
}