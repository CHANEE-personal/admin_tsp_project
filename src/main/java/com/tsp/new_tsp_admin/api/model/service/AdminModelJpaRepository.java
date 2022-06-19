package com.tsp.new_tsp_admin.api.model.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.tsp.new_tsp_admin.api.domain.common.CommonCodeEntity;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageDTO;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelDTO;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import com.tsp.new_tsp_admin.api.model.mapper.ModelImageMapper;
import com.tsp.new_tsp_admin.api.model.mapper.ModelMapper;
import com.tsp.new_tsp_admin.common.StringUtil;
import com.tsp.new_tsp_admin.exception.ApiExceptionType;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_admin.api.domain.common.QCommonCodeEntity.commonCodeEntity;
import static com.tsp.new_tsp_admin.api.domain.common.QCommonImageEntity.commonImageEntity;
import static com.tsp.new_tsp_admin.api.domain.model.QAdminModelEntity.adminModelEntity;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AdminModelJpaRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private BooleanExpression searchModel(Map<String, Object> modelMap) {
        String searchType = StringUtil.getString(modelMap.get("searchType"),"");
        String searchKeyword = StringUtil.getString(modelMap.get("searchKeyword"),"");
        Integer categoryCd = StringUtil.getInt(modelMap.get("categoryCd"),0);

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
            if(!"".equals(searchKeyword)) {
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
     *
     */
    @Transactional(readOnly = true)
    public Long findModelsCount(Map<String, Object> modelMap) {

        try {
            return queryFactory.selectFrom(adminModelEntity)
                    .where(searchModel(modelMap))
                    .fetchCount();
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.NOT_FOUND_MODEL_LIST);
        }
    }


    /**
     * <pre>
     * 1. MethodName : findModelsList
     * 2. ClassName  : AdminModelJpaRepository.java
     * 3. Comment    : 관리자 모델 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     */
    @Transactional(readOnly = true)
    public List<AdminModelDTO> findModelsList(Map<String, Object> modelMap) {

        try {
            List<AdminModelEntity> modelList = queryFactory
                    .selectFrom(adminModelEntity)
                    .orderBy(adminModelEntity.idx.desc())
                    .where(searchModel(modelMap))
                    .offset(StringUtil.getInt(modelMap.get("jpaStartPage"),0))
                    .limit(StringUtil.getInt(modelMap.get("size"),0))
                    .fetch();

            modelList.forEach(list -> modelList.get(modelList.indexOf(list)).setRnum(StringUtil.getInt(modelMap.get("startPage"),1)*(StringUtil.getInt(modelMap.get("size"),1))-(2-modelList.indexOf(list))));

            return ModelMapper.INSTANCE.toDtoList(modelList);
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.NOT_FOUND_MODEL_LIST);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findOneModel
     * 2. ClassName  : ModelRepository.java
     * 3. Comment    : 관리자 모델 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     */
    @Transactional(readOnly = true)
    public AdminModelDTO findOneModel(AdminModelEntity existAdminModelEntity) {

        try {
            //모델 상세 조회
            AdminModelEntity findOneModel = queryFactory
                    .selectFrom(adminModelEntity)
                    .orderBy(adminModelEntity.idx.desc())
                    .leftJoin(adminModelEntity.commonImageEntityList, commonImageEntity)
                    .fetchJoin()
                    .where(adminModelEntity.idx.eq(existAdminModelEntity.getIdx())
                            .and(adminModelEntity.visible.eq("Y"))
                            .and(commonImageEntity.typeName.eq("model")))
                    .fetchOne();

            return ModelMapper.INSTANCE.toDto(findOneModel);
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.NOT_FOUND_MODEL);
        }
    }

    /**
     * <pre>
     * 1. MethodName : insertModel
     * 2. ClassName  : AdminModelJpaRepository.java
     * 3. Comment    : 관리자 모델 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 07.
     * </pre>
     *
     */
    @Transactional
    public AdminModelDTO insertModel(AdminModelEntity adminModelEntity) {
        try {
            em.persist(adminModelEntity);

            return ModelMapper.INSTANCE.toDto(adminModelEntity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TspException(ApiExceptionType.ERROR_MODEL);
        }
    }

    /**
     * <pre>
     * 1. MethodName : insertModelImage
     * 2. ClassName  : AdminModelJpaRepository.java
     * 3. Comment    : 관리자 모델 이미지 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 07.
     * </pre>
     *
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    public CommonImageDTO insertModelImage(CommonImageEntity commonImageEntity) {
        try {
            em.persist(commonImageEntity);

            return ModelImageMapper.INSTANCE.toDto(commonImageEntity);
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.ERROR_MODEL);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateModel
     * 2. ClassName  : AdminModelJpaRepository.java
     * 3. Comment    : 관리자 모델 수정 by queryDsl
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 07.
     * </pre>
     *
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    public AdminModelEntity updateModel(AdminModelEntity existAdminModelEntity) {

        try {
            JPAUpdateClause update = new JPAUpdateClause(em, adminModelEntity);

            existAdminModelEntity.setUpdater("1");
            existAdminModelEntity.setUpdateTime(new Date());

            update.set(adminModelEntity.modelKorName, existAdminModelEntity.getModelKorName())
                    .set(adminModelEntity.categoryCd, existAdminModelEntity.getCategoryCd())
                    .set(adminModelEntity.modelEngName, existAdminModelEntity.getModelEngName())
                    .set(adminModelEntity.modelDescription, existAdminModelEntity.getModelDescription())
                    .set(adminModelEntity.height, existAdminModelEntity.getHeight())
                    .set(adminModelEntity.size3, existAdminModelEntity.getSize3())
                    .set(adminModelEntity.shoes, existAdminModelEntity.getShoes())
                    .set(adminModelEntity.categoryAge, existAdminModelEntity.getCategoryAge())
                    .set(adminModelEntity.updateTime, existAdminModelEntity.getUpdateTime())
                    .set(adminModelEntity.updater, "1")
                    .where(adminModelEntity.idx.eq(existAdminModelEntity.getIdx())).execute();

            return existAdminModelEntity;
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.ERROR_UPDATE_MODEL);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateModelByEm
     * 2. ClassName  : AdminModelJpaRepository.java
     * 3. Comment    : 관리자 모델 수정 by entityManager
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 07.
     * </pre>
     *
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    public AdminModelDTO updateModelByEm(AdminModelEntity existAdminModelEntity) {

        try {
            em.merge(existAdminModelEntity);
            em.flush();
            em.clear();

            return ModelMapper.INSTANCE.toDto(existAdminModelEntity);
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.ERROR_UPDATE_MODEL);
        }
    }

    /**
     * <pre>
     * 1. MethodName : modelCommonCode
     * 2. ClassName  : AdminModelJpaRepository.java
     * 3. Comment    : 관리자 모델 공통 코드 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     */
    public List<CommonCodeEntity> modelCommonCode(CommonCodeEntity existModelCodeEntity) {
        try {
            return queryFactory
                    .selectFrom(commonCodeEntity)
                    .where(commonCodeEntity.cmmType.eq(existModelCodeEntity.getCmmType()))
                    .fetch();
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.NOT_FOUND_COMMON);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteModel
     * 2. ClassName  : AdminModelJpaRepository.java
     * 3. Comment    : 관리자 모델 삭제 by queryDsl
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 17.
     * </pre>
     *
     */
    public Long deleteModel(AdminModelEntity existAdminModelEntity) {

        try {
            JPAUpdateClause update = new JPAUpdateClause(em, adminModelEntity);

            existAdminModelEntity.setUpdater("1");
            existAdminModelEntity.setUpdateTime(new Date());

            return update.set(adminModelEntity.visible, "N")
                    .set(adminModelEntity.updateTime, existAdminModelEntity.getUpdateTime())
                    .set(adminModelEntity.updater, "1")
                    .where(adminModelEntity.idx.eq(existAdminModelEntity.getIdx())).execute();
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.ERROR_DELETE_MODEL);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteModel
     * 2. ClassName  : AdminModelJpaRepository.java
     * 3. Comment    : 관리자 모델 삭제 by entityManager
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 17.
     * </pre>
     *
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    public AdminModelDTO deleteModelByEm(AdminModelEntity adminModelEntity) {

        try {
            adminModelEntity = em.find(AdminModelEntity.class, adminModelEntity.getIdx());
            em.remove(adminModelEntity);
            em.flush();
            em.clear();

            return ModelMapper.INSTANCE.toDto(adminModelEntity);
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.ERROR_DELETE_MODEL);
        }
    }
}
