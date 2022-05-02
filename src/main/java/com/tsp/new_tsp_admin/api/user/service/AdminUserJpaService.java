package com.tsp.new_tsp_admin.api.user.service;

import com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity;

import java.util.List;

public interface AdminUserJpaService {

//    void signUpUser(AdminUserEntity adminUserEntity);

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
    List<AdminUserEntity> getAdminUserList();

    String adminLogin(AdminUserEntity adminUserEntity);

    void saveToken(String userId, String token);
}
