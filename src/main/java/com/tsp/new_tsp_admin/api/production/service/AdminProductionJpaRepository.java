package com.tsp.new_tsp_admin.api.production.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.common.EntityType;
import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentDTO;
import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentEntity;
import com.tsp.new_tsp_admin.api.domain.comment.QAdminCommentEntity;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import com.tsp.new_tsp_admin.api.domain.production.AdminProductionDTO;
import com.tsp.new_tsp_admin.api.domain.production.AdminProductionEntity;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.*;

import static com.tsp.new_tsp_admin.api.domain.comment.AdminCommentEntity.toDtoList;
import static com.tsp.new_tsp_admin.api.domain.common.QCommonImageEntity.commonImageEntity;
import static com.tsp.new_tsp_admin.api.domain.production.AdminProductionEntity.toDto;
import static com.tsp.new_tsp_admin.api.domain.production.AdminProductionEntity.toDtoList;
import static com.tsp.new_tsp_admin.api.domain.production.QAdminProductionEntity.adminProductionEntity;
import static com.tsp.new_tsp_admin.common.StringUtil.getInt;
import static com.tsp.new_tsp_admin.common.StringUtil.getString;
import static com.tsp.new_tsp_admin.exception.ApiExceptionType.*;
import static java.util.Collections.emptyList;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AdminProductionJpaRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private BooleanExpression searchProduction(Map<String, Object> productionMap) {
        String searchType = getString(productionMap.get("searchType"), "");
        String searchKeyword = getString(productionMap.get("searchKeyword"), "");

        if (!Objects.equals(searchKeyword, "")) {
            return "0".equals(searchType) ?
                    adminProductionEntity.title.contains(searchKeyword)
                            .or(adminProductionEntity.description.contains(searchKeyword)) :
                    "1".equals(searchType) ?
                            adminProductionEntity.title.contains(searchKeyword) :
                            adminProductionEntity.description.contains(searchKeyword);
        } else {
            return null;
        }
    }

    /**
     * <pre>
     * 1. MethodName : findProductionCount
     * 2. ClassName  : AdminProductionJpaRepository.java
     * 3. Comment    : 관리자 프로덕션 리스트 갯수 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 09.
     * </pre>
     */
    public int findProductionCount(Map<String, Object> productionMap) {
        return queryFactory.selectFrom(adminProductionEntity).where(searchProduction(productionMap)).fetch().size();
    }

    /**
     * <pre>
     * 1. MethodName : findProductionList
     * 2. ClassName  : AdminProductionJpaRepository.java
     * 3. Comment    : 관리자 프로덕션 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 09.
     * </pre>
     */
    public List<AdminProductionDTO> findProductionList(Map<String, Object> productionMap) {
        List<AdminProductionEntity> productionList = queryFactory
                .selectFrom(adminProductionEntity)
                .orderBy(adminProductionEntity.idx.desc())
                .where(searchProduction(productionMap))
                .offset(getInt(productionMap.get("jpaStartPage"), 0))
                .limit(getInt(productionMap.get("size"), 0))
                .fetch();

        return productionList != null ? toDtoList(productionList) : emptyList();
    }

    /**
     * <pre>
     * 1. MethodName : findOneProduction
     * 2. ClassName  : AdminProductionJpaRepository.java
     * 3. Comment    : 관리자 프로덕션 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 09.
     * </pre>
     */
    public AdminProductionDTO findOneProduction(Long idx) {
        AdminProductionEntity findOneProduction = Optional.ofNullable(queryFactory
                .selectFrom(adminProductionEntity)
                .orderBy(adminProductionEntity.idx.desc())
                .leftJoin(adminProductionEntity.commonImageEntityList, commonImageEntity)
                .fetchJoin()
                .where(adminProductionEntity.idx.eq(idx)
                        .and(adminProductionEntity.visible.eq("Y"))
                        .and(commonImageEntity.typeName.eq(EntityType.PRODUCTION)))
                .fetchOne()).orElseThrow(() -> new TspException(NOT_FOUND_PRODUCTION, new Throwable()));

        return toDto(findOneProduction);
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneProduction
     * 2. ClassName  : AdminProductionJpaRepository.java
     * 3. Comment    : 관리자 이전 프로덕션 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 13.
     * </pre>
     */
    public AdminProductionDTO findPrevOneProduction(Long idx) {
        // 이전 프로덕션 조회
        AdminProductionEntity findPrevOneProduction = Optional.ofNullable(queryFactory
                .selectFrom(adminProductionEntity)
                .orderBy(adminProductionEntity.idx.desc())
                .where(adminProductionEntity.idx.lt(idx)
                        .and(adminProductionEntity.visible.eq("Y")))
                .fetchFirst()).orElseThrow(() -> new TspException(NOT_FOUND_PRODUCTION, new Throwable()));

        return toDto(findPrevOneProduction);
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneProduction
     * 2. ClassName  : AdminProductionJpaRepository.java
     * 3. Comment    : 관리자 다음 프로덕션 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 13.
     * </pre>
     */
    public AdminProductionDTO findNextOneProduction(Long idx) {
        // 다음 프로덕션 조회
        AdminProductionEntity findNextOneProduction = Optional.ofNullable(queryFactory
                .selectFrom(adminProductionEntity)
                .orderBy(adminProductionEntity.idx.asc())
                .where(adminProductionEntity.idx.gt(idx)
                        .and(adminProductionEntity.visible.eq("Y")))
                .fetchFirst()).orElseThrow(() -> new TspException(NOT_FOUND_PRODUCTION, new Throwable()));

        return toDto(findNextOneProduction);
    }

    /**
     * <pre>
     * 1. MethodName : insertProduction
     * 2. ClassName  : AdminProductionJpaRepository.java
     * 3. Comment    : 관리자 프로덕션 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 09.
     * </pre>
     */
    public AdminProductionDTO insertProduction(AdminProductionEntity adminProductionEntity) {
        em.persist(adminProductionEntity);
        return toDto(adminProductionEntity);
    }

    /**
     * <pre>
     * 1. MethodName : insertProductionImage
     * 2. ClassName  : AdminProductionJpaRepository.java
     * 3. Comment    : 관리자 프로덕션 이미지 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 14.
     * </pre>
     */
    @Modifying(clearAutomatically = true)
    public Long insertProductionImage(CommonImageEntity commonImageEntity) {
        em.persist(commonImageEntity);
        return commonImageEntity.getIdx();
    }

    /**
     * <pre>
     * 1. MethodName : updateProductionByEm
     * 2. ClassName  : AdminProductionJpaRepository.java
     * 3. Comment    : 관리자 프로덕션 수정 by entityManager
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 09.
     * </pre>
     */
    public AdminProductionDTO updateProductionByEm(AdminProductionEntity existAdminProductionEntity) {
        em.merge(existAdminProductionEntity);
        em.flush();
        em.clear();
        return toDto(existAdminProductionEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deleteProductionByEm
     * 2. ClassName  : AdminProductionJpaRepository.java
     * 3. Comment    : 관리자 프로덕션 삭제 by entityManager
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 09.
     * </pre>
     */
    public Long deleteProductionByEm(Long idx) {
        em.remove(em.find(AdminProductionEntity.class, idx));
        em.flush();
        em.clear();
        return idx;
    }

    /**
     * <pre>
     * 1. MethodName : findProductionAdminComment
     * 2. ClassName  : AdminProductionJpaRepository.java
     * 3. Comment    : 관리자 프로덕션 어드민 코멘트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 26.
     * </pre>
     */
    public List<AdminCommentDTO> findProductionAdminComment(Long idx) {
        List<AdminCommentEntity> adminCommentEntity = queryFactory
                .selectFrom(QAdminCommentEntity.adminCommentEntity)
                .where(QAdminCommentEntity.adminCommentEntity.commentType.eq("production")
                        .and(QAdminCommentEntity.adminCommentEntity.idx.eq(idx))
                        .and(QAdminCommentEntity.adminCommentEntity.visible.eq("Y")))
                .fetch();

        return adminCommentEntity != null ? toDtoList(adminCommentEntity) : emptyList();
    }
}
