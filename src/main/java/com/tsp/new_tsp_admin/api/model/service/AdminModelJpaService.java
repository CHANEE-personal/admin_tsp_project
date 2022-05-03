package com.tsp.new_tsp_admin.api.model.service;

import com.tsp.new_tsp_admin.api.domain.common.CommonCodeEntity;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelDTO;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface AdminModelJpaService {

    /**
     * <pre>
     * 1. MethodName : getModelsCount
     * 2. ClassName  : AdminModeJpaService.java
     * 3. Comment    : 관리자 모델 리스트 수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     * @param modelMap
     */
    Long getModelsCount(Map<String, Object> modelMap);

    /**
     * <pre>
     * 1. MethodName : getModelsList
     * 2. ClassName  : AdminModeJpaService.java
     * 3. Comment    : 관리자 모델 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     * @param modelMap
     */
    List<AdminModelEntity> getModelsList(Map<String, Object> modelMap);

    /**
     * <pre>
     * 1. MethodName : getOneModel
     * 2. ClassName  : AdminModeJpaService.java
     * 3. Comment    : 관리자 모델 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     * @param adminModelEntity
     */
    AdminModelEntity getOneModel(AdminModelEntity adminModelEntity);

    /**
     * <pre>
     * 1. MethodName : modelCommonCode
     * 2. ClassName  : AdminModeJpaService.java
     * 3. Comment    : 관리자 모델 공통 코드 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     * @param commonCodeEntity
     */
    ConcurrentHashMap<String, Object> modelCommonCode(CommonCodeEntity commonCodeEntity);
}