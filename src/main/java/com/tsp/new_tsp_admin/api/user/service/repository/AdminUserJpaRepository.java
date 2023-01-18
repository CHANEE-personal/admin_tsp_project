package com.tsp.new_tsp_admin.api.user.service.repository;

import com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface AdminUserJpaRepository extends JpaRepository<AdminUserEntity, Long> {
    Optional<AdminUserEntity> findByUserId(String id);
    Optional<AdminUserEntity> findByUserToken(String token);
    Optional<AdminUserEntity> findByUserRefreshToken(String refreshToken);
}
