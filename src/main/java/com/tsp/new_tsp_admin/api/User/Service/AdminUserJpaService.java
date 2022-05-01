package com.tsp.new_tsp_admin.api.User.Service;

import com.tsp.new_tsp_admin.api.domain.User.AdminUserEntity;

public interface AdminUserJpaService {

//    void signUpUser(AdminUserEntity adminUserEntity);

    String adminLogin(AdminUserEntity adminUserEntity);

    void saveToken(String userId, String token);
}
