package com.tsp.new_tsp_admin.api.user.service;

import com.tsp.new_tsp_admin.api.domain.user.AdminUserDTO;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity;
import com.tsp.new_tsp_admin.api.user.service.repository.AdminUserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
     */
    @Override
    @Cacheable("user")
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
     */
    @Override
    @CachePut("user")
    public void insertToken(AdminUserEntity paramUserEntity) {
        AdminUserEntity adminUserEntity = adminUserJpaRepository.findOneUser(paramUserEntity.getUserId());
        adminUserJpaRepository.insertUserTokenByEm(adminUserEntity);
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
     */
    @Override
    @CachePut("user")
    public AdminUserDTO insertAdminUser(AdminUserEntity adminUserEntity) {
        return adminUserJpaRepository.insertAdminUser(adminUserEntity);
    }

    /**
     * <pre>
     * 1. MethodName : updateAdminUser
     * 2. ClassName  : AdminUserJpaServiceImpl.java
     * 3. Comment    : 관리자 회원 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 11.
     * </pre>
     *
     */
    @Override
    @CachePut("user")
    public AdminUserDTO updateAdminUser(AdminUserEntity adminUserEntity) {
        return adminUserJpaRepository.updateAdminUser(adminUserEntity);
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
     */
    @Override
    @CacheEvict("user")
    public AdminUserDTO deleteAdminUser(AdminUserEntity adminUserEntity) {
        return adminUserJpaRepository.deleteAdminUser(adminUserEntity);
    }
}
