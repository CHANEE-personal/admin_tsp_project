package com.tsp.new_tsp_admin.api.production.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.domain.production.AdminProductionDTO;
import com.tsp.new_tsp_admin.api.domain.production.AdminProductionEntity;
import com.tsp.new_tsp_admin.api.production.mapper.ProductionMapper;
import com.tsp.new_tsp_admin.common.StringUtil;
import com.tsp.new_tsp_admin.exception.ApiExceptionType;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_admin.api.domain.common.QCommonImageEntity.commonImageEntity;
import static com.tsp.new_tsp_admin.api.domain.production.QAdminProductionEntity.adminProductionEntity;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AdminProductionJpaRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private BooleanExpression searchProduction(Map<String, Object> productionMap) {
        String searchType = StringUtil.getString(productionMap.get("searchType"),"");
        String searchKeyword = StringUtil.getString(productionMap.get("searchKeyword"),"");

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
     * @param productionMap
     */
    public Long findProductionsCount(Map<String, Object> productionMap) {

        try {
            return queryFactory.selectFrom(adminProductionEntity)
                    .where(searchProduction(productionMap))
                    .fetchCount();
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.NOT_FOUND_PRODUCTION_LIST);
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
     * @param productionMap
     */
    public List<AdminProductionDTO> findProductionsList(Map<String, Object> productionMap) {
        try {
            List<AdminProductionEntity> productionList = queryFactory
                    .selectFrom(adminProductionEntity)
                    .orderBy(adminProductionEntity.idx.desc())
                    .where(searchProduction(productionMap))
                    .offset(StringUtil.getInt(productionMap.get("jpaStartPage"),0))
                    .limit(StringUtil.getInt(productionMap.get("size"),0))
                    .fetch();

            productionList.forEach(list -> productionList.get(productionList.indexOf(list)).setRnum(StringUtil.getInt(productionMap.get("startPage"),1)*(StringUtil.getInt(productionMap.get("size"),1))-(2-productionList.indexOf(list))));

            return ProductionMapper.INSTANCE.toDtoList(productionList);
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.NOT_FOUND_PRODUCTION_LIST);
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
     * @param existAdminProductionEntity
     */
    public AdminProductionDTO findOneProduction(AdminProductionEntity existAdminProductionEntity) {

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

            return ProductionMapper.INSTANCE.toDto(findOneProduction);
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.NOT_FOUND_PRODUCTION);
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
     * @param adminProductionEntity
     */
    @Transactional
    public Integer insertProduction(AdminProductionEntity adminProductionEntity) {
        try {
            em.persist(adminProductionEntity);

            return adminProductionEntity.getIdx();
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.ERROR_PRODUCTION);
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
     * @param existAdminProductionEntity
     */
    @Modifying
    @Transactional
    public AdminProductionEntity updateProductionByEm(AdminProductionEntity existAdminProductionEntity) {

        try {
            em.merge(existAdminProductionEntity);
            em.flush();
            em.clear();
            return existAdminProductionEntity;
        } catch (Exception e) {
            e.printStackTrace();
            throw new TspException(ApiExceptionType.ERROR_UPDATE_MODEL);
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
     * @param adminProductionEntity
     */
    public AdminProductionEntity deleteProductionByEm(AdminProductionEntity adminProductionEntity) {

        try {
            em.flush();
            em.clear();
            adminProductionEntity = em.find(AdminProductionEntity.class, adminProductionEntity.getIdx());
            em.remove(adminProductionEntity);
            em.flush();
            em.clear();

            return adminProductionEntity;
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.ERROR_DELETE_PRODUCTION);
        }
    }
}
