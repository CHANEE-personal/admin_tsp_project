package com.tsp.new_tsp_admin.api.comment.service;

import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentDTO;
import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentEntity;
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
public class AdminCommentJpaServiceImpl implements AdminCommentJpaService {
    private final AdminCommentJpaRepository adminCommentJpaRepository;

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
        return adminCommentJpaRepository.findAdminCommentCount(commentMap);
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
    @Cacheable(value = "comment", key = "#commentMap")
    @Transactional(readOnly = true)
    public List<AdminCommentDTO> findAdminCommentList(Map<String, Object> commentMap) {
        return adminCommentJpaRepository.findAdminCommentList(commentMap);
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
    @Cacheable(value = "comment", key = "#idx")
    @Transactional(readOnly = true)
    public AdminCommentDTO findOneAdminComment(Long idx) {
        return adminCommentJpaRepository.findOneAdminComment(idx);
    }

    /**
     * <pre>
     * 1. MethodName : insertFaq
     * 2. ClassName  : AdminCommentJpaServiceImpl.java
     * 3. Comment    : 관리자 코멘트 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 24.
     * </pre>
     */
    @Override
    @CachePut("comment")
    @Transactional
    public AdminCommentDTO insertAdminComment(AdminCommentEntity adminCommentEntity) {
        try {
            return adminCommentJpaRepository.insertAdminComment(adminCommentEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_COMMENT, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateAdminComment
     * 2. ClassName  : AdminCommentJpaServiceImpl.java
     * 3. Comment    : 관리자 코멘트 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 24.
     * </pre>
     */
    @Override
    @CachePut(value = "comment", key = "#adminCommentEntity.idx")
    @Transactional
    public AdminCommentDTO updateAdminComment(AdminCommentEntity adminCommentEntity) {
        try {
            return adminCommentJpaRepository.updateAdminComment(adminCommentEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_UPDATE_COMMENT, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteAdminComment
     * 2. ClassName  : AdminCommentJpaServiceImpl.java
     * 3. Comment    : 관리자 코멘트 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 24.
     * </pre>
     */
    @Override
    @CacheEvict(value = "comment", key = "#idx")
    @Transactional
    public Long deleteAdminComment(Long idx) {
        try {
            return adminCommentJpaRepository.deleteAdminComment(idx);
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_COMMENT, e);
        }
    }
}
