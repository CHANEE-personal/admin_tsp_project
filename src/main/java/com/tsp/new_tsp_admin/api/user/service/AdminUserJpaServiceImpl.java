package com.tsp.new_tsp_admin.api.user.service;

import com.tsp.new_tsp_admin.api.domain.user.AdminUserDTO;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity;
import com.tsp.new_tsp_admin.api.user.service.repository.AdminUserJpaRepository;
import com.tsp.new_tsp_admin.common.StringUtils;
import com.tsp.new_tsp_admin.exception.ApiExceptionType;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminUserJpaServiceImpl implements AdminUserJpaService {

    private final AdminUserJpaRepository adminUserJpaRepository;

    /**
     * <pre>
     * 1. MethodName : getAdminUserList
     * 2. ClassName  : AdminUserJpaServiceImpl.java
     * 3. Comment    : 관리자 유저 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     * @param userMap
     */
    @Override
    public List<AdminUserDTO> getAdminUserList(Map<String, Object> userMap) {
        return adminUserJpaRepository.findUserList(userMap);
    }

    /**
     * <pre>
     * 1. MethodName : adminLogin
     * 2. ClassName  : AdminUserJpaServiceImpl.java
     * 3. Comment    : 관리자 로그인 처리
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     * @param adminUserEntity
     */
    @Override
    public String adminLogin(AdminUserEntity adminUserEntity) {
        return adminUserJpaRepository.adminLogin(adminUserEntity);
    }

    /**
     * <pre>
     * 1. MethodName : insertToken
     * 2. ClassName  : AdminUserJpaServiceImpl.java
     * 3. Comment    : 관리자 토큰 저장
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     * @param paramUserEntity
     */
    @Override
    public void insertToken(AdminUserEntity paramUserEntity) {
        AdminUserEntity adminUserEntity = adminUserJpaRepository.findOneUser(paramUserEntity.getUserId());
        adminUserJpaRepository.insertUserToken(adminUserEntity);
    }

    /**
     * <pre>
     * 1. MethodName : insertAdminUser
     * 2. ClassName  : AdminUserJpaServiceImpl.java
     * 3. Comment    : 관리자 회원가입
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 11.
     * </pre>
     *
     * @param adminUserEntity
     */
    @Override
    public Integer insertAdminUser(AdminUserEntity adminUserEntity) {
        return adminUserJpaRepository.insertAdminUser(adminUserEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deleteAdminUser
     * 2. ClassName  : AdminUserJpaServiceImpl.java
     * 3. Comment    : 관리자 회원 탈퇴
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 11.
     * </pre>
     *
     * @param adminUserEntity
     */
    @Override
    public Integer deleteAdminUser(AdminUserEntity adminUserEntity) {
        return adminUserJpaRepository.deleteAdminUser(adminUserEntity);
    }
}
