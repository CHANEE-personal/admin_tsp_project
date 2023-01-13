package com.tsp.new_tsp_admin.api.support.service;

import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentDTO;
import com.tsp.new_tsp_admin.api.domain.support.AdminSupportDTO;
import com.tsp.new_tsp_admin.api.domain.support.AdminSupportEntity;
import com.tsp.new_tsp_admin.api.domain.support.evaluation.EvaluationDTO;
import com.tsp.new_tsp_admin.api.domain.support.evaluation.EvaluationEntity;
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
public class AdminSupportJpaServiceImpl implements AdminSupportJpaService {
    private final AdminSupportJpaRepository adminSupportJpaRepository;

    /**
     * <pre>
     * 1. MethodName : findSupportCount
     * 2. ClassName  : AdminSupportJpaServiceImpl.java
     * 3. Comment    : 관리자 지원모델 리스트 갯수 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public int findSupportCount(Map<String, Object> supportMap) {
        return adminSupportJpaRepository.findSupportCount(supportMap);
    }

    /**
     * <pre>
     * 1. MethodName : findSupportList
     * 2. ClassName  : AdminSupportJpaServiceImpl.java
     * 3. Comment    : 관리자 지원모델 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @Override
    @Cacheable(value = "support", key = "#supportMap")
    @Transactional(readOnly = true)
    public List<AdminSupportDTO> findSupportList(Map<String, Object> supportMap) {
        return adminSupportJpaRepository.findSupportList(supportMap);
    }

    /**
     * <pre>
     * 1. MethodName : findOneSupportModel
     * 2. ClassName  : AdminSupportJpaServiceImpl.java
     * 3. Comment    : 관리자 지원모델 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @Override
    @Cacheable(value = "support", key = "#idx")
    @Transactional(readOnly = true)
    public AdminSupportDTO findOneSupportModel(Long idx) {
        return adminSupportJpaRepository.findOneSupportModel(idx);
    }

    @Override
    @CachePut(value = "support")
    @Transactional
    public AdminSupportDTO insertSupportModel(AdminSupportEntity adminSupportEntity) {
        try {
            return adminSupportJpaRepository.insertSupportModel(adminSupportEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_SUPPORT);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateSupportModel
     * 2. ClassName  : AdminSupportJpaServiceImpl.java
     * 3. Comment    : 관리자 지원모델 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @Override
    @CachePut(value = "support", key = "#adminSupportEntity.idx")
    @Transactional
    public AdminSupportDTO updateSupportModel(AdminSupportEntity adminSupportEntity) {
        try {
            return adminSupportJpaRepository.updateSupportModel(adminSupportEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_UPDATE_SUPPORT);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteSupportModel
     * 2. ClassName  : AdminSupportJpaServiceImpl.java
     * 3. Comment    : 관리자 지원모델 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @Override
    @CacheEvict(value = "support", key = "#idx")
    @Transactional
    public Long deleteSupportModel(Long idx) {
        try {
            return adminSupportJpaRepository.deleteSupportModel(idx);
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_SUPPORT);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findEvaluationCount
     * 2. ClassName  : AdminSupportJpaService.java
     * 3. Comment    : 관리자 지원모델 평가 리스트 수 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public int findEvaluationCount(Map<String, Object> evaluationMap) {
        return adminSupportJpaRepository.findEvaluationCount();
    }

    /**
     * <pre>
     * 1. MethodName : findEvaluationList
     * 2. ClassName  : AdminSupportJpaServiceImpl.java
     * 3. Comment    : 관리자 지원모델 평가 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @Override
    @Cacheable(value = "evaluation", key = "#evaluationMap")
    @Transactional(readOnly = true)
    public List<EvaluationDTO> findEvaluationList(Map<String, Object> evaluationMap) {
        return adminSupportJpaRepository.findEvaluationList(evaluationMap);
    }

    /**
     * <pre>
     * 1. MethodName : findOneEvaluation
     * 2. ClassName  : AdminSupportJpaServiceImpl.java
     * 3. Comment    : 관리자 지원모델 평가 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @Override
    @Cacheable(value = "evaluation", key = "#idx")
    @Transactional(readOnly = true)
    public EvaluationDTO findOneEvaluation(Long idx) {
        return adminSupportJpaRepository.findOneEvaluation(idx);
    }

    /**
     * <pre>
     * 1. MethodName : evaluationSupportModel
     * 2. ClassName  : AdminSupportJpaServiceImpl.java
     * 3. Comment    : 관리자 지원모델 평가 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @Override
    @CachePut("evaluation")
    @Transactional
    public EvaluationDTO evaluationSupportModel(EvaluationEntity evaluationEntity) {
        try {
            return adminSupportJpaRepository.evaluationSupportModel(evaluationEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_EVALUATION);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateEvaluation
     * 2. ClassName  : AdminSupportJpaServiceImpl.java
     * 3. Comment    : 관리자 지원모델 평가 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @Override
    @CachePut(value = "evaluation", key = "#evaluationEntity.idx")
    @Transactional
    public EvaluationDTO updateEvaluation(EvaluationEntity evaluationEntity) {
        try {
            return adminSupportJpaRepository.updateEvaluation(evaluationEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_UPDATE_EVALUATION);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteEvaluation
     * 2. ClassName  : AdminSupportJpaServiceImpl.java
     * 3. Comment    : 관리자 지원모델 평가 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @Override
    @CacheEvict(value = "evaluation", key = "#idx")
    @Transactional
    public Long deleteEvaluation(Long idx) {
        try {
            return adminSupportJpaRepository.deleteEvaluation(idx);
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_EVALUATION);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updatePass
     * 2. ClassName  : AdminSupportJpaServiceImpl.java
     * 3. Comment    : 관리자 지원모델 합격 처리
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    @Override
    @CachePut(value = "support", key = "#idx")
    @Transactional
    public AdminSupportDTO updatePass(Long idx) {
        try {
            return adminSupportJpaRepository.updatePass(idx);
        } catch (Exception e) {
            throw new TspException(ERROR_UPDATE_SUPPORT);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findSupportAdminComment
     * 2. ClassName  : AdminSupportJpaServiceImpl.java
     * 3. Comment    : 관리자 지원모델 어드민 코멘트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 26.
     * </pre>
     */
    @Override
    @Cacheable(value = "comment", key = "#idx")
    public List<AdminCommentDTO> findSupportAdminComment(Long idx) {
        return adminSupportJpaRepository.findSupportAdminComment(idx);
    }
}
