package com.tsp.new_tsp_admin.api.user.service.repository;

import com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface AdminUserJpaRepository extends JpaRepository<AdminUserEntity, Long> {
}
