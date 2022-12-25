package com.tsp.new_tsp_admin.api.model.service.agency;

import com.tsp.new_tsp_admin.api.common.SaveImage;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageDTO;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import com.tsp.new_tsp_admin.api.domain.model.agency.AdminAgencyDTO;
import com.tsp.new_tsp_admin.api.domain.model.agency.AdminAgencyEntity;
import com.tsp.new_tsp_admin.api.image.service.ImageService;
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
public class AdminAgencyJpaServiceImpl implements AdminAgencyJpaService {
    private final AdminAgencyJpaRepository adminAgencyJpaRepository;
    private final SaveImage saveImage;
    private final ImageService imageService;

    /**
     * <pre>
     * 1. MethodName : findAgencyCount
     * 2. ClassName  : AdminAgencyJpaServiceImpl.java
     * 3. Comment    : 관리자 소속사 리스트 수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 14.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public int findAgencyCount(Map<String, Object> agencyMap) {
        try {
            return adminAgencyJpaRepository.findAgencyCount(agencyMap);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_AGENCY_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findAgencyList
     * 2. ClassName  : AdminAgencyJpaServiceImpl.java
     * 3. Comment    : 관리자 소속사 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 14.
     * </pre>
     */
    @Override
    @Cacheable(value = "agency", key = "#agencyMap")
    @Transactional(readOnly = true)
    public List<AdminAgencyDTO> findAgencyList(Map<String, Object> agencyMap) {
        try {
            return adminAgencyJpaRepository.findAgencyList(agencyMap);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_AGENCY_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findOneAgency
     * 2. ClassName  : AdminAgencyJpaServiceImpl.java
     * 3. Comment    : 관리자 소속사 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 14.
     * </pre>
     */
    @Override
    @Cacheable(value = "agency", key = "#idx")
    @Transactional(readOnly = true)
    public AdminAgencyDTO findOneAgency(Long idx) {
        try {
            return adminAgencyJpaRepository.findOneAgency(idx);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_AGENCY, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : insertAgency
     * 2. ClassName  : AdminAgencyJpaServiceImpl.java
     * 3. Comment    : 관리자 소속사 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 14.
     * </pre>
     */
    @Override
    @CachePut("agency")
    @Modifying(clearAutomatically = true)
    @Transactional
    public AdminAgencyDTO insertAgency(AdminAgencyEntity adminAgencyEntity) {
        try {
            return adminAgencyJpaRepository.insertAgency(adminAgencyEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_AGENCY, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateAgency
     * 2. ClassName  : AdminAgencyJpaServiceImpl.java
     * 3. Comment    : 관리자 소속사 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 14.
     * </pre>
     */
    @Override
    @CachePut(value = "agency", key = "#adminAgencyEntity.idx")
    @Modifying(clearAutomatically = true)
    @Transactional
    public AdminAgencyDTO updateAgency(AdminAgencyEntity adminAgencyEntity) {
        try {
            return adminAgencyJpaRepository.updateAgency(adminAgencyEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_UPDATE_AGENCY, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteAgency
     * 2. ClassName  : AdminAgencyJpaServiceImpl.java
     * 3. Comment    : 관리자 소속사 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 14.
     * </pre>
     */
    @Override
    @CacheEvict(value = "agency", key = "#idx")
    @Modifying(clearAutomatically = true)
    @Transactional
    public Long deleteAgency(Long idx) {
        try {
            return adminAgencyJpaRepository.deleteAgency(idx);
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_AGENCY, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : insertAgencyImage
     * 2. ClassName  : AdminAgencyJpaServiceImpl.java
     * 3. Comment    : 관리자 소속사 이미지 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 14.
     * </pre>
     */
    @Override
    public List<CommonImageDTO> insertAgencyImage(CommonImageEntity commonImageEntity, List<MultipartFile> fileName) {
        try {
            return saveImage.saveFile(fileName, commonImageEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_AGENCY, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteAgencyImage
     * 2. ClassName  : AdminAgencyJpaServiceImpl.java
     * 3. Comment    : 관리자 소속사 이미지 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 14.
     * </pre>
     */
    @Override
    public Long deleteAgencyImage(CommonImageEntity commonImageEntity) {
        try {
            return imageService.deleteImage(commonImageEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_IMAGE, e);
        }
    }
}
