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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
import static java.util.Collections.emptyList;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AdminSupportJpaQueryRepository {
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
     * 1. MethodName : findSupportList
     * 2. ClassName  : AdminSupportJpaRepository.java
     * 3. Comment    : 관리자 지원모델 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    public Page<AdminSupportDTO> findSupportList(Map<String, Object> supportMap, PageRequest pageRequest) {
        List<AdminSupportEntity> supportList = queryFactory.selectFrom(adminSupportEntity)
                .where(searchSupport(supportMap))
                .orderBy(adminSupportEntity.idx.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        return new PageImpl<>(AdminSupportEntity.toDtoList(supportList), pageRequest, supportList.size());
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
    public Page<EvaluationDTO> findEvaluationList(Map<String, Object> evaluationMap, PageRequest pageRequest) {
        List<EvaluationEntity> evaluationList = queryFactory.selectFrom(evaluationEntity)
                .where(evaluationEntity.visible.eq("Y"))
                .orderBy(adminSupportEntity.idx.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        return new PageImpl<>(EvaluationEntity.toDtoList(evaluationList), pageRequest, evaluationList.size());
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
//                        .and(QAdminCommentEntity.adminCommentEntity.commentTypeIdx.eq(idx))
                        .and(QAdminCommentEntity.adminCommentEntity.visible.eq("Y")))
                .fetch();

        return adminCommentEntity != null ? AdminCommentEntity.toDtoList(adminCommentEntity) : emptyList();
    }
}
