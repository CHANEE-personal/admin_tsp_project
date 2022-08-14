package com.tsp.new_tsp_admin.api.model.service.agency;

import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import com.tsp.new_tsp_admin.api.domain.model.agency.AdminAgencyDTO;
import com.tsp.new_tsp_admin.api.domain.model.agency.AdminAgencyEntity;
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
    public Integer findAgencyCount(Map<String, Object> agencyMap) throws Exception {
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
     * 3. Comment    : 관리자 소속사 리스트 수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 14.
     * </pre>
     */
    @Override
    @Cacheable("agency")
    @Transactional(readOnly = true)
    public List<AdminAgencyDTO> findAgencyList(Map<String, Object> agencyMap) throws Exception {
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
    @Cacheable("agency")
    @Transactional(readOnly = true)
    public AdminAgencyDTO findOneAgency(AdminAgencyEntity adminAgencyEntity) throws Exception {
        try {
            return adminAgencyJpaRepository.findOneAgency(adminAgencyEntity);
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
    public AdminAgencyDTO insertAgency(AdminAgencyEntity adminAgencyEntity) throws Exception {
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
    @CachePut("agency")
    @Modifying(clearAutomatically = true)
    @Transactional
    public AdminAgencyDTO updateAgency(AdminAgencyEntity adminAgencyEntity) throws Exception {
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
    @CacheEvict("agency")
    @Modifying(clearAutomatically = true)
    @Transactional
    public Integer deleteAgency(Integer idx) throws Exception {
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
    public String insertAgencyImage(CommonImageEntity commonImageEntity, List<MultipartFile> fileName) throws Exception {
        return null;
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
    public Integer deleteAgencyImage(Integer idx) throws Exception {
        return null;
    }
}
