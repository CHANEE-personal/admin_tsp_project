package com.tsp.new_tsp_admin.api.common.service;

import com.tsp.new_tsp_admin.api.domain.common.CommonCodeDTO;
import com.tsp.new_tsp_admin.api.domain.common.CommonCodeEntity;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_admin.exception.ApiExceptionType.*;

@Service
@RequiredArgsConstructor
public class AdminCommonJpaServiceImpl implements AdminCommonJpaService {

    private final AdminCommonJpaRepository adminCommonJpaRepository;

    /**
     * <pre>
     * 1. MethodName : findCommonCodeListCount
     * 2. ClassName  : AdminCommonJpaServiceImpl.java
     * 3. Comment    : 관리자 공통 코드 리스트 수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    @Override
    public Integer findCommonCodeListCount() throws TspException {
        try {
            return adminCommonJpaRepository.findCommonCodeListCount();
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_COMMON_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findCommonCodeList
     * 2. ClassName  : AdminCommonJpaServiceImpl.java
     * 3. Comment    : 관리자 공통 코드 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public List<CommonCodeDTO> findCommonCodeList(Map<String, Object> commonMap) throws TspException {
        try {
            return adminCommonJpaRepository.findCommonCodeList(commonMap);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_COMMON_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findOneCommonCode
     * 2. ClassName  : AdminCommonJpaServiceImpl.java
     * 3. Comment    : 관리자 공통 코드 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public CommonCodeDTO findOneCommonCode(CommonCodeEntity commonCodeEntity) throws TspException {
        try {
            return adminCommonJpaRepository.findOneCommonCode(commonCodeEntity);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_COMMON, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : insertCommonCode
     * 2. ClassName  : AdminCommonJpaServiceImpl.java
     * 3. Comment    : 관리자 공통 코드 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    @Override
    @CachePut("common")
    @Modifying(clearAutomatically = true)
    @Transactional
    public CommonCodeDTO insertCommonCode(CommonCodeEntity commonCodeEntity) throws TspException {
        try {
            return adminCommonJpaRepository.insertCommonCode(commonCodeEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_COMMON, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateCommonCode
     * 2. ClassName  : AdminCommonJpaServiceImpl.java
     * 3. Comment    : 관리자 공통 코드 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    @Override
    @CachePut("common")
    @Modifying(clearAutomatically = true)
    @Transactional
    public CommonCodeDTO updateCommonCode(CommonCodeEntity commonCodeEntity) throws TspException {
        try {
            return adminCommonJpaRepository.updateCommonCode(commonCodeEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_UPDATE_COMMON, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteCommonCode
     * 2. ClassName  : AdminCommonJpaServiceImpl.java
     * 3. Comment    : 관리자 공통 코드 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    @Override
    @CacheEvict("common")
    @Modifying(clearAutomatically = true)
    @Transactional
    public Long deleteCommonCode(Long idx) throws TspException {
        try {
            return adminCommonJpaRepository.deleteCommonCode(idx);
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_COMMON, e);
        }
    }
}
