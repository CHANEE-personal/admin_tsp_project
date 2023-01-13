package com.tsp.new_tsp_admin.api.support.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentDTO;
import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentEntity;
import com.tsp.new_tsp_admin.api.domain.comment.QAdminCommentEntity;
import com.tsp.new_tsp_admin.api.domain.support.AdminSupportDTO;
import com.tsp.new_tsp_admin.api.domain.support.AdminSupportEntity;
import com.tsp.new_tsp_admin.api.domain.support.evaluation.EvaluationDTO;
import com.tsp.new_tsp_admin.api.domain.support.evaluation.EvaluationEntity;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.*;

import static com.tsp.new_tsp_admin.api.domain.support.AdminSupportEntity.toDto;
import static com.tsp.new_tsp_admin.api.domain.support.AdminSupportEntity.toDtoList;
import static com.tsp.new_tsp_admin.api.domain.support.QAdminSupportEntity.adminSupportEntity;
import static com.tsp.new_tsp_admin.api.domain.support.evaluation.EvaluationEntity.toDto;
import static com.tsp.new_tsp_admin.api.domain.support.evaluation.EvaluationEntity.toDtoList;
import static com.tsp.new_tsp_admin.api.domain.support.evaluation.QEvaluationEntity.evaluationEntity;
import static com.tsp.new_tsp_admin.common.StringUtil.getInt;
import static com.tsp.new_tsp_admin.common.StringUtil.getString;
import static com.tsp.new_tsp_admin.exception.ApiExceptionType.NOT_FOUND_EVALUATION;
import static com.tsp.new_tsp_admin.exception.ApiExceptionType.NOT_FOUND_SUPPORT;
import static java.util.Collections.emptyList;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AdminSupportJpaRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private BooleanExpression searchSupport(Map<String, Object> supportMap) {
        String searchType = getString(supportMap.get("searchType"), "");
        String searchKeyword = getString(supportMap.get("searchKeyword"), "");

        if (!Objects.equals(searchKeyword, "")) {
            return "0".equals(searchType) ?
                    adminSupportEntity.supportName.contains(searchKeyword)
                            .or(adminSupportEntity.supportMessage.contains(searchKeyword)) :
                    "1".equals(searchType) ?
                            adminSupportEntity.supportName.contains(searchKeyword) :
                            adminSupportEntity.supportMessage.contains(searchKeyword);
        } else {
            return null;
        }
    }

    /**
     * <pre>
     * 1. MethodName : findSupportCount
     * 2. ClassName  : AdminSupportJpaRepository.java
     * 3. Comment    : 관리자 지원모델 리스트 갯수 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    public int findSupportCount(Map<String, Object> supportMap) {
        return queryFactory.selectFrom(adminSupportEntity).where(searchSupport(supportMap)).fetch().size();
    }

    /**
     * <pre>
     * 1. MethodName : findSupportList
     * 2. ClassName  : AdminSupportJpaRepository.java
     * 3. Comment    : 관리자 지원모델 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    public List<AdminSupportDTO> findSupportList(Map<String, Object> supportMap) {
        List<AdminSupportEntity> supportList = queryFactory.selectFrom(adminSupportEntity)
                .where(searchSupport(supportMap))
                .orderBy(adminSupportEntity.idx.desc())
                .offset(getInt(supportMap.get("jpaStartPage"), 0))
                .limit(getInt(supportMap.get("size"), 0))
                .fetch();

        return supportList != null ? toDtoList(supportList) : emptyList();
    }

    /**
     * <pre>
     * 1. MethodName : findOneSupportModel
     * 2. ClassName  : AdminSupportJpaRepository.java
     * 3. Comment    : 관리자 지원모델 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    public AdminSupportDTO findOneSupportModel(Long idx) {
        //모델 상세 조회
        AdminSupportEntity findOneSupportModel = Optional.ofNullable(queryFactory
                .selectFrom(adminSupportEntity)
                .where(adminSupportEntity.idx.eq(idx))
                .fetchOne()).orElseThrow(() -> new TspException(NOT_FOUND_SUPPORT));

        return toDto(findOneSupportModel);
    }

    /**
     * <pre>
     * 1. MethodName : insertSupportModel
     * 2. ClassName  : AdminSupportJpaRepository.java
     * 3. Comment    : 관리자 지원모델 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    public AdminSupportDTO insertSupportModel(AdminSupportEntity adminSupportEntity) {
        em.persist(adminSupportEntity);
        return toDto(adminSupportEntity);
    }

    /**
     * <pre>
     * 1. MethodName : updateSupportModel
     * 2. ClassName  : AdminSupportJpaRepository.java
     * 3. Comment    : 관리자 지원모델 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    public AdminSupportDTO updateSupportModel(AdminSupportEntity existAdminSupportEntity) {
        em.merge(existAdminSupportEntity);
        return toDto(existAdminSupportEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deleteSupportModel
     * 2. ClassName  : AdminSupportJpaRepository.java
     * 3. Comment    : 관리자 지원모델 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    public Long deleteSupportModel(Long idx) {
        em.remove(em.find(AdminSupportEntity.class, idx));
        return idx;
    }

    /**
     * <pre>
     * 1. MethodName : findEvaluationCount
     * 2. ClassName  : AdminSupportJpaRepository.java
     * 3. Comment    : 관리자 지원모델 평가 리스트 갯수 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    public int findEvaluationCount() {
        return queryFactory.selectFrom(evaluationEntity).fetch().size();
    }

    /**
     * <pre>
     * 1. MethodName : findEvaluationList
     * 2. ClassName  : AdminSupportJpaRepository.java
     * 3. Comment    : 관리자 지원모델 평가내용 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    public List<EvaluationDTO> findEvaluationList(Map<String, Object> evaluationMap) {
        List<EvaluationEntity> evaluationList = queryFactory.selectFrom(evaluationEntity)
                .where(evaluationEntity.visible.eq("Y"))
                .orderBy(adminSupportEntity.idx.desc())
                .offset(getInt(evaluationMap.get("jpaStartPage"), 0))
                .limit(getInt(evaluationMap.get("size"), 0))
                .fetch();

        return evaluationList != null ? toDtoList(evaluationList) : emptyList();
    }

    /**
     * <pre>
     * 1. MethodName : findOneEvaluation
     * 2. ClassName  : AdminSupportJpaRepository.java
     * 3. Comment    : 관리자 지원모델 평가내용 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    public EvaluationDTO findOneEvaluation(Long idx) {
        //모델 상세 조회
        EvaluationEntity findOneEvaluation = Optional.ofNullable(queryFactory
                .selectFrom(evaluationEntity)
                .where(evaluationEntity.idx.eq(idx))
                .fetchOne()).orElseThrow(() -> new TspException(NOT_FOUND_EVALUATION));

        return toDto(findOneEvaluation);
    }

    /**
     * <pre>
     * 1. MethodName : evaluationSupportModel
     * 2. ClassName  : AdminSupportJpaRepository.java
     * 3. Comment    : 관리자 지원모델 평가내용 작성
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    public EvaluationDTO evaluationSupportModel(EvaluationEntity evaluationEntity) {
        em.persist(evaluationEntity);
        return toDto(evaluationEntity);
    }

    /**
     * <pre>
     * 1. MethodName : updateEvaluation
     * 2. ClassName  : AdminSupportJpaRepository.java
     * 3. Comment    : 관리자 지원모델 평가내용 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    public EvaluationDTO updateEvaluation(EvaluationEntity existEvaluationEntity) {
        em.merge(existEvaluationEntity);
        return toDto(existEvaluationEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deleteEvaluation
     * 2. ClassName  : AdminSupportJpaRepository.java
     * 3. Comment    : 관리자 지원모델 평가내용 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    public Long deleteEvaluation(Long idx) {
        em.remove(em.find(EvaluationEntity.class, idx));
        return idx;
    }

    /**
     * <pre>
     * 1. MethodName : updatePass
     * 2. ClassName  : AdminSupportJpaRepository.java
     * 3. Comment    : 관리자 지원모델 합격 처리
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    public AdminSupportDTO updatePass(Long idx) {
        queryFactory.update(adminSupportEntity)
                .set(adminSupportEntity.passYn, "Y")
                .set(adminSupportEntity.passTime, LocalDateTime.now())
                .where(adminSupportEntity.idx.eq(idx))
                .execute();

        em.flush();
        em.clear();

        return toDto(em.find(AdminSupportEntity.class, idx));
    }

    /**
     * <pre>
     * 1. MethodName : findSupportAdminComment
     * 2. ClassName  : AdminSupportJpaRepository.java
     * 3. Comment    : 관리자 지원모델 어드민 코멘트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 26.
     * </pre>
     */
    public List<AdminCommentDTO> findSupportAdminComment(Long idx) {
        List<AdminCommentEntity> adminCommentEntity = queryFactory
                .selectFrom(QAdminCommentEntity.adminCommentEntity)
                .where(QAdminCommentEntity.adminCommentEntity.commentType.eq("support")
                        .and(QAdminCommentEntity.adminCommentEntity.commentTypeIdx.eq(idx))
                        .and(QAdminCommentEntity.adminCommentEntity.visible.eq("Y")))
                .fetch();

        return adminCommentEntity != null ? AdminCommentEntity.toDtoList(adminCommentEntity) : emptyList();
    }
}
