package com.tsp.new_tsp_admin.api.support.service;

import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentDTO;
import com.tsp.new_tsp_admin.api.domain.production.AdminProductionEntity;
import com.tsp.new_tsp_admin.api.domain.support.AdminSupportDTO;
import com.tsp.new_tsp_admin.api.domain.support.AdminSupportEntity;
import com.tsp.new_tsp_admin.api.domain.support.evaluation.EvaluationDTO;
import com.tsp.new_tsp_admin.api.domain.support.evaluation.EvaluationEntity;
import com.tsp.new_tsp_admin.api.support.evaluation.AdminEvaluationJpaRepository;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_admin.exception.ApiExceptionType.*;

@Service
@RequiredArgsConstructor
public class AdminSupportJpaServiceImpl implements AdminSupportJpaService {
    private final AdminSupportJpaQueryRepository adminSupportJpaQueryRepository;
    private final AdminSupportJpaRepository adminSupportJpaRepository;
    private final AdminEvaluationJpaRepository adminEvaluationJpaRepository;

    private AdminSupportEntity oneSupport(Long idx) {
        return adminSupportJpaRepository.findById(idx)
                .orElseThrow(() -> new TspException(NOT_FOUND_SUPPORT));
    }

    private EvaluationEntity oneEvaluation(Long idx) {
        return adminEvaluationJpaRepository.findById(idx)
                .orElseThrow(() -> new TspException(NOT_FOUND_EVALUATION));
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
    @Transactional(readOnly = true)
    public Page<AdminSupportDTO> findSupportList(Map<String, Object> supportMap, PageRequest pageRequest) {
        return adminSupportJpaQueryRepository.findSupportList(supportMap, pageRequest);
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
    @Transactional(readOnly = true)
    public AdminSupportDTO findOneSupportModel(Long idx) {
        return AdminSupportEntity.toDto(oneSupport(idx));
    }

    @Override
    @Transactional
    public AdminSupportDTO insertSupportModel(AdminSupportEntity adminSupportEntity) {
        try {
            return AdminSupportEntity.toDto(adminSupportJpaRepository.save(adminSupportEntity));
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
    @Transactional
    public AdminSupportDTO updateSupportModel(Long idx, AdminSupportEntity adminSupportEntity) {
        try {
            oneSupport(idx).update(adminSupportEntity);
            return AdminSupportEntity.toDto(adminSupportEntity);
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
    @Transactional
    public Long deleteSupportModel(Long idx) {
        try {
            adminSupportJpaRepository.deleteById(idx);
            return idx;
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_SUPPORT);
        }
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
    @Transactional(readOnly = true)
    public Page<EvaluationDTO> findEvaluationList(Map<String, Object> evaluationMap, PageRequest pageRequest) {
        return adminSupportJpaQueryRepository.findEvaluationList(evaluationMap, pageRequest);
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
    @Transactional(readOnly = true)
    public EvaluationDTO findOneEvaluation(Long idx) {
        return EvaluationEntity.toDto(oneEvaluation(idx));
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
    @Transactional
    public EvaluationDTO evaluationSupportModel(Long idx, EvaluationEntity evaluationEntity) {
        try {
            oneSupport(idx).addSupport(evaluationEntity);
            return adminSupportJpaQueryRepository.evaluationSupportModel(evaluationEntity);
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
    @Transactional
    public EvaluationDTO updateEvaluation(Long idx, EvaluationEntity evaluationEntity) {
        try {
            oneEvaluation(idx).update(evaluationEntity);
            return EvaluationEntity.toDto(evaluationEntity);
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
    @Transactional
    public Long deleteEvaluation(Long idx) {
        try {
            adminEvaluationJpaRepository.deleteById(idx);
            return idx;
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
    @Transactional
    public AdminSupportDTO updatePass(Long idx) {
        try {
            AdminSupportEntity oneSupport = oneSupport(idx);
            oneSupport.togglePassYn(oneSupport.getPassYn());
            return AdminSupportEntity.toDto(oneSupport);
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
    @Transactional(readOnly = true)
    public List<AdminCommentDTO> findSupportAdminComment(Long idx) {
        return adminSupportJpaQueryRepository.findSupportAdminComment(idx);
    }
}
