package com.tsp.new_tsp_admin.api.user.service;

import com.tsp.new_tsp_admin.api.domain.user.AdminUserDTO;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AdminUserJpaService {

    /**
     * <pre>
     * 1. MethodName : findUserList
     * 2. ClassName  : AdminUserJpaService.java
     * 3. Comment    : Admin User 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    Page<AdminUserDTO> findUserList(Map<String, Object> userMap, PageRequest pageRequest);

    /**
     * <pre>
     * 1. MethodName : findOneUser
     * 2. ClassName  : AdminUserJpaService.java
     * 3. Comment    : Admin User 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    AdminUserEntity findOneUser(String id);

    /**
     * <pre>
     * 1. MethodName : findOneUserByToken
     * 2. ClassName  : AdminUserJpaService.java
     * 3. Comment    : token을 활용한 Admin User 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    String findOneUserByToken(String token);

    /**
     * <pre>
     * 1. MethodName : adminLogin
     * 2. ClassName  : AdminUserJpaService.java
     * 3. Comment    : 관리자 로그인 처리
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    String adminLogin(AdminUserEntity adminUserEntity);

    /**
     * <pre>
     * 1. MethodName : insertToken
     * 2. ClassName  : AdminUserJpaService.java
     * 3. Comment    : 관리자 토큰 저장
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    void insertToken(AdminUserEntity adminUserEntity);

    /**
     * <pre>
     * 1. MethodName : insertAdminUser
     * 2. ClassName  : AdminUserJpaService.java
     * 3. Comment    : 관리자 회원가입
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 11.
     * </pre>
     */
    AdminUserDTO insertAdminUser(AdminUserEntity adminUserEntity);

    /**
     * <pre>
     * 1. MethodName : updateAdminUser
     * 2. ClassName  : AdminUserJpaService.java
     * 3. Comment    : 관리자 회원 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 11.
     * </pre>
     */
    AdminUserDTO updateAdminUser(Long idx, AdminUserEntity adminUserEntity);

    /**
     * <pre>
     * 1. MethodName : deleteAdminUser
     * 2. ClassName  : AdminUserJpaService.java
     * 3. Comment    : 관리자 회원 탈퇴
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 11.
     * </pre>
     */
    Long deleteAdminUser(Long idx);
}
