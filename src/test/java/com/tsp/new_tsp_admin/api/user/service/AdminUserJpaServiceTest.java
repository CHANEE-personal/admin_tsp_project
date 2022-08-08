package com.tsp.new_tsp_admin.api.user.service;

import com.tsp.new_tsp_admin.api.domain.user.AdminUserDTO;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
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
import static com.tsp.new_tsp_admin.api.user.mapper.UserMapper.INSTANCE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
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
        // given
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("jpaStartPage", 1);
        userMap.put("size", 3);
        List<AdminUserDTO> adminUserList = adminUserJpaService.findUsersList(userMap);
        // then
        assertThat(adminUserList).isNotEmpty();
    }

    @Test
    @DisplayName("관리자 회원 리스트 조회 Mockito 테스트")
    void 관리자회원리스트조회Mockito테스트() throws Exception {
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

        // then
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

        InOrder inOrder = inOrder(mockAdminUserJpaService);
        inOrder.verify(mockAdminUserJpaService).findUsersList(userMap);
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
        given(mockAdminUserJpaService.findUsersList(userMap)).willReturn(returnUserList);
        List<AdminUserDTO> userList = mockAdminUserJpaService.findUsersList(userMap);

        // then
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
        then(mockAdminUserJpaService).should(times(1)).findUsersList(userMap);
        then(mockAdminUserJpaService).should(atLeastOnce());
        then(mockAdminUserJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("관리자 회원 상세 조회 테스트")
    void 관리자회원상세조회테스트() throws Exception {
        // given
        AdminUserEntity adminUserEntity = adminUserJpaService.findOneUser("admin01");
        // then
        assertThat(adminUserEntity).isNotNull();
    }

    @Test
    @DisplayName("관리자 회원 상세 조회 Mockito 테스트")
    void 관리자회원상세조회Mockito테스트() throws Exception {
        // given
        AdminUserEntity adminUserEntity = builder()
                .userId("admin03")
                .password("pass1234")
                .name("admin03")
                .visible("Y")
                .build();

        AdminUserDTO adminUserDTO = INSTANCE.toDto(adminUserEntity);
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

        InOrder inOrder = inOrder(mockAdminUserJpaService);
        inOrder.verify(mockAdminUserJpaService).findOneUser(adminUserDTO.getUserId());
    }

    @Test
    @DisplayName("관리자 회원 상세 조회 BDD 테스트")
    void 관리자회원상세조회BDD테스트() throws Exception {
        // given
        AdminUserEntity adminUserEntity = builder()
                .userId("admin03")
                .password("pass1234")
                .name("admin03")
                .visible("Y")
                .build();

        // when
        given(mockAdminUserJpaService.findOneUser(adminUserEntity.getUserId())).willReturn(adminUserEntity);
        AdminUserEntity userInfo = mockAdminUserJpaService.findOneUser(adminUserEntity.getUserId());

        // then
        assertThat(userInfo.getIdx()).isEqualTo(adminUserEntity.getIdx());
        assertThat(userInfo.getUserId()).isEqualTo(adminUserEntity.getUserId());
        assertThat(userInfo.getPassword()).isEqualTo(adminUserEntity.getPassword());
        assertThat(userInfo.getVisible()).isEqualTo(adminUserEntity.getVisible());

        // verify
        then(mockAdminUserJpaService).should(times(1)).findOneUser(adminUserEntity.getUserId());
        then(mockAdminUserJpaService).should(atLeastOnce()).findOneUser(adminUserEntity.getUserId());
        then(mockAdminUserJpaService).shouldHaveNoMoreInteractions();
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

        // then
        assertThat(adminUserJpaService.findOneUserByToken(adminUserEntity.getUserToken())).isEqualTo(adminUserEntity.getUserId());
    }

    @Test
    @DisplayName("관리자 로그인 처리 테스트")
    void 관리자로그인처리테스트() throws Exception {
        // given
        AdminUserEntity adminUserEntity = builder()
                .userId("admin03")
                .password("pass1234")
                .build();

        // then
        assertThat(adminUserJpaService.adminLogin(adminUserEntity)).isEqualTo("Y");
    }

    @Test
    @DisplayName("관리자 토큰 저장 테스트")
    void 관리자토큰저장테스트() throws Exception {
        // given
        AdminUserEntity adminUserEntity = builder()
                .userId("admin03")
                .userToken("test___eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTY1MTkyNDU0NSwiaWF0IjoxNjUxODg4NTQ1fQ.H3ntnpBve8trpCiwgdF8wlZsXa51FJmMWzIVf")
                .build();

        adminUserJpaService.insertToken(adminUserEntity);

        // then
        assertThat(adminUserEntity.getUserToken()).isNotEmpty();
    }

    @Test
    @DisplayName("관리자 회원가입 Mockito 테스트")
    void 관리자회원가입Mockito테스트() throws Exception {
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
        AdminUserEntity userInfo = mockAdminUserJpaService.findOneUser(adminUserEntity.getUserId());

        // then
        assertThat(userInfo.getUserId()).isEqualTo("test");
        assertThat(userInfo.getName()).isEqualTo("test");
        assertThat(userInfo.getEmail()).isEqualTo("test@test.com");

        // verify
        verify(mockAdminUserJpaService, times(1)).findOneUser(adminUserEntity.getUserId());
        verify(mockAdminUserJpaService, atLeastOnce()).findOneUser(adminUserEntity.getUserId());
        verifyNoMoreInteractions(mockAdminUserJpaService);

        InOrder inOrder = inOrder(mockAdminUserJpaService);
        inOrder.verify(mockAdminUserJpaService).findOneUser(adminUserEntity.getUserId());
    }

    @Test
    @DisplayName("관리자 회원가입 BDD 테스트")
    void 관리자회원가입BDD테스트() throws Exception {
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
        given(mockAdminUserJpaService.findOneUser(adminUserEntity.getUserId())).willReturn(adminUserEntity);

        // then
        assertThat(mockAdminUserJpaService.findOneUser(adminUserEntity.getUserId()).getUserId()).isEqualTo("test");
        assertThat(mockAdminUserJpaService.findOneUser(adminUserEntity.getUserId()).getName()).isEqualTo("test");
        assertThat(mockAdminUserJpaService.findOneUser(adminUserEntity.getUserId()).getEmail()).isEqualTo("test@test.com");

        // verify
        then(mockAdminUserJpaService).should(times(3)).findOneUser(adminUserEntity.getUserId());
        then(mockAdminUserJpaService).should(atLeastOnce()).findOneUser(adminUserEntity.getUserId());
        then(mockAdminUserJpaService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("관리자 수정 Mockito 테스트")
    void 관리자수정Mockito테스트() throws Exception {
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
        AdminUserEntity userInfo = mockAdminUserJpaService.findOneUser(newAdminUserEntity.getUserId());

        // then
        assertThat(userInfo.getUserId()).isEqualTo("test1");
        assertThat(userInfo.getName()).isEqualTo("test1");
        assertThat(userInfo.getEmail()).isEqualTo("test1@test.com");

        // verify
        verify(mockAdminUserJpaService, times(1)).findOneUser(newAdminUserEntity.getUserId());
        verify(mockAdminUserJpaService, atLeastOnce()).findOneUser(newAdminUserEntity.getUserId());
        verifyNoMoreInteractions(mockAdminUserJpaService);

        InOrder inOrder = inOrder(mockAdminUserJpaService);
        inOrder.verify(mockAdminUserJpaService).findOneUser(newAdminUserEntity.getUserId());
    }

    @Test
    @DisplayName("관리자 수정 BDD 테스트")
    void 관리자수정BDD테스트() throws Exception {
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
        given(mockAdminUserJpaService.findOneUser(newAdminUserEntity.getUserId())).willReturn(newAdminUserEntity);

        // then
        assertThat(mockAdminUserJpaService.findOneUser(newAdminUserEntity.getUserId()).getUserId()).isEqualTo("test1");
        assertThat(mockAdminUserJpaService.findOneUser(newAdminUserEntity.getUserId()).getName()).isEqualTo("test1");
        assertThat(mockAdminUserJpaService.findOneUser(newAdminUserEntity.getUserId()).getEmail()).isEqualTo("test1@test.com");

        // verify
        then(mockAdminUserJpaService).should(times(3)).findOneUser(newAdminUserEntity.getUserId());
        then(mockAdminUserJpaService).should(atLeastOnce()).findOneUser(newAdminUserEntity.getUserId());
        then(mockAdminUserJpaService).shouldHaveNoMoreInteractions();
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