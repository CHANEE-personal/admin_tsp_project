package com.tsp.new_tsp_admin.api.support.service;

import com.tsp.new_tsp_admin.api.domain.support.AdminSupportDTO;
import com.tsp.new_tsp_admin.api.domain.support.AdminSupportEntity;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_admin.exception.ApiExceptionType.*;

@Service
@RequiredArgsConstructor
public class AdminSupportJpaServiceImpl implements AdminSupportJpaService {
    private final AdminSupportJpaRepository adminSupportJpaRepository;

    /**
     * <pre>
     * 1. MethodName : findSupportsList
     * 2. ClassName  : AdminSupportJpaServiceImpl.java
     * 3. Comment    : 관리자 지원모델 리스트 갯수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     */
    @Override
    @Transactional(readOnly = true)
    public Integer findSupportsCount(Map<String, Object> supportMap) throws TspException {
        try {
            return adminSupportJpaRepository.findSupportsCount(supportMap);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_SUPPORT_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findSupportsList
     * 2. ClassName  : AdminSupportJpaServiceImpl.java
     * 3. Comment    : 관리자 지원모델 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     */
    @Override
    @Transactional(readOnly = true)
    public List<AdminSupportDTO> findSupportsList(Map<String, Object> supportMap) throws TspException {
        try {
            return adminSupportJpaRepository.findSupportsList(supportMap);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_SUPPORT_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findOneSupportModel
     * 2. ClassName  : AdminSupportJpaServiceImpl.java
     * 3. Comment    : 관리자 지원모델 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     */
    @Override
    @Transactional(readOnly = true)
    public AdminSupportDTO findOneSupportModel(AdminSupportEntity adminSupportEntity) throws TspException {
        try {
            return adminSupportJpaRepository.findOneSupportModel(adminSupportEntity);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_SUPPORT, e);
        }
    }

    @Override
    @Modifying(clearAutomatically = true)
    @Transactional
    public AdminSupportDTO insertSupportModel(AdminSupportEntity adminSupportEntity) throws TspException {
        try {
            return adminSupportJpaRepository.insertSupportModel(adminSupportEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_SUPPORT, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateSupportModel
     * 2. ClassName  : AdminSupportJpaService.java
     * 3. Comment    : 관리자 지원모델 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     */
    @Override
    @Modifying(clearAutomatically = true)
    @Transactional
    public AdminSupportDTO updateSupportModel(AdminSupportEntity adminSupportEntity) throws TspException {
        try {
            return adminSupportJpaRepository.updateSupportModel(adminSupportEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_UPDATE_SUPPORT, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteSupportModel
     * 2. ClassName  : AdminSupportJpaService.java
     * 3. Comment    : 관리자 지원모델 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     */
    @Override
    @Modifying(clearAutomatically = true)
    @Transactional
    public Integer deleteSupportModel(Integer idx) throws TspException {
        try {
            return adminSupportJpaRepository.deleteSupportModel(idx);
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_SUPPORT, e);
        }
    }
}
