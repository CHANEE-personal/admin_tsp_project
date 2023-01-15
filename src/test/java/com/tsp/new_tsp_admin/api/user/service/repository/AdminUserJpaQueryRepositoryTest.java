package com.tsp.new_tsp_admin.api.user.service.repository;

import com.tsp.new_tsp_admin.api.domain.user.AdminUserDTO;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
@DisplayName("유저 Repository Test")
class AdminUserJpaQueryRepositoryTest {
    @Mock private AdminUserJpaQueryRepository mockAdminUserJpaQueryRepository;
    private final AdminUserJpaQueryRepository adminUserJpaQueryRepository;

    private AdminUserEntity adminUserEntity;
    private AdminUserDTO adminUserDTO;

    void createUser() {
        adminUserEntity = AdminUserEntity.builder().idx(2L).userId("admin01").build();
        adminUserDTO = AdminUserEntity.toDto(adminUserEntity);
    }

    @BeforeEach
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        createUser();
    }

    @Test
    @DisplayName("유저 조회 테스트")
    void 유저조회테스트() {
        // given
        Map<String, Object> userMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 100);

        // then
        assertThat(adminUserJpaQueryRepository.findUserList(userMap, pageRequest)).isNotEmpty();
    }

    @Test
    @DisplayName("유저 Mockito 조회 테스트")
    void 유저Mockito조회테스트() {
        // given
        Map<String, Object> userMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<AdminUserDTO> userList = new ArrayList<>();
        AdminUserDTO adminUserDTO = AdminUserDTO.builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .build();
        userList.add(adminUserDTO);

        Page<AdminUserDTO> resultUser = new PageImpl<>(userList, pageRequest, userList.size());

        // when
        when(mockAdminUserJpaQueryRepository.findUserList(userMap, pageRequest)).thenReturn(resultUser);
        Page<AdminUserDTO> newUserList = mockAdminUserJpaQueryRepository.findUserList(userMap, pageRequest);
        List<AdminUserDTO> findUserList = newUserList.stream().collect(Collectors.toList());

        // then
        assertThat(findUserList.get(0).getUserId()).isEqualTo(userList.get(0).getUserId());
        assertThat(findUserList.get(0).getPassword()).isEqualTo(userList.get(0).getPassword());
        assertThat(findUserList.get(0).getName()).isEqualTo(userList.get(0).getName());
        assertThat(findUserList.get(0).getEmail()).isEqualTo(userList.get(0).getEmail());

        // verify
        verify(mockAdminUserJpaQueryRepository, times(1)).findUserList(userMap, pageRequest);
        verify(mockAdminUserJpaQueryRepository, atLeastOnce()).findUserList(userMap, pageRequest);
        verifyNoMoreInteractions(mockAdminUserJpaQueryRepository);

        InOrder inOrder = inOrder(mockAdminUserJpaQueryRepository);
        inOrder.verify(mockAdminUserJpaQueryRepository).findUserList(userMap, pageRequest);
    }

    @Test
    @DisplayName("유저 BDD 조회 테스트")
    void 유저BDD조회테스트() {
        // given
        Map<String, Object> userMap = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(1, 3);

        List<AdminUserDTO> userList = new ArrayList<>();
        AdminUserDTO adminUserDTO = AdminUserDTO.builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .build();
        userList.add(adminUserDTO);

        Page<AdminUserDTO> resultUser = new PageImpl<>(userList, pageRequest, userList.size());

        // when
        given(mockAdminUserJpaQueryRepository.findUserList(userMap, pageRequest)).willReturn(resultUser);
        Page<AdminUserDTO> newUserList = mockAdminUserJpaQueryRepository.findUserList(userMap, pageRequest);
        List<AdminUserDTO> findUserList = newUserList.stream().collect(Collectors.toList());

        // then
        assertThat(findUserList.get(0).getUserId()).isEqualTo(userList.get(0).getUserId());
        assertThat(findUserList.get(0).getPassword()).isEqualTo(userList.get(0).getPassword());
        assertThat(findUserList.get(0).getName()).isEqualTo(userList.get(0).getName());
        assertThat(findUserList.get(0).getEmail()).isEqualTo(userList.get(0).getEmail());

        // verify
        then(mockAdminUserJpaQueryRepository).should(times(1)).findUserList(userMap, pageRequest);
        then(mockAdminUserJpaQueryRepository).should(atLeastOnce()).findUserList(userMap, pageRequest);
        then(mockAdminUserJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("유저 상세 조회 테스트")
    void 유저상세조회테스트() {
        // given
        AdminUserEntity adminUser = adminUserJpaQueryRepository.findOneUser(adminUserEntity.getUserId());

        // then
        assertAll(() -> {
                    assertThat(adminUser.getIdx()).isEqualTo(2);
                },
                () -> {
                    assertThat(adminUser.getUserId()).isEqualTo("admin01");
                    assertNotNull(adminUser.getUserId());
                },
                () -> {
                    assertNotNull(adminUser.getUserToken());
                });
    }

    @Test
    @DisplayName("유저 Mockito 상세 조회 테스트")
    void 유저Mockito상세조회테스트() {
        // given
        adminUserEntity = AdminUserEntity.builder().idx(1L).userId("test").build();

        AdminUserEntity bddUserEntity = AdminUserEntity.builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .build();

        // when
        when(mockAdminUserJpaQueryRepository.findOneUser(adminUserEntity.getUserId())).thenReturn(bddUserEntity);
        AdminUserEntity userInfo = mockAdminUserJpaQueryRepository.findOneUser(adminUserEntity.getUserId());

        // then
        assertThat(userInfo.getUserId()).isEqualTo(bddUserEntity.getUserId());
        assertThat(userInfo.getPassword()).isEqualTo(bddUserEntity.getPassword());
        assertThat(userInfo.getName()).isEqualTo(bddUserEntity.getName());
        assertThat(userInfo.getEmail()).isEqualTo(bddUserEntity.getEmail());

        // verify
        verify(mockAdminUserJpaQueryRepository, times(1)).findOneUser(bddUserEntity.getUserId());
        verify(mockAdminUserJpaQueryRepository, atLeastOnce()).findOneUser(bddUserEntity.getUserId());
        verifyNoMoreInteractions(mockAdminUserJpaQueryRepository);

        InOrder inOrder = inOrder(mockAdminUserJpaQueryRepository);
        inOrder.verify(mockAdminUserJpaQueryRepository).findOneUser(bddUserEntity.getUserId());
    }

    @Test
    @DisplayName("유저 상세 BDD 조회 테스트")
    void 유저상세BDD조회테스트() {
        // given
        adminUserEntity = AdminUserEntity.builder().idx(1L).userId("test").build();

        AdminUserEntity bddUserEntity = AdminUserEntity.builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .build();

        // when
        given(mockAdminUserJpaQueryRepository.findOneUser(adminUserEntity.getUserId())).willReturn(bddUserEntity);
        AdminUserEntity userInfo = mockAdminUserJpaQueryRepository.findOneUser(adminUserEntity.getUserId());

        // then
        assertThat(userInfo.getUserId()).isEqualTo(bddUserEntity.getUserId());
        assertThat(userInfo.getPassword()).isEqualTo(bddUserEntity.getPassword());
        assertThat(userInfo.getName()).isEqualTo(bddUserEntity.getName());
        assertThat(userInfo.getEmail()).isEqualTo(bddUserEntity.getEmail());

        // verify
        then(mockAdminUserJpaQueryRepository).should(times(1)).findOneUser(adminUserEntity.getUserId());
        then(mockAdminUserJpaQueryRepository).should(atLeastOnce()).findOneUser(adminUserEntity.getUserId());
        then(mockAdminUserJpaQueryRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("유저 로그인 테스트")
    void 유저로그인테스트() {
        // given
        AdminUserEntity adminUserEntity = AdminUserEntity.builder()
                .userId("admin02")
                .password("pass1234")
                .build();

        // then
        assertThat(adminUserJpaQueryRepository.adminLogin(adminUserEntity)).isEqualTo("Y");
    }

    @Test
    @DisplayName("유저 토큰을 이용한 회원 조회")
    void 유저토큰을이용한회원조회테스트() {
        // given
        AdminUserEntity adminUserEntity = AdminUserEntity.builder()
                .userId("admin01")
                .idx(2L)
                .userToken("test___eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTY1MTkyNDU0NSwiaWF0IjoxNjUxODg4NTQ1fQ.H3ntnpBve8trpCiwgdF8wlZsXa51FJmMWzIVf")
                .build();

        // when
        adminUserJpaQueryRepository.insertUserTokenByEm(adminUserEntity);
        // then
        assertThat(adminUserJpaQueryRepository.findOneUserByToken(adminUserEntity.getUserToken())).isEqualTo(adminUserEntity.getUserId());
    }

    @Test
    @DisplayName("유저 토큰 저장 테스트")
    void 유저토큰저장테스트() {
        // given
        AdminUserEntity adminUserEntity = AdminUserEntity.builder()
                .idx(2L)
                .userToken("test___eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTY1MTkyNDU0NSwiaWF0IjoxNjUxODg4NTQ1fQ.H3ntnpBve8trpCiwgdF8wlZsXa51FJmMWzIVf")
                .build();

        // then
        assertThat(adminUserJpaQueryRepository.insertUserTokenByEm(adminUserEntity)).isEqualTo(adminUserEntity.getIdx());
        assertNotNull(adminUserEntity.getUserToken());
        assertNotNull(adminUserEntity.getUserRefreshToken());
    }
}