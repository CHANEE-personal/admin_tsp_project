package com.tsp.new_tsp_admin.api.model.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.comment.mapper.AdminCommentMapper;
import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentDTO;
import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentEntity;
import com.tsp.new_tsp_admin.api.domain.comment.QAdminCommentEntity;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageDTO;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelDTO;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import com.tsp.new_tsp_admin.api.domain.model.schedule.AdminScheduleDTO;
import com.tsp.new_tsp_admin.api.domain.model.schedule.AdminScheduleEntity;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.stream.Collectors;

import static com.tsp.new_tsp_admin.api.domain.common.QCommonImageEntity.commonImageEntity;
import static com.tsp.new_tsp_admin.api.domain.model.QAdminModelEntity.adminModelEntity;
import static com.tsp.new_tsp_admin.api.domain.model.agency.QAdminAgencyEntity.*;
import static com.tsp.new_tsp_admin.api.domain.model.schedule.QAdminScheduleEntity.*;
import static com.tsp.new_tsp_admin.common.StringUtil.*;
import static com.tsp.new_tsp_admin.exception.ApiExceptionType.NOT_FOUND_AGENCY;
import static java.util.Objects.requireNonNull;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AdminModelJpaRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private BooleanExpression searchCategory(Map<String, Object> modelMap) {
        int categoryCd = getInt(modelMap.get("categoryCd"), 0);

        return adminModelEntity.categoryCd.eq(categoryCd);
    }

    private BooleanExpression searchModelInfo(Map<String, Object> modelMap) {
        String searchType = getString(modelMap.get("searchType"), "");
        String searchKeyword = getString(modelMap.get("searchKeyword"), "");

        if ("0".equals(searchType)) {
            return adminModelEntity.modelKorName.contains(searchKeyword)
                    .or(adminModelEntity.modelEngName.contains(searchKeyword)
                    .or(adminModelEntity.modelDescription.contains(searchKeyword)));
        } else if ("1".equals(searchType)) {
            return adminModelEntity.modelKorName.contains(searchKeyword)
                    .or(adminModelEntity.modelEngName.contains(searchKeyword));
        } else {
            return adminModelEntity.modelDescription.contains(searchKeyword);
        }
    }

    private BooleanExpression searchNewModel(Map<String, Object> modelMap) {
        return adminModelEntity.newYn.eq(getString(modelMap.get("searchNewModel"), "N"));
    }

    /**
     * <pre>
     * 1. MethodName : findModelCount
     * 2. ClassName  : AdminModelJpaRepository.java
     * 3. Comment    : 관리자 모델 리스트 갯수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    public int findModelCount(Map<String, Object> modelMap) {
        return queryFactory.selectFrom(adminModelEntity)
                .where((searchCategory(modelMap).or(searchModelInfo(modelMap)))
                        .and(searchNewModel(modelMap)))
                .fetch().size();
    }


    /**
     * <pre>
     * 1. MethodName : findModelList
     * 2. ClassName  : AdminModelJpaRepository.java
     * 3. Comment    : 관리자 모델 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    public List<AdminModelDTO> findModelList(Map<String, Object> modelMap) {
        List<AdminModelEntity> modelList = queryFactory
                .selectFrom(adminModelEntity)
                .orderBy(adminModelEntity.idx.desc())
                .leftJoin(adminModelEntity.adminAgencyEntity, adminAgencyEntity)
                .fetchJoin()
                .leftJoin(adminModelEntity.commonImageEntityList, commonImageEntity)
                .fetchJoin()
                .where((searchCategory(modelMap).or(searchModelInfo(modelMap))).and(searchNewModel(modelMap)))
                .offset(getInt(modelMap.get("jpaStartPage"), 0))
                .limit(getInt(modelMap.get("size"), 0))
                .fetch();

        modelList.forEach(list -> modelList.get(modelList.indexOf(list))
                .setRowNum(getInt(modelMap.get("startPage"), 1) * (getInt(modelMap.get("size"), 1)) - (2 - modelList.indexOf(list))));

        return modelList.stream().map(AdminModelEntity::toDto).collect(Collectors.toList());
    }

    /**
     * <pre>
     * 1. MethodName : findOneModel
     * 2. ClassName  : AdminModelJpaRepository.java
     * 3. Comment    : 관리자 모델 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    public AdminModelDTO findOneModel(Long idx) {
        //모델 상세 조회
        AdminModelEntity findOneModel = queryFactory
                .selectFrom(adminModelEntity)
                .innerJoin(adminModelEntity.adminAgencyEntity, adminAgencyEntity)
                .fetchJoin()
                .leftJoin(adminModelEntity.commonImageEntityList, commonImageEntity)
                .fetchJoin()
                .where(adminModelEntity.idx.eq(idx)
                        .and(adminModelEntity.visible.eq("Y"))
                        .and(commonImageEntity.typeName.eq("model")))
                .fetchOne();

        assert findOneModel != null;
        return AdminModelEntity.toDto(findOneModel);
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneModel
     * 2. ClassName  : AdminModelJpaRepository.java
     * 3. Comment    : 관리자 이전 모델 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 12.
     * </pre>
     */
    public AdminModelDTO findPrevOneModel(AdminModelEntity existAdminModelEntity) {
        // 이전 모델 조회
        AdminModelEntity findPrevOneModel = queryFactory
                .selectFrom(adminModelEntity)
                .orderBy(adminModelEntity.idx.desc())
                .where(adminModelEntity.idx.lt(existAdminModelEntity.getIdx())
                        .and(adminModelEntity.categoryCd.eq(existAdminModelEntity.getCategoryCd()))
                        .and(adminModelEntity.visible.eq("Y")))
                .fetchFirst();

        return AdminModelEntity.toDto(findPrevOneModel);
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneModel
     * 2. ClassName  : AdminModelJpaRepository.java
     * 3. Comment    : 관리자 다음 모델 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 12.
     * </pre>
     */
    public AdminModelDTO findNextOneModel(AdminModelEntity existAdminModelEntity) {
        // 다음 모델 조회
        AdminModelEntity findNextOneModel = queryFactory
                .selectFrom(adminModelEntity)
                .orderBy(adminModelEntity.idx.asc())
                .where(adminModelEntity.idx.gt(existAdminModelEntity.getIdx())
                        .and(adminModelEntity.categoryCd.eq(existAdminModelEntity.getCategoryCd()))
                        .and(adminModelEntity.visible.eq("Y")))
                .fetchFirst();

        return AdminModelEntity.toDto(findNextOneModel);
    }

    /**
     * <pre>
     * 1. MethodName : insertModel
     * 2. ClassName  : AdminModelJpaRepository.java
     * 3. Comment    : 관리자 모델 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 07.
     * </pre>
     */
    public AdminModelDTO insertModel(AdminModelEntity adminModelEntity) {
        em.persist(adminModelEntity);
        return AdminModelEntity.toDto(adminModelEntity);
    }

    /**
     * <pre>
     * 1. MethodName : insertModelImage
     * 2. ClassName  : AdminModelJpaRepository.java
     * 3. Comment    : 관리자 모델 이미지 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 07.
     * </pre>
     */
    public CommonImageDTO insertModelImage(CommonImageEntity commonImageEntity) {
        em.persist(commonImageEntity);
        return CommonImageEntity.toDto(commonImageEntity);
    }

    /**
     * <pre>
     * 1. MethodName : updateModel
     * 2. ClassName  : AdminModelJpaRepository.java
     * 3. Comment    : 관리자 모델 수정 by queryDsl
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 07.
     * </pre>
     */
//    @Modifying(clearAutomatically = true)
//    public AdminModelEntity updateModel(AdminModelEntity existAdminModelEntity) {
//        JPAUpdateClause update = new JPAUpdateClause(em, adminModelEntity);
//
//        existAdminModelEntity.setUpdater("1");
//        existAdminModelEntity.setUpdateTime(new Date());
//
//        update.set(adminModelEntity.modelKorName, existAdminModelEntity.getModelKorName())
//                .set(adminModelEntity.categoryCd, existAdminModelEntity.getCategoryCd())
//                .set(adminModelEntity.modelEngName, existAdminModelEntity.getModelEngName())
//                .set(adminModelEntity.modelDescription, existAdminModelEntity.getModelDescription())
//                .set(adminModelEntity.height, existAdminModelEntity.getHeight())
//                .set(adminModelEntity.size3, existAdminModelEntity.getSize3())
//                .set(adminModelEntity.shoes, existAdminModelEntity.getShoes())
//                .set(adminModelEntity.categoryAge, existAdminModelEntity.getCategoryAge())
//                .set(adminModelEntity.updateTime, existAdminModelEntity.getUpdateTime())
//                .set(adminModelEntity.updater, "1")
//                .where(adminModelEntity.idx.eq(existAdminModelEntity.getIdx())).execute();
//
//        return existAdminModelEntity;
//    }

    /**
     * <pre>
     * 1. MethodName : updateModelByEm
     * 2. ClassName  : AdminModelJpaRepository.java
     * 3. Comment    : 관리자 모델 수정 by entityManager
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 07.
     * </pre>
     */
    public AdminModelDTO updateModelByEm(AdminModelEntity existAdminModelEntity) {
        em.merge(existAdminModelEntity);
        em.flush();
        em.clear();
        return AdminModelEntity.toDto(existAdminModelEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deleteModel
     * 2. ClassName  : AdminModelJpaRepository.java
     * 3. Comment    : 관리자 모델 삭제 by queryDsl
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 17.
     * </pre>
     */
//    public Long deleteModel(AdminModelEntity existAdminModelEntity) {
//        JPAUpdateClause update = new JPAUpdateClause(em, adminModelEntity);
//
//        existAdminModelEntity.setUpdater(1L);
//        existAdminModelEntity.setUpdateTime(LocalDateTime.now());
//
//        return update.set(adminModelEntity.visible, "N")
//                .set(adminModelEntity.updateTime, existAdminModelEntity.getUpdateTime())
//                .set(adminModelEntity.updater, 1L)
//                .where(adminModelEntity.idx.eq(existAdminModelEntity.getIdx())).execute();
//    }

    /**
     * <pre>
     * 1. MethodName : deleteModel
     * 2. ClassName  : AdminModelJpaRepository.java
     * 3. Comment    : 관리자 모델 삭제 by entityManager
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 17.
     * </pre>
     */
    public Long deleteModelByEm(Long idx) {
        em.remove(em.find(AdminModelEntity.class, idx));
        em.flush();
        em.clear();
        return idx;
    }

    /**
     * <pre>
     * 1. MethodName : updateModelAgency
     * 2. ClassName  : AdminModelJpaRepository.java
     * 3. Comment    : 관리자 모델 소속사 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 14.
     * </pre>
     */
    public AdminModelDTO updateModelAgency(AdminModelEntity existAdminModelEntity) {
        // 소속사 존재 여부 체크
        Long agencyIdx = requireNonNull(queryFactory
                .selectFrom(adminAgencyEntity)
                .where(adminAgencyEntity.idx.eq(existAdminModelEntity.getAgencyIdx())).fetchOne()).getIdx();

        if (agencyIdx != null) {
            queryFactory
                    .update(adminModelEntity)
                    .where(adminModelEntity.idx.eq(existAdminModelEntity.getIdx()))
                    .set(adminModelEntity.adminAgencyEntity.idx, agencyIdx)
                    .execute();

            em.flush();
            em.clear();

            return AdminModelEntity.toDto(existAdminModelEntity);
        } else {
            throw new TspException(NOT_FOUND_AGENCY, new Throwable("NOT_FOUND_AGENCY"));
        }
    }

    /**
     * <pre>
     * 1. MethodName : findModelAdminComment
     * 2. ClassName  : AdminModelJpaRepository.java
     * 3. Comment    : 관리자 모델 어드민 코멘트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 26.
     * </pre>
     */
    public List<AdminCommentDTO> findModelAdminComment(Long idx) {
        List<AdminCommentEntity> adminCommentEntity = queryFactory
                .selectFrom(QAdminCommentEntity.adminCommentEntity)
                .where(QAdminCommentEntity.adminCommentEntity.commentType.eq("model")
                        .and(QAdminCommentEntity.adminCommentEntity.commentTypeIdx.eq(idx))
                        .and(QAdminCommentEntity.adminCommentEntity.visible.eq("Y")))
                .fetch();

        return AdminCommentMapper.INSTANCE.toDtoList(adminCommentEntity);
    }

    /**
     * <pre>
     * 1. MethodName : toggleModelNewYn
     * 2. ClassName  : AdminModelJpaRepository.java
     * 3. Comment    : 관리자 새로운 모델 설정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 29.
     * </pre>
     */
    public AdminModelDTO toggleModelNewYn(Long idx) {
        String newYn = Objects.equals(em.find(AdminModelEntity.class, idx).getNewYn(), "Y") ? "N" : "Y";

        queryFactory
                .update(adminModelEntity)
                .where(adminModelEntity.idx.eq(idx))
                .set(adminModelEntity.newYn, newYn)
                .execute();

        em.flush();
        em.clear();

        return AdminModelEntity.toDto(em.find(AdminModelEntity.class, idx));
    }

    /**
     * <pre>
     * 1. MethodName : findOneModelSchedule
     * 2. ClassName  : AdminModelJpaRepository.java
     * 3. Comment    : 관리자 모델 스케줄 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 03.
     * </pre>
     */
    public List<AdminScheduleDTO> findOneModelSchedule(Long idx) {
        List<AdminScheduleEntity> scheduleList = queryFactory
                .selectFrom(adminScheduleEntity)
                .orderBy(adminScheduleEntity.idx.desc())
                .where(adminScheduleEntity.modelIdx.eq(idx)
                        .and(adminScheduleEntity.visible.eq("Y")))
                .fetch();

        return scheduleList.stream().map(AdminScheduleEntity::toDto).collect(Collectors.toList());
    }
}
