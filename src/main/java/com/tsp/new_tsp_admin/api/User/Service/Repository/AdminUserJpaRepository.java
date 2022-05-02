package com.tsp.new_tsp_admin.api.User.Service.Repository;

import com.tsp.new_tsp_admin.api.domain.User.AdminUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdminUserJpaRepository extends JpaRepository<AdminUserEntity, Long> {

    List<AdminUserEntity> findAll();

    AdminUserEntity findAdminUserEntityByUserId(String id);

    @Query("select a.password from AdminUserEntity a where a.userId = ?1")
    String findAdminUserEntityByPassword(String id);
}
