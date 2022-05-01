package com.tsp.new_tsp_admin.api.User.Service.Repository;

import com.tsp.new_tsp_admin.api.domain.User.AdminUserEntity;
import com.tsp.new_tsp_admin.api.jwt.SecurityUser;
import org.springframework.data.repository.CrudRepository;

public interface AdminUserJpaRepository extends CrudRepository<AdminUserEntity, Long> {

    SecurityUser findAdminUserEntityByUserId(String id);

    String findAdminUserEntityByPassword(String id);

    void saveToken(String token);
}
