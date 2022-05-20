package com.tsp.new_tsp_admin.api.model.service;

import com.tsp.new_tsp_admin.api.domain.common.CommonCodeEntity;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelDTO;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface AdminModelJpaService {

    /**
     * <pre>
     * 1. MethodName : findModelsCount
     * 2. ClassName  : AdminModeJpaService.java
     * 3. Comment    : 관리자 모델 리스트 수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     * @param modelMap
     */
    Long findModelsCount(Map<String, Object> modelMap);

    /**
     * <pre>
     * 1. MethodName : findModelsList
     * 2. ClassName  : AdminModeJpaService.java
     * 3. Comment    : 관리자 모델 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     * @param modelMap
     */
    List<AdminModelDTO> findModelsList(Map<String, Object> modelMap);

    /**
     * <pre>
     * 1. MethodName : findOneModel
     * 2. ClassName  : AdminModeJpaService.java
     * 3. Comment    : 관리자 모델 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     * @param adminModelEntity
     */
    AdminModelDTO findOneModel(AdminModelEntity adminModelEntity);

    /**
     * <pre>
     * 1. MethodName : insertModel
     * 2. ClassName  : AdminModeJpaService.java
     * 3. Comment    : 관리자 모델 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     * @param adminModelEntity
     */
    AdminModelDTO insertModel(AdminModelEntity adminModelEntity);

    /**
     * <pre>
     * 1. MethodName : updateModel
     * 2. ClassName  : AdminModeJpaService.java
     * 3. Comment    : 관리자 모델 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     * @param adminModelEntity
     */
    AdminModelDTO updateModel(AdminModelEntity adminModelEntity);

    /**
     * <pre>
     * 1. MethodName : deleteModel
     * 2. ClassName  : AdminModeJpaService.java
     * 3. Comment    : 관리자 모델 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 17.
     * </pre>
     *
     * @param adminModelEntity
     */
    AdminModelDTO deleteModel(AdminModelEntity adminModelEntity);

    /**
     * <pre>
     * 1. MethodName : insertModelImage
     * 2. ClassName  : AdminModeJpaService.java
     * 3. Comment    : 관리자 모델 이미지 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     * @param commonImageEntity
     */
    Integer insertModelImage(AdminModelEntity adminModelEntity,
                             CommonImageEntity commonImageEntity,
                             MultipartFile[] fileName);

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
