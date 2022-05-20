package com.tsp.new_tsp_admin.api.model.service;

import com.tsp.new_tsp_admin.api.domain.common.CommonCodeEntity;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelDTO;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import com.tsp.new_tsp_admin.api.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AdminModelJpaServiceImpl implements AdminModelJpaService {

    private final AdminModelJpaRepository adminModelJpaRepository;
    private final ImageService imageService;

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
    public AdminModelDTO insertModel(AdminModelEntity adminModelEntity) {
        return adminModelJpaRepository.insertModel(adminModelEntity);
    }

    /**
     * <pre>
     * 1. MethodName : updateModel
     * 2. ClassName  : AdminModeJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     * @param adminModelEntity
     */
    @Override
    public AdminModelDTO updateModel(AdminModelEntity adminModelEntity) {
        return adminModelJpaRepository.updateModelByEm(adminModelEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deleteModel
     * 2. ClassName  : AdminModeJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 17.
     * </pre>
     *
     * @param adminModelEntity
     */
    @Override
    public AdminModelDTO deleteModel(AdminModelEntity adminModelEntity) {
        return adminModelJpaRepository.deleteModelByEm(adminModelEntity);
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
    public Integer insertModelImage(AdminModelEntity adminModelEntity,
                                    CommonImageEntity commonImageEntity,
                                    MultipartFile[] fileName) {
        CommonImageEntity.builder().typeName("model").typeIdx(adminModelEntity.getIdx()).visible("Y").build();
        return adminModelJpaRepository.insertModelImage(commonImageEntity);
    }

    /**
     * <pre>
     * 1. MethodName : modelCommonCode
     * 2. ClassName  : AdminModelJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 공통 코드 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2021. 09. 08.
     * </pre>
     *
     */
    @Override
    public ConcurrentHashMap<String, Object> modelCommonCode(CommonCodeEntity commonCodeEntity) {
        return null;
    }
}
