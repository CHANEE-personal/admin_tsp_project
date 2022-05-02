package com.tsp.new_tsp_admin.api.model.service;

import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminModelJpaRepository extends JpaRepository<AdminModelEntity, Long> {


}
