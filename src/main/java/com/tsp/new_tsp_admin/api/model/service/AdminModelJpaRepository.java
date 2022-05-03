package com.tsp.new_tsp_admin.api.model.service;

import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminModelJpaRepository extends JpaRepository<AdminModelEntity, Long> {

    /**
     * <pre>
     * 1. MethodName : findAll
     * 2. ClassName  : AdminModeJpaRepository.java
     * 3. Comment    : 관리자 모델 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     */
    List<AdminModelEntity> findAll();

    /**
     * <pre>
     * 1. MethodName : findAdminModelEntityByUserId
     * 2. ClassName  : AdminModeJpaRepository.java
     * 3. Comment    : 관리자 모델 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     */
    AdminModelEntity findAdminModelEntityByUserId(AdminModelEntity adminModelEntity);
}
