package com.tsp.new_tsp_admin.api.user.service;

import com.tsp.new_tsp_admin.api.domain.user.AdminUserDTO;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity;
import com.tsp.new_tsp_admin.api.user.service.repository.AdminUserJpaQueryRepository;
import com.tsp.new_tsp_admin.api.user.service.repository.AdminUserJpaRepository;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.tsp.new_tsp_admin.exception.ApiExceptionType.*;

@Service
@RequiredArgsConstructor
public class AdminUserJpaServiceImpl implements AdminUserJpaService {
    private final AdminUserJpaQueryRepository adminUserJpaQueryRepository;
    private final AdminUserJpaRepository adminUserJpaRepository;

    private AdminUserEntity oneUser(Long idx) {
        return adminUserJpaRepository.findById(idx)
                .orElseThrow(() -> new TspException(NOT_FOUND_USER));
    }

    /**
     * <pre>
     * 1. MethodName : findUsersList
     * 2. ClassName  : AdminUserJpaServiceImpl.java
     * 3. Comment    : 관리자 유저 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AdminUserDTO> findUserList(Map<String, Object> userMap, PageRequest pageRequest) {
        return adminUserJpaQueryRepository.findUserList(userMap, pageRequest);
    }

    /**
     * <pre>
     * 1. MethodName : findOneUser
     * 2. ClassName  : AdminUserJpaServiceImpl.java
     * 3. Comment    : 관리자 유저 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public AdminUserEntity findOneUser(String id) {
        return adminUserJpaQueryRepository.findOneUser(id);
    }

    /**
     * <pre>
     * 1. MethodName : findOneUserByToken
     * 2. ClassName  : AdminUserJpaServiceImpl.java
     * 3. Comment    : 관리자 토큰을 이용한 유저 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public String findOneUserByToken(String token) {
        try {
            return adminUserJpaQueryRepository.findOneUserByToken(token);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_USER);
        }
    }

    /**
     * <pre>
     * 1. MethodName : adminLogin
     * 2. ClassName  : AdminUserJpaServiceImpl.java
     * 3. Comment    : 관리자 로그인 처리
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public String adminLogin(AdminUserEntity adminUserEntity) {
        try {
            return adminUserJpaQueryRepository.adminLogin(adminUserEntity);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_USER);
        }
    }

    /**
     * <pre>
     * 1. MethodName : insertToken
     * 2. ClassName  : AdminUserJpaServiceImpl.java
     * 3. Comment    : 관리자 토큰 저장
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @Override
    @Transactional
    public void insertToken(AdminUserEntity paramUserEntity) {
        try {
            AdminUserEntity adminUserEntity = adminUserJpaQueryRepository.findOneUser(paramUserEntity.getUserId());
            adminUserJpaQueryRepository.insertUserTokenByEm(adminUserEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_USER);
        }
    }

    /**
     * <pre>
     * 1. MethodName : insertAdminUser
     * 2. ClassName  : AdminUserJpaServiceImpl.java
     * 3. Comment    : 관리자 회원가입
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 11.
     * </pre>
     */
    @Override
    @Transactional
    public AdminUserDTO insertAdminUser(AdminUserEntity adminUserEntity) {
        try {
            return AdminUserEntity.toDto(adminUserJpaRepository.save(adminUserEntity));
        } catch (Exception e) {
            throw new TspException(ERROR_USER);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateAdminUser
     * 2. ClassName  : AdminUserJpaServiceImpl.java
     * 3. Comment    : 관리자 회원 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 11.
     * </pre>
     */
    @Override
    @Transactional
    public AdminUserDTO updateAdminUser(Long idx, AdminUserEntity adminUserEntity) {
        try {
            oneUser(idx).update(adminUserEntity);
            return AdminUserEntity.toDto(adminUserEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_UPDATE_USER);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteAdminUser
     * 2. ClassName  : AdminUserJpaServiceImpl.java
     * 3. Comment    : 관리자 회원 탈퇴
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 11.
     * </pre>
     */
    @Override
    @Transactional
    public Long deleteAdminUser(Long idx) {
        try {
            adminUserJpaRepository.deleteById(idx);
            return idx;
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_USER);
        }
    }
}
