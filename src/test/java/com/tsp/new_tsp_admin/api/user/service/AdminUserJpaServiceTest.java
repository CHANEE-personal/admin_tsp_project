package com.tsp.new_tsp_admin.api.user.service;

import com.tsp.new_tsp_admin.api.domain.user.AdminUserDTO;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity;
import com.tsp.new_tsp_admin.api.user.mapper.UserMapperImpl;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity.builder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
@AutoConfigureTestDatabase(replace = NONE)
@DisplayName("유저 Service Test")
class AdminUserJpaServiceTest {
    @Mock private AdminUserJpaService mockAdminUserJpaService;
    private final AdminUserJpaService adminUserJpaService;

    @Test
    @DisplayName("관리자 회원 리스트 조회 테스트")
    void 관리자회원리스트조회테스트() throws Exception {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("startPage", 1);
        userMap.put("size", 3);
        List<AdminUserDTO> adminUserList = adminUserJpaService.findUsersList(userMap);
        assertThat(adminUserList).isNotEmpty();
    }

    @Test
    @DisplayName("관리자 회원 리스트 조회 BDD 테스트")
    void 관리자회원리스트조회BDD테스트() throws Exception {
        // given
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("jpaStartPage", 1);
        userMap.put("size", 3);

        List<AdminUserDTO> returnUserList = new ArrayList<>();
        returnUserList.add(AdminUserDTO.builder()
                .idx(1).userId("admin05").password("test1234").name("admin05").visible("Y").build());

        // when
        when(mockAdminUserJpaService.findUsersList(userMap)).thenReturn(returnUserList);
        List<AdminUserDTO> userList = mockAdminUserJpaService.findUsersList(userMap);

        assertAll(
                () -> assertThat(userList).isNotEmpty(),
                () -> assertThat(userList).hasSize(1)
        );

        assertThat(userList.get(0).getIdx()).isEqualTo(returnUserList.get(0).getIdx());
        assertThat(userList.get(0).getUserId()).isEqualTo(returnUserList.get(0).getUserId());
        assertThat(userList.get(0).getPassword()).isEqualTo(returnUserList.get(0).getPassword());
        assertThat(userList.get(0).getName()).isEqualTo(returnUserList.get(0).getName());
        assertThat(userList.get(0).getVisible()).isEqualTo(returnUserList.get(0).getVisible());

        // verify
        verify(mockAdminUserJpaService, times(1)).findUsersList(userMap);
        verify(mockAdminUserJpaService, atLeastOnce()).findUsersList(userMap);
        verifyNoMoreInteractions(mockAdminUserJpaService);
    }

    @Test
    @DisplayName("관리자 회원 상세 조회 테스트")
    void 관리자회원상세조회테스트() throws Exception {
        AdminUserEntity adminUserEntity = adminUserJpaService.findOneUser("admin01");
        assertThat(adminUserEntity).isNotNull();
    }

    @Test
    @DisplayName("관리자 회원 상세 조회 BDD 테스트")
    void 관리자회원상세조회BDD테스트() throws Exception {
        AdminUserEntity adminUserEntity = builder()
                .userId("admin03")
                .password("pass1234")
                .name("admin03")
                .visible("Y")
                .build();

        AdminUserDTO adminUserDTO = UserMapperImpl.INSTANCE.toDto(adminUserEntity);
        // when
        when(mockAdminUserJpaService.findOneUser(adminUserEntity.getUserId())).thenReturn(adminUserEntity);
        AdminUserEntity userInfo = mockAdminUserJpaService.findOneUser(adminUserEntity.getUserId());

        // then
        assertThat(userInfo.getIdx()).isEqualTo(adminUserEntity.getIdx());
        assertThat(userInfo.getUserId()).isEqualTo(adminUserEntity.getUserId());
        assertThat(userInfo.getPassword()).isEqualTo(adminUserEntity.getPassword());
        assertThat(userInfo.getVisible()).isEqualTo(adminUserEntity.getVisible());

        // verify
        verify(mockAdminUserJpaService, times(1)).findOneUser(adminUserEntity.getUserId());
        verify(mockAdminUserJpaService, atLeastOnce()).findOneUser(adminUserDTO.getUserId());
        verifyNoMoreInteractions(mockAdminUserJpaService);
    }

    @Test
    @DisplayName("Token을 이용한 관리자 조회 테스트")
    void 토큰을이용한관리자조회테스트() throws Exception {
        // given
        AdminUserEntity adminUserEntity = builder()
                .userId("admin01")
                .idx(2)
                .userToken("test___eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTY1MTkyNDU0NSwiaWF0IjoxNjUxODg4NTQ1fQ.H3ntnpBve8trpCiwgdF8wlZsXa51FJmMWzIVf")
                .build();

        adminUserJpaService.insertToken(adminUserEntity);
        assertThat(adminUserJpaService.findOneUserByToken(adminUserEntity.getUserToken())).isEqualTo(adminUserEntity.getUserId());
    }

    @Test
    @DisplayName("관리자 로그인 처리 테스트")
    void 관리자로그인처리테스트() throws Exception {
        AdminUserEntity adminUserEntity = builder()
                .userId("admin03")
                .password("pass1234")
                .build();

        assertThat(adminUserJpaService.adminLogin(adminUserEntity)).isEqualTo("Y");
    }

    @Test
    @DisplayName("관리자 토큰 저장 테스트")
    void 관리자토큰저장테스트() throws Exception {
        AdminUserEntity adminUserEntity = builder()
                .userId("admin03")
                .userToken("test___eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTY1MTkyNDU0NSwiaWF0IjoxNjUxODg4NTQ1fQ.H3ntnpBve8trpCiwgdF8wlZsXa51FJmMWzIVf")
                .build();

        adminUserJpaService.insertToken(adminUserEntity);

        assertThat(adminUserEntity.getUserToken()).isNotEmpty();
    }

    @Test
    @DisplayName("관리자 회원가입 테스트")
    void 관리자회원가입테스트() throws Exception {
        // given
        AdminUserEntity adminUserEntity = builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .build();

        adminUserJpaService.insertAdminUser(adminUserEntity);

        // when
        when(mockAdminUserJpaService.findOneUser(adminUserEntity.getUserId())).thenReturn(adminUserEntity);

        // then
        assertThat(mockAdminUserJpaService.findOneUser(adminUserEntity.getUserId()).getUserId()).isEqualTo("test");
        assertThat(mockAdminUserJpaService.findOneUser(adminUserEntity.getUserId()).getName()).isEqualTo("test");
        assertThat(mockAdminUserJpaService.findOneUser(adminUserEntity.getUserId()).getEmail()).isEqualTo("test@test.com");

        // verify
        verify(mockAdminUserJpaService, times(3)).findOneUser(adminUserEntity.getUserId());
        verify(mockAdminUserJpaService, atLeastOnce()).findOneUser(adminUserEntity.getUserId());
        verifyNoMoreInteractions(mockAdminUserJpaService);
    }

    @Test
    @DisplayName("관리자 수정 테스트")
    void 관리자수정테스트() throws Exception {
        // given
        AdminUserEntity adminUserEntity = builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .build();

        Integer idx = adminUserJpaService.insertAdminUser(adminUserEntity).getIdx();

        AdminUserEntity newAdminUserEntity = builder()
                .idx(idx)
                .userId("test1")
                .password("test1")
                .name("test1")
                .email("test1@test.com")
                .visible("Y")
                .build();

        adminUserJpaService.updateAdminUser(newAdminUserEntity);

        // when
        when(mockAdminUserJpaService.findOneUser(newAdminUserEntity.getUserId())).thenReturn(newAdminUserEntity);

        // then
        assertThat(mockAdminUserJpaService.findOneUser(newAdminUserEntity.getUserId()).getUserId()).isEqualTo("test1");
        assertThat(mockAdminUserJpaService.findOneUser(newAdminUserEntity.getUserId()).getName()).isEqualTo("test1");
        assertThat(mockAdminUserJpaService.findOneUser(newAdminUserEntity.getUserId()).getEmail()).isEqualTo("test1@test.com");

        // verify
        verify(mockAdminUserJpaService, times(3)).findOneUser(newAdminUserEntity.getUserId());
        verify(mockAdminUserJpaService, atLeastOnce()).findOneUser(newAdminUserEntity.getUserId());
        verifyNoMoreInteractions(mockAdminUserJpaService);
    }

    @Test
    @DisplayName("관리자 탈퇴 테스트")
    void 관리자탈퇴테스트() throws Exception {
        // given
        AdminUserEntity adminUserEntity = builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .build();

        Integer idx = adminUserJpaService.insertAdminUser(adminUserEntity).getIdx();

        // then
        assertThat(adminUserJpaService.deleteAdminUser(idx)).isNotNull();
    }
}