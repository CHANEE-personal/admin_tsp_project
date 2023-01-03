package com.tsp.new_tsp_admin.api.production.service;

import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentDTO;
import com.tsp.new_tsp_admin.api.domain.production.AdminProductionDTO;
import com.tsp.new_tsp_admin.api.domain.production.AdminProductionEntity;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_admin.exception.ApiExceptionType.*;

@Service
@RequiredArgsConstructor
public class AdminProductionJpaServiceImpl implements AdminProductionJpaService {
    private final AdminProductionJpaRepository adminProductionJpaRepository;

    /**
     * <pre>
     * 1. MethodName : findProductionCount
     * 2. ClassName  : AdminProductionJpaServiceImpl.java
     * 3. Comment    : 관리자 프로덕션 리스트 수 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 09.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public int findProductionCount(Map<String, Object> productionMap) {
        return adminProductionJpaRepository.findProductionCount(productionMap);
    }

    /**
     * <pre>
     * 1. MethodName : findProductionList
     * 2. ClassName  : AdminProductionJpaServiceImpl.java
     * 3. Comment    : 관리자 프로덕션 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 09.
     * </pre>
     */
    @Override
    @Cacheable(value = "production", key = "#productionMap")
    @Transactional(readOnly = true)
    public List<AdminProductionDTO> findProductionList(Map<String, Object> productionMap) {
        return adminProductionJpaRepository.findProductionList(productionMap);
    }

    /**
     * <pre>
     * 1. MethodName : findOneProduction
     * 2. ClassName  : AdminProductionJpaServiceImpl.java
     * 3. Comment    : 관리자 프로덕션 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 15.
     * </pre>
     */
    @Override
    @Cacheable(value = "production", key = "#idx")
    @Transactional(readOnly = true)
    public AdminProductionDTO findOneProduction(Long idx) {
        return adminProductionJpaRepository.findOneProduction(idx);
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneProduction
     * 2. ClassName  : AdminProductionJpaServiceImpl.java
     * 3. Comment    : 관리자 이전 프로덕션 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 13.
     * </pre>
     */
    @Override
    @Cacheable(value = "production", key = "#idx")
    @Transactional(readOnly = true)
    public AdminProductionDTO findPrevOneProduction(Long idx) {
        return adminProductionJpaRepository.findPrevOneProduction(idx);
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneProduction
     * 2. ClassName  : AdminProductionJpaServiceImpl.java
     * 3. Comment    : 관리자 다음 프로덕션 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 13.
     * </pre>
     */
    @Override
    @Cacheable(value = "production", key = "#idx")
    @Transactional(readOnly = true)
    public AdminProductionDTO findNextOneProduction(Long idx) {
        return adminProductionJpaRepository.findNextOneProduction(idx);
    }

    /**
     * <pre>
     * 1. MethodName : insertProduction
     * 2. ClassName  : AdminProductionJpaServiceImpl.java
     * 3. Comment    : 관리자 프로덕션 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 16.
     * </pre>
     */
    @Override
    @CachePut("production")
    @Transactional
    public AdminProductionDTO insertProduction(AdminProductionEntity adminProductionEntity) {
        try {
            return adminProductionJpaRepository.insertProduction(adminProductionEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_PRODUCTION, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateProduction
     * 2. ClassName  : AdminProductionJpaServiceImpl.java
     * 3. Comment    : 관리자 프로덕션 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 16.
     * </pre>
     */
    @Override
    @CachePut(value = "production", key = "#adminProductionEntity.idx")
    @Transactional
    public AdminProductionDTO updateProduction(AdminProductionEntity adminProductionEntity) {
        try {
            return adminProductionJpaRepository.updateProductionByEm(adminProductionEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_UPDATE_MODEL, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteProduction
     * 2. ClassName  : AdminProductionJpaServiceImpl.java
     * 3. Comment    : 관리자 프로덕션 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 17.
     * </pre>
     */
    @Override
    @CacheEvict(value = "production", key = "#idx")
    @Transactional
    public Long deleteProduction(Long idx) {
        try {
            return adminProductionJpaRepository.deleteProductionByEm(idx);
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_PRODUCTION, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findProductionAdminComment
     * 2. ClassName  : AdminProductionJpaServiceImpl.java
     * 3. Comment    : 관리자 프로덕션 어드민 코멘트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 26.
     * </pre>
     */
    @Override
    @Cacheable(value = "comment", key = "#idx")
    @Transactional(readOnly = true)
    public List<AdminCommentDTO> findProductionAdminComment(Long idx) {
        try {
            return adminProductionJpaRepository.findProductionAdminComment(idx);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_COMMENT_LIST, e);
        }
    }
}
