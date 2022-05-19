package com.tsp.new_tsp_admin.api.user.service;

import com.tsp.new_tsp_admin.api.domain.user.AdminUserDTO;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity;

import java.util.List;
import java.util.Map;

public interface AdminUserJpaService {

    /**
     * <pre>
     * 1. MethodName : getAdminUserList
     * 2. ClassName  : AdminUserJpaService.java
     * 3. Comment    : Admin User 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     * @return
     * @throws Exception
     */
    List<AdminUserDTO> getAdminUserList(Map<String, Object> userMap);

    /**
     * <pre>
     * 1. MethodName : adminLogin
     * 2. ClassName  : AdminUserJpaService.java
     * 3. Comment    : 관리자 로그인 처리
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     * @return
     * @throws Exception
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
     *
     * @return
     * @throws Exception
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
     *
     * @param adminUserEntity
     * @throws Exception
     */
    Integer insertAdminUser(AdminUserEntity adminUserEntity);

    /**
     * <pre>
     * 1. MethodName : deleteAdminUser
     * 2. ClassName  : AdminUserJpaService.java
     * 3. Comment    : 관리자 회원 탈퇴
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 11.
     * </pre>
     *
     * @param adminUserEntity
     * @throws Exception
     */
    AdminUserDTO deleteAdminUser(AdminUserEntity adminUserEntity);
}
