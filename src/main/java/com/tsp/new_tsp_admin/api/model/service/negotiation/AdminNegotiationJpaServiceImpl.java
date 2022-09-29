package com.tsp.new_tsp_admin.api.model.service.negotiation;

import com.tsp.new_tsp_admin.api.domain.model.AdminModelDTO;
import com.tsp.new_tsp_admin.api.domain.model.negotiation.AdminNegotiationDTO;
import com.tsp.new_tsp_admin.api.domain.model.negotiation.AdminNegotiationEntity;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_admin.exception.ApiExceptionType.*;

@Service
@RequiredArgsConstructor
public class AdminNegotiationJpaServiceImpl implements AdminNegotiationJpaService {

    private final AdminNegotiationJpaRepository adminNegotiationJpaRepository;

    /**
     * <pre>
     * 1. MethodName : findNegotiationCount
     * 2. ClassName  : AdminNegotiationJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 섭외 리스트 수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 09.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public Integer findNegotiationCount(Map<String, Object> negotiationMap) throws TspException {
        try {
            return adminNegotiationJpaRepository.findNegotiationCount(negotiationMap);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_MODEL_NEGOTIATION_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findModelNegotiationList
     * 2. ClassName  : AdminNegotiationJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 섭외 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 09.
     * </pre>
     */
    @Override
    @Cacheable("model")
    @Transactional(readOnly = true)
    public List<AdminModelDTO> findModelNegotiationList(Map<String, Object> negotiationMap) throws TspException {
        try {
            return adminNegotiationJpaRepository.findModelNegotiationList(negotiationMap);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_MODEL_NEGOTIATION_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findOneNegotiation
     * 2. ClassName  : AdminNegotiationJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 섭외 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 09.
     * </pre>
     */
    @Override
    @Cacheable("negotiation")
    @Transactional(readOnly = true)
    public AdminNegotiationDTO findOneNegotiation(AdminNegotiationEntity adminNegotiationEntity) throws TspException {
        try {
            return adminNegotiationJpaRepository.findOneNegotiation(adminNegotiationEntity);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_MODEL_NEGOTIATION, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneNegotiation
     * 2. ClassName  : AdminNegotiationJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 섭외 이전 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 21.
     * </pre>
     */
    @Override
    @Cacheable("negotiation")
    @Transactional(readOnly = true)
    public AdminNegotiationDTO findPrevOneNegotiation(AdminNegotiationEntity adminNegotiationEntity) throws TspException {
        try {
            return adminNegotiationJpaRepository.findPrevOneNegotiation(adminNegotiationEntity);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_MODEL_NEGOTIATION, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneNegotiation
     * 2. ClassName  : AdminNegotiationJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 섭외 다음 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 21.
     * </pre>
     */
    @Override
    @Cacheable("negotiation")
    @Transactional(readOnly = true)
    public AdminNegotiationDTO findNextOneNegotiation(AdminNegotiationEntity adminNegotiationEntity) throws TspException {
        try {
            return adminNegotiationJpaRepository.findNextOneNegotiation(adminNegotiationEntity);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_MODEL_NEGOTIATION, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : insertModelNegotiation
     * 2. ClassName  : AdminNegotiationJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 섭외 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 09.
     * </pre>
     */
    @Override
    @CachePut("negotiation")
    @Modifying(clearAutomatically = true)
    @Transactional
    public AdminNegotiationDTO insertModelNegotiation(AdminNegotiationEntity adminNegotiationEntity) throws TspException {
        try {
            return adminNegotiationJpaRepository.insertModelNegotiation(adminNegotiationEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_MODEL_NEGOTIATION, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateModelNegotiation
     * 2. ClassName  : AdminNegotiationJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 섭외 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 09.
     * </pre>
     */
    @Override
    @CachePut("negotiation")
    @Modifying(clearAutomatically = true)
    @Transactional
    public AdminNegotiationDTO updateModelNegotiation(AdminNegotiationEntity adminNegotiationEntity) throws TspException {
        try {
            return adminNegotiationJpaRepository.updateModelNegotiation(adminNegotiationEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_UPDATE_MODEL_NEGOTIATION, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteModelNegotiation
     * 2. ClassName  : AdminNegotiationJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 섭외 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 09.
     * </pre>
     */
    @Override
    @CacheEvict("negotiation")
    @Modifying(clearAutomatically = true)
    @Transactional
    public Long deleteModelNegotiation(Long idx) throws TspException {
        try {
            return adminNegotiationJpaRepository.deleteModelNegotiation(idx);
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_MODEL_NEGOTIATION, e);
        }
    }
}
