package com.tsp.new_tsp_admin.api.model.service;

import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentDTO;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelDTO;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import com.tsp.new_tsp_admin.api.domain.model.schedule.AdminScheduleDTO;
import com.tsp.new_tsp_admin.api.image.service.ImageRepository;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_admin.exception.ApiExceptionType.*;

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
     */
    @Override
    @Transactional(readOnly = true)
    public Integer findModelsCount(Map<String, Object> modelMap) throws TspException {
        try {
            return adminModelJpaRepository.findModelsCount(modelMap);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_MODEL_LIST, e);
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
     */
    @Override
    @Cacheable("model")
    @Transactional(readOnly = true)
    public List<AdminModelDTO> findModelsList(Map<String, Object> modelMap) throws TspException {
        try {
            return adminModelJpaRepository.findModelsList(modelMap);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_MODEL_LIST, e);
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
     */
    @Override
    @Cacheable("model")
    @Transactional(readOnly = true)
    public AdminModelDTO findOneModel(AdminModelEntity adminModelEntity) throws TspException {
        try {
            return adminModelJpaRepository.findOneModel(adminModelEntity);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_MODEL, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneModel
     * 2. ClassName  : AdminModelJpaServiceImpl.java
     * 3. Comment    : 관리자 이전 모델 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 12.
     * </pre>
     */
    @Override
    @Cacheable("model")
    @Transactional(readOnly = true)
    public AdminModelDTO findPrevOneModel(AdminModelEntity adminModelEntity) throws TspException {
        try {
            return adminModelJpaRepository.findPrevOneModel(adminModelEntity);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_MODEL, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneModel
     * 2. ClassName  : AdminModelJpaServiceImpl.java
     * 3. Comment    : 관리자 다음 모델 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 12.
     * </pre>
     */
    @Override
    @Cacheable("model")
    @Transactional(readOnly = true)
    public AdminModelDTO findNextOneModel(AdminModelEntity adminModelEntity) throws TspException {
        try {
            return adminModelJpaRepository.findNextOneModel(adminModelEntity);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_MODEL, e);
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
     */
    @Override
    @CachePut("model")
    @Modifying(clearAutomatically = true)
    @Transactional
    public AdminModelDTO insertModel(AdminModelEntity adminModelEntity) throws TspException {
        try {
            return adminModelJpaRepository.insertModel(adminModelEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_MODEL, e);
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
     */
    @Override
    @CachePut("model")
    @Modifying(clearAutomatically = true)
    @Transactional
    public AdminModelDTO updateModel(AdminModelEntity adminModelEntity) throws TspException {
        try {
            return adminModelJpaRepository.updateModelByEm(adminModelEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_UPDATE_MODEL, e);
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
     */
    @Override
    @CacheEvict("model")
    @Modifying(clearAutomatically = true)
    @Transactional
    public Integer deleteModel(Integer idx) throws TspException {
        try {
            return adminModelJpaRepository.deleteModelByEm(idx);
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_MODEL, e);
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
     */
    @Override
    @Modifying(clearAutomatically = true)
    @Transactional
    public String insertModelImage(CommonImageEntity commonImageEntity, List<MultipartFile> fileName) throws TspException {
        try {
            return imageRepository.uploadImageFile(commonImageEntity, fileName, "insert");
        } catch (Exception e) {
            throw new TspException(ERROR_MODEL, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteModelImage
     * 2. ClassName  : AdminModelJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 이미지 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 07.
     * </pre>
     */
    @Override
    public Integer deleteModelImage(Integer idx) {
        return imageRepository.deleteModelImage(idx);
    }

    /**
     * <pre>
     * 1. MethodName : updateModelAgency
     * 2. ClassName  : AdminModelJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 소속사 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 14.
     * </pre>
     */
    @Override
    @CachePut("model")
    @Modifying(clearAutomatically = true)
    @Transactional
    public AdminModelDTO updateModelAgency(AdminModelEntity adminModelEntity) {
        try {
            return adminModelJpaRepository.updateModelAgency(adminModelEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_UPDATE_MODEL, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findModelAdminComment
     * 2. ClassName  : AdminModelJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 어드민 코멘트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 26.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public List<AdminCommentDTO> findModelAdminComment(AdminModelEntity adminModelEntity) throws TspException {
        try {
            return adminModelJpaRepository.findModelAdminComment(adminModelEntity);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_COMMENT_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findNewModelsCount
     * 2. ClassName  : AdminModelJpaServiceImpl.java
     * 3. Comment    : 관리자 새로운 모델 리스트 수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 29.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public Integer findNewModelsCount(Map<String, Object> modelMap) throws TspException {
        try {
            return adminModelJpaRepository.findNewModelsCount(modelMap);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_MODEL_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findNewModelsList
     * 2. ClassName  : AdminModelJpaServiceImpl.java
     * 3. Comment    : 관리자 새로운 모델 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 29.
     * </pre>
     */
    @Override
    @Cacheable("model")
    @Transactional(readOnly = true)
    public List<AdminModelDTO> findNewModelsList(Map<String, Object> modelMap) throws TspException {
        try {
            return adminModelJpaRepository.findNewModelsList(modelMap);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_MODEL_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : toggleModelNewYn
     * 2. ClassName  : AdminModelJpaServiceImpl.java
     * 3. Comment    : 관리자 새로운 모델 설정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 29.
     * </pre>
     */
    @Override
    @CachePut("model")
    @Modifying(clearAutomatically = true)
    @Transactional
    public AdminModelDTO toggleModelNewYn(AdminModelEntity adminModelEntity) throws Exception {
        try {
            return adminModelJpaRepository.toggleModelNewYn(adminModelEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_UPDATE_MODEL, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findOneModelSchedule
     * 2. ClassName  : AdminModelJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 스케줄 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 03.
     * </pre>
     */
    @Override
    @Cacheable("schedule")
    @Transactional(readOnly = true)
    public List<AdminScheduleDTO> findOneModelSchedule(AdminModelEntity adminModelEntity) throws TspException {
        try {
            return adminModelJpaRepository.findOneModelSchedule(adminModelEntity);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_MODEL_SCHEDULE_LIST, e);
        }
    }
}
