package com.tsp.new_tsp_admin.api.support.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.comment.mapper.AdminCommentMapper;
import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentDTO;
import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentEntity;
import com.tsp.new_tsp_admin.api.domain.comment.QAdminCommentEntity;
import com.tsp.new_tsp_admin.api.domain.support.AdminSupportDTO;
import com.tsp.new_tsp_admin.api.domain.support.AdminSupportEntity;
import com.tsp.new_tsp_admin.api.domain.support.evaluation.EvaluationDTO;
import com.tsp.new_tsp_admin.api.domain.support.evaluation.EvaluationEntity;
import com.tsp.new_tsp_admin.api.support.mapper.evaluate.EvaluateMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_admin.api.domain.support.QAdminSupportEntity.adminSupportEntity;
import static com.tsp.new_tsp_admin.api.domain.support.evaluation.QEvaluationEntity.evaluationEntity;
import static com.tsp.new_tsp_admin.api.support.mapper.SupportMapper.INSTANCE;
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
     * 1. MethodName : findSupportsCount
     * 2. ClassName  : AdminSupportJpaRepository.java
     * 3. Comment    : 관리자 지원모델 리스트 갯수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    public Integer findSupportsCount(Map<String, Object> supportMap) {
        return queryFactory.selectFrom(adminSupportEntity).where(searchSupport(supportMap)).fetch().size();
    }

    /**
     * <pre>
     * 1. MethodName : findSupportsList
     * 2. ClassName  : AdminSupportJpaRepository.java
     * 3. Comment    : 관리자 지원모델 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    public List<AdminSupportDTO> findSupportsList(Map<String, Object> supportMap) {
        List<AdminSupportEntity> supportList = queryFactory.selectFrom(adminSupportEntity)
                .where(searchSupport(supportMap))
                .orderBy(adminSupportEntity.idx.desc())
                .offset(getInt(supportMap.get("jpaStartPage"), 0))
                .limit(getInt(supportMap.get("size"), 0))
                .fetch();

        supportList.forEach(list -> supportList.get(supportList.indexOf(list))
                .setRnum(getInt(supportMap.get("startPage"), 1) * (getInt(supportMap.get("size"), 1)) - (2 - supportList.indexOf(list))));

        return INSTANCE.toDtoList(supportList);
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
    public AdminSupportDTO findOneSupportModel(AdminSupportEntity existAdminSupportEntity) {
        //모델 상세 조회
        AdminSupportEntity findOneSupportModel = queryFactory.selectFrom(adminSupportEntity)
                .where(adminSupportEntity.idx.eq(existAdminSupportEntity.getIdx()))
                .fetchOne();

        return INSTANCE.toDto(findOneSupportModel);
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
        return INSTANCE.toDto(adminSupportEntity);
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
        return INSTANCE.toDto(existAdminSupportEntity);
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
    public Integer deleteSupportModel(Integer idx) {
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
     * 1. MethodName : findEvaluationsList
     * 2. ClassName  : AdminSupportJpaRepository.java
     * 3. Comment    : 관리자 지원모델 평가내용 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    public List<EvaluationDTO> findEvaluationsList(Map<String, Object> evaluationMap) {
        List<EvaluationEntity> evaluationList = queryFactory.selectFrom(evaluationEntity)
                .where(evaluationEntity.visible.eq("Y"))
                .orderBy(adminSupportEntity.idx.desc())
                .offset(getInt(evaluationMap.get("jpaStartPage"), 0))
                .limit(getInt(evaluationMap.get("size"), 0))
                .fetch();

        evaluationList.forEach(list -> evaluationList.get(evaluationList.indexOf(list))
                .setRnum(getInt(evaluationMap.get("startPage"), 1) * (getInt(evaluationMap.get("size"), 1)) - (2 - evaluationList.indexOf(list))));

        return EvaluateMapper.INSTANCE.toDtoList(evaluationList);
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
    public EvaluationDTO findOneEvaluation(EvaluationEntity existEvaluationEntity) {
        //모델 상세 조회
        EvaluationEntity findOneEvaluation = queryFactory.selectFrom(evaluationEntity)
                .where(evaluationEntity.idx.eq(existEvaluationEntity.getIdx()))
                .fetchOne();

        return EvaluateMapper.INSTANCE.toDto(findOneEvaluation);
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
        return EvaluateMapper.INSTANCE.toDto(evaluationEntity);
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
        return EvaluateMapper.INSTANCE.toDto(existEvaluationEntity);
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
    public Integer deleteEvaluation(Integer idx) {
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
    public AdminSupportDTO updatePass(AdminSupportEntity existSupportEntity) {
        queryFactory.update(adminSupportEntity)
                .set(adminSupportEntity.passYn, existSupportEntity.getPassYn())
                .set(adminSupportEntity.passTime, existSupportEntity.getPassTime())
                .where(adminSupportEntity.idx.eq(existSupportEntity.getIdx()))
                .execute();

        em.flush();
        em.clear();

        return INSTANCE.toDto(existSupportEntity);
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
    public List<AdminCommentDTO> findSupportAdminComment(AdminSupportEntity existAdminSupportEntity) {
        List<AdminCommentEntity> adminCommentEntity = queryFactory
                .selectFrom(QAdminCommentEntity.adminCommentEntity)
                .where(QAdminCommentEntity.adminCommentEntity.commentType.eq("support")
                        .and(QAdminCommentEntity.adminCommentEntity.commentTypeIdx.eq(existAdminSupportEntity.getIdx()))
                        .and(QAdminCommentEntity.adminCommentEntity.visible.eq("Y")))
                .fetch();

        return AdminCommentMapper.INSTANCE.toDtoList(adminCommentEntity);
    }
}
