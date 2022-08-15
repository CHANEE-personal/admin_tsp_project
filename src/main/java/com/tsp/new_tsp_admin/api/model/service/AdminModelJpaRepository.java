package com.tsp.new_tsp_admin.api.model.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageDTO;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelDTO;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import com.tsp.new_tsp_admin.api.model.mapper.ModelImageMapper;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_admin.api.domain.common.QCommonImageEntity.commonImageEntity;
import static com.tsp.new_tsp_admin.api.domain.model.QAdminModelEntity.adminModelEntity;
import static com.tsp.new_tsp_admin.api.domain.model.agency.QAdminAgencyEntity.*;
import static com.tsp.new_tsp_admin.api.model.mapper.ModelMapper.INSTANCE;
import static com.tsp.new_tsp_admin.common.StringUtil.getInt;
import static com.tsp.new_tsp_admin.common.StringUtil.getString;
import static com.tsp.new_tsp_admin.exception.ApiExceptionType.NOT_FOUND_AGENCY;
import static java.util.Objects.requireNonNull;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AdminModelJpaRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private BooleanExpression searchModel(Map<String, Object> modelMap) {
        String searchType = getString(modelMap.get("searchType"), "");
        String searchKeyword = getString(modelMap.get("searchKeyword"), "");
        Integer categoryCd = getInt(modelMap.get("categoryCd"), 0);

        if ("0".equals(searchType)) {
            return adminModelEntity.modelKorName.contains(searchKeyword)
                    .or(adminModelEntity.modelEngName.contains(searchKeyword)
                    .or(adminModelEntity.modelDescription.contains(searchKeyword)))
                    .and(adminModelEntity.categoryCd.eq(categoryCd));
        } else if ("1".equals(searchType)) {
            return adminModelEntity.modelKorName.contains(searchKeyword)
                    .or(adminModelEntity.modelEngName.contains(searchKeyword))
                    .and(adminModelEntity.categoryCd.eq(categoryCd));
        } else {
            if (!"".equals(searchKeyword)) {
                return adminModelEntity.modelDescription.contains(searchKeyword).and(adminModelEntity.categoryCd.eq(categoryCd));
            } else {
                return adminModelEntity.categoryCd.eq(categoryCd);
            }
        }
    }

    /**
     * <pre>
     * 1. MethodName : findModelsCount
     * 2. ClassName  : AdminModelJpaRepository.java
     * 3. Comment    : 관리자 모델 리스트 갯수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    public Integer findModelsCount(Map<String, Object> modelMap) {
        return queryFactory.selectFrom(adminModelEntity).where(searchModel(modelMap)).fetch().size();
    }


    /**
     * <pre>
     * 1. MethodName : findModelsList
     * 2. ClassName  : AdminModelJpaRepository.java
     * 3. Comment    : 관리자 모델 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    public List<AdminModelDTO> findModelsList(Map<String, Object> modelMap) {
        List<AdminModelEntity> modelList = queryFactory
                .selectFrom(adminModelEntity)
                .orderBy(adminModelEntity.idx.desc())
                .leftJoin(adminModelEntity.adminAgencyEntity, adminAgencyEntity)
                .fetchJoin()
                .where(searchModel(modelMap).and(adminModelEntity.visible.eq("Y")))
                .offset(getInt(modelMap.get("jpaStartPage"), 0))
                .limit(getInt(modelMap.get("size"), 0))
                .fetch();

        modelList.forEach(list -> modelList.get(modelList.indexOf(list))
                .setRnum(getInt(modelMap.get("startPage"), 1) * (getInt(modelMap.get("size"), 1)) - (2 - modelList.indexOf(list))));

        return INSTANCE.toDtoList(modelList);
    }

    /**
     * <pre>
     * 1. MethodName : findOneModel
     * 2. ClassName  : ModelRepository.java
     * 3. Comment    : 관리자 모델 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    public AdminModelDTO findOneModel(AdminModelEntity existAdminModelEntity) {
        //모델 상세 조회
        AdminModelEntity findOneModel = queryFactory
                .selectFrom(adminModelEntity)
                .orderBy(adminModelEntity.idx.desc())
                .innerJoin(adminModelEntity.adminAgencyEntity, adminAgencyEntity)
                .leftJoin(adminModelEntity.commonImageEntityList, commonImageEntity)
                .fetchJoin()
                .where(adminModelEntity.idx.eq(existAdminModelEntity.getIdx())
                        .and(adminModelEntity.visible.eq("Y"))
                        .and(commonImageEntity.typeName.eq("model")))
                .fetchOne();

        return INSTANCE.toDto(findOneModel);
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
        return INSTANCE.toDto(adminModelEntity);
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
        return ModelImageMapper.INSTANCE.toDto(commonImageEntity);
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
        return INSTANCE.toDto(existAdminModelEntity);
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
    public Long deleteModel(AdminModelEntity existAdminModelEntity) {
        JPAUpdateClause update = new JPAUpdateClause(em, adminModelEntity);

        existAdminModelEntity.setUpdater("1");
        existAdminModelEntity.setUpdateTime(new Date());

        return update.set(adminModelEntity.visible, "N")
                .set(adminModelEntity.updateTime, existAdminModelEntity.getUpdateTime())
                .set(adminModelEntity.updater, "1")
                .where(adminModelEntity.idx.eq(existAdminModelEntity.getIdx())).execute();
    }

    /**
     * <pre>
     * 1. MethodName : deleteModel
     * 2. ClassName  : AdminModelJpaRepository.java
     * 3. Comment    : 관리자 모델 삭제 by entityManager
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 17.
     * </pre>
     */
    public Integer deleteModelByEm(Integer idx) {
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
        Integer agencyIdx = requireNonNull(queryFactory
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

            return INSTANCE.toDto(existAdminModelEntity);
        } else {
            throw new TspException(NOT_FOUND_AGENCY, new Throwable("NOT_FOUND_AGENCY"));
        }
    }
}
