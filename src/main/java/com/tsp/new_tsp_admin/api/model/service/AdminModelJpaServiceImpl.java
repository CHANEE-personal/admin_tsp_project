package com.tsp.new_tsp_admin.api.model.service;

import com.tsp.new_tsp_admin.api.domain.common.CommonCodeEntity;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelDTO;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AdminModelJpaServiceImpl implements AdminModelJpaService {

    private final AdminModelJpaRepository adminModelJpaRepository;

    /**
     * <pre>
     * 1. MethodName : getModelsCount
     * 2. ClassName  : AdminModeJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 리스트 수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     * @param modelMap
     */
    @Override
    public Long getModelsCount(Map<String, Object> modelMap) {
        return null;
    }

    /**
     * <pre>
     * 1. MethodName : getModelsList
     * 2. ClassName  : AdminModeJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     * @param modelMap
     */
    @Override
    public List<AdminModelEntity> getModelsList(Map<String, Object> modelMap) {
        return adminModelJpaRepository.findAll();
    }

    /**
     * <pre>
     * 1. MethodName : getOneModel
     * 2. ClassName  : AdminModeJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     * @param adminModelEntity
     */
    @Override
    public AdminModelEntity getOneModel(AdminModelEntity adminModelEntity) {
        return null;
    }

    @Override
    public ConcurrentHashMap<String, Object> modelCommonCode(CommonCodeEntity commonCodeEntity) {
        return null;
    }
}
