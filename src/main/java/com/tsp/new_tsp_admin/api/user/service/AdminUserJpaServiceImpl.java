package com.tsp.new_tsp_admin.api.user.service;

import com.tsp.new_tsp_admin.api.domain.user.AdminUserDTO;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity;
import com.tsp.new_tsp_admin.api.user.service.repository.AdminUserJpaRepository;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_admin.exception.ApiExceptionType.*;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminUserJpaServiceImpl implements AdminUserJpaService {
    private final AdminUserJpaRepository adminUserJpaRepository;

    /**
     * <pre>
     * 1. MethodName : findUsersList
     * 2. ClassName  : AdminUserJpaServiceImpl.java
     * 3. Comment    : 관리자 유저 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     */
    @Override
    @Cacheable("user")
    @Transactional(readOnly = true)
    public List<AdminUserDTO> findUsersList(Map<String, Object> userMap) throws TspException {
        try {
            return adminUserJpaRepository.findUsersList(userMap);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_USER_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findOneUser
     * 2. ClassName  : AdminUserJpaServiceImpl.java
     * 3. Comment    : 관리자 유저 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     */
    @Override
    @Cacheable("user")
    @Transactional(readOnly = true)
    public AdminUserEntity findOneUser(String id) throws TspException {
        try {
            return adminUserJpaRepository.findOneUser(id);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_USER, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String findOneUserByToken(String token) throws TspException {
        try {
            return adminUserJpaRepository.findOneUserByToken(token);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_USER, e);
        }
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
    @Transactional(readOnly = true)
    public String adminLogin(AdminUserEntity adminUserEntity) throws TspException {
        try {
            return adminUserJpaRepository.adminLogin(adminUserEntity);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_USER, e);
        }
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
    @Modifying(clearAutomatically = true)
    @Transactional
    public void insertToken(AdminUserEntity paramUserEntity) throws TspException {
        try {
            AdminUserEntity adminUserEntity = adminUserJpaRepository.findOneUser(paramUserEntity.getUserId());
            adminUserJpaRepository.insertUserTokenByEm(adminUserEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_USER, e);
        }
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
    @Modifying(clearAutomatically = true)
    @Transactional
    public AdminUserDTO insertAdminUser(AdminUserEntity adminUserEntity) throws TspException {
        try {
            return adminUserJpaRepository.insertAdminUser(adminUserEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_USER, e);
        }
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
    @Modifying(clearAutomatically = true)
    @Transactional
    public AdminUserDTO updateAdminUser(AdminUserEntity adminUserEntity) {
        try {
            return adminUserJpaRepository.updateAdminUser(adminUserEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_UPDATE_USER, e);
        }
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
    @Modifying(clearAutomatically = true)
    @Transactional
    public Integer deleteAdminUser(Integer idx) throws TspException {
        try {
            return adminUserJpaRepository.deleteAdminUser(idx);
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_USER, e);
        }
    }
}
