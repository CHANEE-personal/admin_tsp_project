package com.tsp.new_tsp_admin.api.model.service;

import com.tsp.new_tsp_admin.api.domain.common.CommonCodeEntity;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelDTO;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import com.tsp.new_tsp_admin.api.image.service.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminModelJpaServiceImpl implements AdminModelJpaService {
    private final AdminModelJpaRepository adminModelJpaRepository;
    private final ImageRepository imageRepository;

    /**
     * <pre>
     * 1. MethodName : findModelsCount
     * 2. ClassName  : AdminModelJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 리스트 수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     */
    @Override
    public int findModelsCount(Map<String, Object> modelMap) {
        return adminModelJpaRepository.findModelsCount(modelMap);
    }

    /**
     * <pre>
     * 1. MethodName : findModelsList
     * 2. ClassName  : AdminModelJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     */
    @Override
    @Cacheable("model")
    public List<AdminModelDTO> findModelsList(Map<String, Object> modelMap) {
        return adminModelJpaRepository.findModelsList(modelMap);
    }

    /**
     * <pre>
     * 1. MethodName : findOneModel
     * 2. ClassName  : AdminModelJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     */
    @Override
    @Cacheable("model")
    public AdminModelDTO findOneModel(AdminModelEntity adminModelEntity) {
        return adminModelJpaRepository.findOneModel(adminModelEntity);
    }

    /**
     * <pre>
     * 1. MethodName : insertModel
     * 2. ClassName  : AdminModelJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 07.
     * </pre>
     *
     */
    @Override
    @CachePut("model")
    public AdminModelDTO insertModel(AdminModelEntity adminModelEntity) {
        return adminModelJpaRepository.insertModel(adminModelEntity);
    }

    /**
     * <pre>
     * 1. MethodName : updateModel
     * 2. ClassName  : AdminModelJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 07.
     * </pre>
     *
     */
    @Override
    @CachePut("model")
    public AdminModelDTO updateModel(AdminModelEntity adminModelEntity) {
        return adminModelJpaRepository.updateModelByEm(adminModelEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deleteModel
     * 2. ClassName  : AdminModelJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 17.
     * </pre>
     *
     */
    @Override
    @CacheEvict("model")
    public AdminModelDTO deleteModel(AdminModelEntity adminModelEntity) {
        return adminModelJpaRepository.deleteModelByEm(adminModelEntity);
    }

    /**
     * <pre>
     * 1. MethodName : insertModelImage
     * 2. ClassName  : AdminModelJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 이미지 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 07.
     * </pre>
     *
     */
    @Override
    public String insertModelImage(CommonImageEntity commonImageEntity, List<MultipartFile> fileName) {
        return imageRepository.uploadImageFile(commonImageEntity, fileName, "insert");
    }

    /**
     * <pre>
     * 1. MethodName : modelCommonCode
     * 2. ClassName  : AdminModelJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 공통 코드 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     */
    @Override
    public List<CommonCodeEntity> modelCommonCode(CommonCodeEntity commonCodeEntity) {
        return adminModelJpaRepository.modelCommonCode(commonCodeEntity);
    }
}
