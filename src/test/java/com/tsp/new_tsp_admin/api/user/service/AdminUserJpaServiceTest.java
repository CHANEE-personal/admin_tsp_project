package com.tsp.new_tsp_admin.api.user.service;

import com.tsp.new_tsp_admin.api.domain.user.AdminUserDTO;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity.builder;
import static org.assertj.core.api.Assertions.assertThat;
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
    @DisplayName("관리자 회원 상세 조회 테스트")
    void 관리자회원상세조회테스트() throws Exception {
        AdminUserEntity adminUserEntity = adminUserJpaService.findOneUser("admin01");
        assertThat(adminUserEntity).isNotNull();
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
        AdminUserEntity adminUserEntity = builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .build();

        adminUserJpaService.insertAdminUser(adminUserEntity);

        when(mockAdminUserJpaService.findOneUser(adminUserEntity.getUserId())).thenReturn(adminUserEntity);

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
        AdminUserEntity adminUserEntity = builder()
                .userId("test")
                .password("test")
                .name("test")
                .email("test@test.com")
                .visible("Y")
                .build();

        Integer idx = adminUserJpaService.insertAdminUser(adminUserEntity).getIdx();

        assertThat(adminUserJpaService.deleteAdminUser(idx)).isNotNull();
    }
}