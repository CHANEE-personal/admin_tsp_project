package com.tsp.new_tsp_admin.api.production.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import com.tsp.new_tsp_admin.api.domain.production.AdminProductionDTO;
import com.tsp.new_tsp_admin.api.domain.production.AdminProductionEntity;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_admin.api.domain.common.QCommonImageEntity.commonImageEntity;
import static com.tsp.new_tsp_admin.api.domain.production.QAdminProductionEntity.adminProductionEntity;
import static com.tsp.new_tsp_admin.api.production.mapper.ProductionMapper.INSTANCE;
import static com.tsp.new_tsp_admin.common.StringUtil.getInt;
import static com.tsp.new_tsp_admin.common.StringUtil.getString;
import static com.tsp.new_tsp_admin.exception.ApiExceptionType.*;
import static com.tsp.new_tsp_admin.exception.ApiExceptionType.NOT_FOUND_PRODUCTION_LIST;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AdminProductionJpaRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private BooleanExpression searchProduction(Map<String, Object> productionMap) {
        String searchType = getString(productionMap.get("searchType"),"");
        String searchKeyword = getString(productionMap.get("searchKeyword"),"");

        if ("0".equals(searchType)) {
            return adminProductionEntity.title.contains(searchKeyword)
                    .or(adminProductionEntity.description.contains(searchKeyword));
        } else if ("1".equals(searchType)) {
            return adminProductionEntity.title.contains(searchKeyword);
        } else {
            return adminProductionEntity.description.contains(searchKeyword);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findProductionsCount
     * 2. ClassName  : AdminProductionJpaRepository.java
     * 3. Comment    : 관리자 프로덕션 리스트 갯수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 09.
     * </pre>
     *
     */
    @Transactional(readOnly = true)
    public int findProductionsCount(Map<String, Object> productionMap) throws TspException {
        try {
            return queryFactory.selectFrom(adminProductionEntity)
                    .where(searchProduction(productionMap)).fetch().size();
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_PRODUCTION_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findProductionsList
     * 2. ClassName  : AdminProductionJpaRepository.java
     * 3. Comment    : 관리자 프로덕션 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 09.
     * </pre>
     *
     */
    @Transactional(readOnly = true)
    public List<AdminProductionDTO> findProductionsList(Map<String, Object> productionMap) throws TspException {
        try {
            List<AdminProductionEntity> productionList = queryFactory
                    .selectFrom(adminProductionEntity)
                    .orderBy(adminProductionEntity.idx.desc())
                    .where(searchProduction(productionMap))
                    .offset(getInt(productionMap.get("jpaStartPage"),0))
                    .limit(getInt(productionMap.get("size"),0))
                    .fetch();

            productionList.forEach(list -> productionList.get(productionList.indexOf(list))
                    .setRnum(getInt(productionMap.get("startPage"),1)*(getInt(productionMap.get("size"),1))-(2-productionList.indexOf(list))));

            return INSTANCE.toDtoList(productionList);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_PRODUCTION_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findOneProduction
     * 2. ClassName  : AdminProductionJpaRepository.java
     * 3. Comment    : 관리자 프로덕션 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 09.
     * </pre>
     *
     */
    @Transactional(readOnly = true)
    public AdminProductionDTO findOneProduction(AdminProductionEntity existAdminProductionEntity) throws TspException {
        try {
            AdminProductionEntity findOneProduction = queryFactory
                    .selectFrom(adminProductionEntity)
                    .orderBy(adminProductionEntity.idx.desc())
                    .leftJoin(adminProductionEntity.commonImageEntityList, commonImageEntity)
                    .fetchJoin()
                    .where(adminProductionEntity.idx.eq(existAdminProductionEntity.getIdx())
                            .and(adminProductionEntity.visible.eq("Y"))
                            .and(commonImageEntity.typeName.eq("production")))
                    .fetchOne();

            return INSTANCE.toDto(findOneProduction);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_PRODUCTION, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : insertProduction
     * 2. ClassName  : AdminProductionJpaRepository.java
     * 3. Comment    : 관리자 프로덕션 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 09.
     * </pre>
     *
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    public AdminProductionDTO insertProduction(AdminProductionEntity adminProductionEntity) throws TspException {
        try {
            em.persist(adminProductionEntity);

            return INSTANCE.toDto(adminProductionEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_PRODUCTION, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : insertProductionImage
     * 2. ClassName  : AdminProductionJpaRepository.java
     * 3. Comment    : 관리자 프로덕션 이미지 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 14.
     * </pre>
     *
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    public Integer insertProductionImage(CommonImageEntity commonImageEntity) throws TspException {
        try {
            em.persist(commonImageEntity);

            return commonImageEntity.getIdx();
        } catch (Exception e) {
            throw new TspException(ERROR_PRODUCTION, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateProductionByEm
     * 2. ClassName  : AdminProductionJpaRepository.java
     * 3. Comment    : 관리자 프로덕션 수정 by entityManager
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 09.
     * </pre>
     *
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    public AdminProductionDTO updateProductionByEm(AdminProductionEntity existAdminProductionEntity) throws TspException {
        try {
            em.merge(existAdminProductionEntity);
            em.flush();
            em.clear();

            return INSTANCE.toDto(existAdminProductionEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_UPDATE_MODEL, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteProductionByEm
     * 2. ClassName  : AdminProductionJpaRepository.java
     * 3. Comment    : 관리자 프로덕션 삭제 by entityManager
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 09.
     * </pre>
     *
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    public Integer deleteProductionByEm(Integer idx) throws TspException {
        try {
            em.remove(em.find(AdminProductionEntity.class, idx));
            em.flush();
            em.clear();

            return idx;
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_PRODUCTION, e);
        }
    }
}
