package com.tsp.new_tsp_admin.api.model.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentDTO;
import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentEntity;
import com.tsp.new_tsp_admin.api.domain.comment.QAdminCommentEntity;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelDTO;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import com.tsp.new_tsp_admin.api.domain.model.recommend.AdminRecommendDTO;
import com.tsp.new_tsp_admin.api.domain.model.recommend.AdminRecommendEntity;
import com.tsp.new_tsp_admin.api.domain.model.recommend.QAdminRecommendEntity;
import com.tsp.new_tsp_admin.api.domain.model.schedule.AdminScheduleDTO;
import com.tsp.new_tsp_admin.api.domain.model.schedule.AdminScheduleEntity;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.*;

import static com.tsp.new_tsp_admin.api.domain.comment.AdminCommentEntity.toDtoList;
import static com.tsp.new_tsp_admin.api.domain.common.QCommonImageEntity.commonImageEntity;
import static com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity.*;
import static com.tsp.new_tsp_admin.api.domain.model.QAdminModelEntity.adminModelEntity;
import static com.tsp.new_tsp_admin.api.domain.model.agency.QAdminAgencyEntity.*;
import static com.tsp.new_tsp_admin.api.domain.model.schedule.QAdminScheduleEntity.*;
import static com.tsp.new_tsp_admin.common.StringUtil.*;
import static com.tsp.new_tsp_admin.exception.ApiExceptionType.NOT_FOUND_MODEL;
import static java.util.Collections.emptyList;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AdminModelJpaQueryRepository {
    private final JPAQueryFactory queryFactory;

    private BooleanExpression searchCategory(Map<String, Object> modelMap) {
        int categoryCd = getInt(modelMap.get("categoryCd"), 0);

        return adminModelEntity.categoryCd.eq(categoryCd);
    }

    private BooleanExpression searchModelInfo(Map<String, Object> modelMap) {
        String searchType = getString(modelMap.get("searchType"), "");
        String searchKeyword = getString(modelMap.get("searchKeyword"), "");

        if (!Objects.equals(searchKeyword, "")) {
            return "0".equals(searchType) ?
                    adminModelEntity.modelKorName.contains(searchKeyword)
                            .or(adminModelEntity.modelEngName.contains(searchKeyword)
                                    .or(adminModelEntity.modelDescription.contains(searchKeyword))) :
                    "1".equals(searchType) ?
                            adminModelEntity.modelKorName.contains(searchKeyword)
                                    .or(adminModelEntity.modelEngName.contains(searchKeyword)) :
                            adminModelEntity.modelDescription.contains(searchKeyword);
        } else {
            return null;
        }
    }

    private BooleanExpression searchNewModel(Map<String, Object> modelMap) {
        String newYn = getString(modelMap.get("newYn"), "");
        return !Objects.equals(newYn, "") ? adminModelEntity.newYn.eq(newYn) : null;
    }

    /**
     * <pre>
     * 1. MethodName : findModelList
     * 2. ClassName  : AdminModelJpaRepository.java
     * 3. Comment    : 관리자 모델 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    public Page<AdminModelDTO> findModelList(Map<String, Object> modelMap, PageRequest pageRequest) {
        List<AdminModelEntity> modelList = queryFactory
                .selectFrom(adminModelEntity)
                .orderBy(adminModelEntity.idx.desc())
                .innerJoin(adminModelEntity.adminAgencyEntity, adminAgencyEntity)
                .where(searchCategory(modelMap), searchModelInfo(modelMap), searchNewModel(modelMap))
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        return new PageImpl<>(AdminModelEntity.toDtoList(modelList), pageRequest, modelList.size());
    }

    /**
     * <pre>
     * 1. MethodName : findOneModel
     * 2. ClassName  : AdminModelJpaRepository.java
     * 3. Comment    : 관리자 모델 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    public AdminModelDTO findOneModel(Long idx) {
        //모델 상세 조회
        AdminModelEntity findOneModel = Optional.ofNullable(queryFactory
                .selectFrom(adminModelEntity)
                .innerJoin(adminModelEntity.adminAgencyEntity, adminAgencyEntity)
                .fetchJoin()
                .leftJoin(adminModelEntity.commonImageEntityList, commonImageEntity)
                .fetchJoin()
                .where(adminModelEntity.idx.eq(idx)
                        .and(adminModelEntity.visible.eq("Y")))
                .fetchOne()).orElseThrow(() -> new TspException(NOT_FOUND_MODEL));

        return toDto(findOneModel);
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneModel
     * 2. ClassName  : AdminModelJpaRepository.java
     * 3. Comment    : 관리자 이전 모델 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 12.
     * </pre>
     */
    public AdminModelDTO findPrevOneModel(AdminModelEntity existAdminModelEntity) {
        // 이전 모델 조회
        AdminModelEntity findPrevOneModel = Optional.ofNullable(queryFactory
                .selectFrom(adminModelEntity)
                .orderBy(adminModelEntity.idx.desc())
                .where(adminModelEntity.idx.lt(existAdminModelEntity.getIdx())
                        .and(adminModelEntity.categoryCd.eq(existAdminModelEntity.getCategoryCd()))
                        .and(adminModelEntity.visible.eq("Y")))
                .fetchFirst()).orElseThrow(() -> new TspException(NOT_FOUND_MODEL));

        return toDto(findPrevOneModel);
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneModel
     * 2. ClassName  : AdminModelJpaRepository.java
     * 3. Comment    : 관리자 다음 모델 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 12.
     * </pre>
     */
    public AdminModelDTO findNextOneModel(AdminModelEntity existAdminModelEntity) {
        // 다음 모델 조회
        AdminModelEntity findNextOneModel = Optional.ofNullable(queryFactory
                .selectFrom(adminModelEntity)
                .orderBy(adminModelEntity.idx.asc())
                .where(adminModelEntity.idx.gt(existAdminModelEntity.getIdx())
                        .and(adminModelEntity.categoryCd.eq(existAdminModelEntity.getCategoryCd()))
                        .and(adminModelEntity.visible.eq("Y")))
                .fetchFirst()).orElseThrow(() -> new TspException(NOT_FOUND_MODEL));

        return toDto(findNextOneModel);
    }

    /**
     * <pre>
     * 1. MethodName : findModelAdminComment
     * 2. ClassName  : AdminModelJpaRepository.java
     * 3. Comment    : 관리자 모델 어드민 코멘트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 26.
     * </pre>
     */
    public List<AdminCommentDTO> findModelAdminComment(Long idx) {
        List<AdminCommentEntity> commentEntity = queryFactory
                .selectFrom(QAdminCommentEntity.adminCommentEntity)
                .where(QAdminCommentEntity.adminCommentEntity.commentType.eq("model")
                        .and(QAdminCommentEntity.adminCommentEntity.adminModelEntity.idx.eq(idx))
                        .and(QAdminCommentEntity.adminCommentEntity.visible.eq("Y")))
                .fetch();

        return commentEntity != null ? toDtoList(commentEntity) : emptyList();
    }

    /**
     * <pre>
     * 1. MethodName : findOneModelSchedule
     * 2. ClassName  : AdminModelJpaRepository.java
     * 3. Comment    : 관리자 모델 스케줄 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 03.
     * </pre>
     */
    public List<AdminScheduleDTO> findOneModelSchedule(Long idx) {
        List<AdminScheduleEntity> scheduleList = queryFactory
                .selectFrom(adminScheduleEntity)
                .orderBy(adminScheduleEntity.idx.desc())
                .where(adminScheduleEntity.adminModelEntity.idx.eq(idx)
                        .and(adminScheduleEntity.visible.eq("Y")))
                .fetch();

        return scheduleList != null ? AdminScheduleEntity.toDtoList(scheduleList) : emptyList();
    }

    /**
     * <pre>
     * 1. MethodName : findRecommendList
     * 2. ClassName  : AdminModelJpaRepository.java
     * 3. Comment    : 관리자 추천 검색어 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2023. 01. 05.
     * </pre>
     */
    public List<AdminRecommendDTO> findRecommendList(Map<String, Object> recommendMap) {
        List<AdminRecommendEntity> recommendList = queryFactory
                .selectFrom(QAdminRecommendEntity.adminRecommendEntity)
                .orderBy(QAdminRecommendEntity.adminRecommendEntity.idx.desc())
                .offset(getInt(recommendMap.get("jpaStartPage"), 0))
                .limit(getInt(recommendMap.get("size"), 0))
                .fetch();

        return recommendList != null ? AdminRecommendEntity.toDtoList(recommendList) : emptyList();
    }
}
