package com.tsp.new_tsp_admin.api.user.service;

import com.tsp.new_tsp_admin.api.domain.user.AdminUserDTO;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = NONE)
class AdminUserJpaServiceTest {
    @Autowired
    private AdminUserJpaService adminUserJpaService;

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
}