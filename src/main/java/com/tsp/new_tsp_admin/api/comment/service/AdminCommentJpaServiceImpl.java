package com.tsp.new_tsp_admin.api.comment.service;

import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentDTO;
import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentEntity;
import com.tsp.new_tsp_admin.api.domain.faq.AdminFaqEntity;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioEntity;
import com.tsp.new_tsp_admin.api.domain.production.AdminProductionEntity;
import com.tsp.new_tsp_admin.api.model.service.AdminModelJpaRepository;
import com.tsp.new_tsp_admin.api.portfolio.service.AdminPortfolioJpaRepository;
import com.tsp.new_tsp_admin.api.production.service.AdminProductionJpaRepository;
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
public class AdminCommentJpaServiceImpl implements AdminCommentJpaService {
    private final AdminCommentJpaQueryRepository adminCommentJpaQueryRepository;
    private final AdminCommentJpaRepository adminCommentJpaRepository;
    private final AdminModelJpaRepository adminModelJpaRepository;
    private final AdminProductionJpaRepository adminProductionJpaRepository;
    private final AdminPortfolioJpaRepository adminPortfolioJpaRepository;

    private AdminModelEntity oneModel(Long idx) {
        return adminModelJpaRepository.findById(idx)
                .orElseThrow(() -> new TspException(NOT_FOUND_MODEL));
    }

    private AdminProductionEntity oneProduction(Long idx) {
        return adminProductionJpaRepository.findById(idx)
                .orElseThrow(() -> new TspException(NOT_FOUND_PRODUCTION));
    }

    private AdminPortFolioEntity onePortfolio(Long idx) {
        return adminPortfolioJpaRepository.findById(idx)
                .orElseThrow(() -> new TspException(NOT_FOUND_PORTFOLIO));
    }

    private AdminCommentEntity oneComment(Long idx) {
        return adminCommentJpaRepository.findById(idx)
                .orElseThrow(() -> new TspException(NOT_FOUND_COMMENT));
    }

    /**
     * <pre>
     * 1. MethodName : findAdminCommentCount
     * 2. ClassName  : AdminCommentJpaServiceImpl.java
     * 3. Comment    : 관리자 코멘트 리스트 수 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 24.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public int findAdminCommentCount(Map<String, Object> commentMap) {
        return adminCommentJpaQueryRepository.findAdminCommentCount(commentMap);
    }

    /**
     * <pre>
     * 1. MethodName : findAdminCommentList
     * 2. ClassName  : AdminCommentJpaServiceImpl.java
     * 3. Comment    : 관리자 코멘트 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 24.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public List<AdminCommentDTO> findAdminCommentList(Map<String, Object> commentMap) {
        return adminCommentJpaQueryRepository.findAdminCommentList(commentMap);
    }

    /**
     * <pre>
     * 1. MethodName : findOneAdminComment
     * 2. ClassName  : AdminCommentJpaServiceImpl.java
     * 3. Comment    : 관리자 코멘트 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 24.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public AdminCommentDTO findOneAdminComment(Long idx) {
        return AdminCommentEntity.toDto(oneComment(idx));
    }

    /**
     * <pre>
     * 1. MethodName : insertModelAdminComment
     * 2. ClassName  : AdminCommentJpaServiceImpl.java
     * 3. Comment    : 관리자 코멘트 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 24.
     * </pre>
     */
    @Override
    @Transactional
    public AdminCommentDTO insertModelAdminComment(Long idx, AdminCommentEntity adminCommentEntity) {
        try {
            oneModel(idx).addComment(adminCommentEntity);
            return AdminCommentEntity.toDto(adminCommentJpaRepository.save(adminCommentEntity));
        } catch (Exception e) {
            throw new TspException(ERROR_COMMENT);
        }
    }

    /**
     * <pre>
     * 1. MethodName : insertProductionAdminComment
     * 2. ClassName  : AdminCommentJpaServiceImpl.java
     * 3. Comment    : 관리자 프로덕션 코멘트 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 24.
     * </pre>
     */
    @Override
    @Transactional
    public AdminCommentDTO insertProductionAdminComment(Long idx, AdminCommentEntity adminCommentEntity) {
        try {
            oneProduction(idx).addComment(adminCommentEntity);
            return AdminCommentEntity.toDto(adminCommentJpaRepository.save(adminCommentEntity));
        } catch (Exception e) {
            throw new TspException(ERROR_COMMENT);
        }
    }

    /**
     * <pre>
     * 1. MethodName : insertPortfolioAdminComment
     * 2. ClassName  : AdminCommentJpaServiceImpl.java
     * 3. Comment    : 관리자 포트폴리오 코멘트 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 24.
     * </pre>
     */
    @Override
    @Transactional
    public AdminCommentDTO insertPortfolioAdminComment(Long idx, AdminCommentEntity adminCommentEntity) {
        try {
            onePortfolio(idx).addComment(adminCommentEntity);
            return AdminCommentEntity.toDto(adminCommentJpaRepository.save(adminCommentEntity));
        } catch (Exception e) {
            throw new TspException(ERROR_COMMENT);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateAdminComment
     * 2. ClassName  : AdminCommentJpaServiceImpl.java
     * 3. Comment    : 관리자 코멘트 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 24.
     * </pre>
     */
    @Override
    @Transactional
    public AdminCommentDTO updateAdminComment(Long idx, AdminCommentEntity adminCommentEntity) {
        try {
            oneComment(idx).update(adminCommentEntity);
            return AdminCommentEntity.toDto(adminCommentEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_UPDATE_COMMENT);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteAdminComment
     * 2. ClassName  : AdminCommentJpaServiceImpl.java
     * 3. Comment    : 관리자 코멘트 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 24.
     * </pre>
     */
    @Override
    @Transactional
    public Long deleteAdminComment(Long idx) {
        try {
            adminCommentJpaRepository.deleteById(idx);
            return idx;
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_COMMENT);
        }
    }
}
