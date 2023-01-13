package com.tsp.new_tsp_admin.api.common.service;

import com.tsp.new_tsp_admin.api.domain.common.NewCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface AdminCommonJpaRepository extends JpaRepository<NewCodeEntity, Long> {
    Optional<NewCodeEntity> findByCategoryCd(Integer categoryCd);
}
