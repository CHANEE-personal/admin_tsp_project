package com.tsp.new_tsp_admin.api.model.service;

import com.tsp.new_tsp_admin.api.domain.common.CommonCodeEntity;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelDTO;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import com.tsp.new_tsp_admin.api.image.service.ImageRepository;
import com.tsp.new_tsp_admin.exception.ApiExceptionType;
import com.tsp.new_tsp_admin.exception.TspException;
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
    public Long findModelsCount(Map<String, Object> modelMap) {
        try {
            return adminModelJpaRepository.findModelsCount(modelMap);
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.NOT_FOUND_MODEL_LIST);
        }
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
        try {
            return adminModelJpaRepository.findModelsList(modelMap);
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.NOT_FOUND_MODEL_LIST);
        }
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
        try {
            return adminModelJpaRepository.findOneModel(adminModelEntity);
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.NOT_FOUND_MODEL);
        }
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
        try {
            return adminModelJpaRepository.insertModel(adminModelEntity);
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.ERROR_MODEL);
        }
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
        try {
            return adminModelJpaRepository.updateModelByEm(adminModelEntity);
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.ERROR_UPDATE_MODEL);
        }
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
        try {
            return adminModelJpaRepository.deleteModelByEm(adminModelEntity);
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.ERROR_DELETE_MODEL);
        }
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
        try {
            return imageRepository.uploadImageFile(commonImageEntity, fileName, "insert");
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.ERROR_IMAGE);
        }
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
