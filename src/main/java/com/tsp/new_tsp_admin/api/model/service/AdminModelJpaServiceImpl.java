package com.tsp.new_tsp_admin.api.model.service;

import com.tsp.new_tsp_admin.api.domain.common.CommonCodeEntity;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
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
     * 1. MethodName : findModelsCount
     * 2. ClassName  : AdminModeJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 리스트 수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     * @param modelMap
     */
    @Override
    public Long findModelsCount(Map<String, Object> modelMap) {
        return adminModelJpaRepository.findModelsCount(modelMap);
    }

    /**
     * <pre>
     * 1. MethodName : findModelsList
     * 2. ClassName  : AdminModeJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     * @param modelMap
     */
    @Override
    public List<AdminModelDTO> findModelsList(Map<String, Object> modelMap) {
        return adminModelJpaRepository.findModelsList(modelMap);
    }

    /**
     * <pre>
     * 1. MethodName : findOneModel
     * 2. ClassName  : AdminModeJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     * @param adminModelEntity
     */
    @Override
    public AdminModelDTO findOneModel(AdminModelEntity adminModelEntity) {
        return adminModelJpaRepository.findOneModel(adminModelEntity);
    }

    /**
     * <pre>
     * 1. MethodName : insertModel
     * 2. ClassName  : AdminModeJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     * @param adminModelEntity
     */
    @Override
    public Integer insertModel(AdminModelEntity adminModelEntity) {
        return adminModelJpaRepository.insertModel(adminModelEntity);
    }

    /**
     * <pre>
     * 1. MethodName : insertModelImage
     * 2. ClassName  : AdminModeJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 이미지 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     * @param commonImageEntity
     */
    @Override
    public Integer insertModelImage(CommonImageEntity commonImageEntity) {
        return adminModelJpaRepository.insertModelImage(commonImageEntity);
    }

    @Override
    public ConcurrentHashMap<String, Object> modelCommonCode(CommonCodeEntity commonCodeEntity) {
        return null;
    }
}
