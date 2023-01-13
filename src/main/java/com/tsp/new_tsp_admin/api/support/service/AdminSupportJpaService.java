package com.tsp.new_tsp_admin.api.support.service;

import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentDTO;
import com.tsp.new_tsp_admin.api.domain.support.AdminSupportDTO;
import com.tsp.new_tsp_admin.api.domain.support.AdminSupportEntity;
import com.tsp.new_tsp_admin.api.domain.support.evaluation.EvaluationDTO;
import com.tsp.new_tsp_admin.api.domain.support.evaluation.EvaluationEntity;

import java.util.List;
import java.util.Map;

public interface AdminSupportJpaService {

    /**
     * <pre>
     * 1. MethodName : findSupportCount
     * 2. ClassName  : AdminSupportJpaService.java
     * 3. Comment    : 관리자 지원모델 리스트 수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    int findSupportCount(Map<String, Object> supportMap);

    /**
     * <pre>
     * 1. MethodName : findSupportList
     * 2. ClassName  : AdminSupportJpaService.java
     * 3. Comment    : 관리자 지원모델 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    List<AdminSupportDTO> findSupportList(Map<String, Object> supportMap);

    /**
     * <pre>
     * 1. MethodName : findOneSupportModel
     * 2. ClassName  : AdminSupportJpaService.java
     * 3. Comment    : 관리자 지원모델 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    AdminSupportDTO findOneSupportModel(Long idx);

    /**
     * <pre>
     * 1. MethodName : insertSupportModel
     * 2. ClassName  : AdminSupportJpaService.java
     * 3. Comment    : 관리자 지원모델 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    AdminSupportDTO insertSupportModel(AdminSupportEntity adminSupportEntity);

    /**
     * <pre>
     * 1. MethodName : updateSupportModel
     * 2. ClassName  : AdminSupportJpaService.java
     * 3. Comment    : 관리자 지원모델 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    AdminSupportDTO updateSupportModel(Long idx, AdminSupportEntity adminSupportEntity);

    /**
     * <pre>
     * 1. MethodName : deleteSupportModel
     * 2. ClassName  : AdminSupportJpaService.java
     * 3. Comment    : 관리자 지원모델 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    Long deleteSupportModel(Long idx);

    /**
     * <pre>
     * 1. MethodName : findEvaluationCount
     * 2. ClassName  : AdminSupportJpaService.java
     * 3. Comment    : 관리자 지원모델 평가 리스트 수 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    int findEvaluationCount(Map<String, Object> evaluationMap);

    /**
     * <pre>
     * 1. MethodName : findEvaluationList
     * 2. ClassName  : AdminSupportJpaService.java
     * 3. Comment    : 관리자 지원모델 평가 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    List<EvaluationDTO> findEvaluationList(Map<String, Object> evaluationMap);

    /**
     * <pre>
     * 1. MethodName : findOneEvaluation
     * 2. ClassName  : AdminSupportJpaService.java
     * 3. Comment    : 관리자 지원모델 평가 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    EvaluationDTO findOneEvaluation(Long idx);

    /**
     * <pre>
     * 1. MethodName : evaluationSupportModel
     * 2. ClassName  : AdminSupportJpaService.java
     * 3. Comment    : 관리자 지원모델 평가 작성
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    EvaluationDTO evaluationSupportModel(Long idx, EvaluationEntity evaluationEntity);

    /**
     * <pre>
     * 1. MethodName : updateEvaluation
     * 2. ClassName  : AdminSupportJpaService.java
     * 3. Comment    : 관리자 지원모델 평가 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    EvaluationDTO updateEvaluation(Long idx, EvaluationEntity evaluationEntity);

    /**
     * <pre>
     * 1. MethodName : deleteEvaluation
     * 2. ClassName  : AdminSupportJpaService.java
     * 3. Comment    : 관리자 지원모델 평가 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    Long deleteEvaluation(Long idx);

    /**
     * <pre>
     * 1. MethodName : updatePass
     * 2. ClassName  : AdminSupportJpaService.java
     * 3. Comment    : 관리자 지원모델 합격 처리
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    AdminSupportDTO updatePass(Long idx);

    /**
     * <pre>
     * 1. MethodName : findSupportAdminComment
     * 2. ClassName  : AdminSupportJpaService.java
     * 3. Comment    : 관리자 지원모델 어드민 코멘트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 26.
     * </pre>
     */
    List<AdminCommentDTO> findSupportAdminComment(Long idx);
}
