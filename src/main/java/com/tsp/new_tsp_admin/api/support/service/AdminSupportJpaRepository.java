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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_admin.api.domain.support.QAdminSupportEntity.adminSupportEntity;
import static com.tsp.new_tsp_admin.api.domain.support.evaluation.QEvaluationEntity.evaluationEntity;
import static com.tsp.new_tsp_admin.common.StringUtil.getInt;
import static com.tsp.new_tsp_admin.common.StringUtil.getString;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AdminSupportJpaRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private BooleanExpression searchSupport(Map<String, Object> supportMap) {
        String searchType = getString(supportMap.get("searchType"), "");
        String searchKeyword = getString(supportMap.get("searchKeyword"), "");

        if ("0".equals(searchType)) {
            return adminSupportEntity.supportName.contains(searchKeyword)
                    .or(adminSupportEntity.supportMessage.contains(searchKeyword));
        } else if ("1".equals(searchType)) {
            return adminSupportEntity.supportName.contains(searchKeyword);
        } else {
            return adminSupportEntity.supportMessage.contains(searchKeyword);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findSupportCount
     * 2. ClassName  : AdminSupportJpaRepository.java
     * 3. Comment    : 관리자 지원모델 리스트 갯수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    public Integer findSupportCount(Map<String, Object> supportMap) {
        return queryFactory.selectFrom(adminSupportEntity).where(searchSupport(supportMap)).fetch().size();
    }

    /**
     * <pre>
     * 1. MethodName : findSupportList
     * 2. ClassName  : AdminSupportJpaRepository.java
     * 3. Comment    : 관리자 지원모델 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    public List<AdminSupportDTO> findSupportList(Map<String, Object> supportMap) {
        List<AdminSupportEntity> supportList = queryFactory.selectFrom(adminSupportEntity)
                .where(searchSupport(supportMap))
                .orderBy(adminSupportEntity.idx.desc())
                .offset(getInt(supportMap.get("jpaStartPage"), 0))
                .limit(getInt(supportMap.get("size"), 0))
                .fetch();

        supportList.forEach(list -> supportList.get(supportList.indexOf(list))
                .setRowNum(getInt(supportMap.get("startPage"), 1) * (getInt(supportMap.get("size"), 1)) - (2 - supportList.indexOf(list))));

        return AdminSupportEntity.toDtoList(supportList);
    }

    /**
     * <pre>
     * 1. MethodName : findOneSupportModel
     * 2. ClassName  : AdminSupportJpaRepository.java
     * 3. Comment    : 관리자 지원모델 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    public AdminSupportDTO findOneSupportModel(Long idx) {
        //모델 상세 조회
        AdminSupportEntity findOneSupportModel = queryFactory.selectFrom(adminSupportEntity)
                .where(adminSupportEntity.idx.eq(idx))
                .fetchOne();

        assert findOneSupportModel != null;
        return AdminSupportEntity.toDto(findOneSupportModel);
    }

    /**
     * <pre>
     * 1. MethodName : insertSupportModel
     * 2. ClassName  : AdminSupportJpaRepository.java
     * 3. Comment    : 관리자 지원모델 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    public AdminSupportDTO insertSupportModel(AdminSupportEntity adminSupportEntity) {
        em.persist(adminSupportEntity);
        return AdminSupportEntity.toDto(adminSupportEntity);
    }

    /**
     * <pre>
     * 1. MethodName : updateSupportModel
     * 2. ClassName  : AdminSupportJpaRepository.java
     * 3. Comment    : 관리자 지원모델 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    public AdminSupportDTO updateSupportModel(AdminSupportEntity existAdminSupportEntity) {
        em.merge(existAdminSupportEntity);
        em.flush();
        em.clear();
        return AdminSupportEntity.toDto(existAdminSupportEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deleteSupportModel
     * 2. ClassName  : AdminSupportJpaRepository.java
     * 3. Comment    : 관리자 지원모델 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    public Long deleteSupportModel(Long idx) {
        em.remove(em.find(AdminSupportEntity.class, idx));
        em.flush();
        em.clear();
        return idx;
    }

    /**
     * <pre>
     * 1. MethodName : findEvaluationsCount
     * 2. ClassName  : AdminSupportJpaRepository.java
     * 3. Comment    : 관리자 지원모델 평가 리스트 갯수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    public Integer findEvaluationsCount() {
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

        evaluationList.forEach(list -> evaluationList.get(evaluationList.indexOf(list))
                .setRowNum(getInt(evaluationMap.get("startPage"), 1) * (getInt(evaluationMap.get("size"), 1)) - (2 - evaluationList.indexOf(list))));

        return EvaluationEntity.toDtoList(evaluationList);
    }

    /**
     * <pre>
     * 1. MethodName : findOneEvaluation
     * 2. ClassName  : AdminSupportJpaRepository.java
     * 3. Comment    : 관리자 지원모델 평가내용 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    public EvaluationDTO findOneEvaluation(Long idx) {
        //모델 상세 조회
        EvaluationEntity findOneEvaluation = queryFactory.selectFrom(evaluationEntity)
                .where(evaluationEntity.idx.eq(idx))
                .fetchOne();

        assert findOneEvaluation != null;
        return EvaluationEntity.toDto(findOneEvaluation);
    }

    /**
     * <pre>
     * 1. MethodName : evaluationSupportModel
     * 2. ClassName  : AdminSupportJpaRepository.java
     * 3. Comment    : 관리자 지원모델 평가내용 작성
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    public EvaluationDTO evaluationSupportModel(EvaluationEntity evaluationEntity) {
        em.persist(evaluationEntity);
        return EvaluationEntity.toDto(evaluationEntity);
    }

    /**
     * <pre>
     * 1. MethodName : updateEvaluation
     * 2. ClassName  : AdminSupportJpaRepository.java
     * 3. Comment    : 관리자 지원모델 평가내용 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    public EvaluationDTO updateEvaluation(EvaluationEntity existEvaluationEntity) {
        em.merge(existEvaluationEntity);
        em.flush();
        em.clear();
        return EvaluationEntity.toDto(existEvaluationEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deleteEvaluation
     * 2. ClassName  : AdminSupportJpaRepository.java
     * 3. Comment    : 관리자 지원모델 평가내용 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    public Long deleteEvaluation(Long idx) {
        em.remove(em.find(EvaluationEntity.class, idx));
        em.flush();
        em.clear();
        return idx;
    }

    /**
     * <pre>
     * 1. MethodName : updatePass
     * 2. ClassName  : AdminSupportJpaRepository.java
     * 3. Comment    : 관리자 지원모델 합격 처리
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
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

        return AdminSupportEntity.toDto(em.find(AdminSupportEntity.class, idx));
    }

    /**
     * <pre>
     * 1. MethodName : findSupportAdminComment
     * 2. ClassName  : AdminSupportJpaRepository.java
     * 3. Comment    : 관리자 지원모델 어드민 코멘트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 26.
     * </pre>
     */
    public List<AdminCommentDTO> findSupportAdminComment(Long idx) {
        List<AdminCommentEntity> adminCommentEntity = queryFactory
                .selectFrom(QAdminCommentEntity.adminCommentEntity)
                .where(QAdminCommentEntity.adminCommentEntity.commentType.eq("support")
                        .and(QAdminCommentEntity.adminCommentEntity.commentTypeIdx.eq(idx))
                        .and(QAdminCommentEntity.adminCommentEntity.visible.eq("Y")))
                .fetch();

        return AdminCommentEntity.toDtoList(adminCommentEntity);
    }
}
