package com.tsp.new_tsp_admin.api.portfolio.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.common.EntityType;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioDTO;
import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioEntity;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.*;

import static com.tsp.new_tsp_admin.api.domain.common.QCommonImageEntity.commonImageEntity;
import static com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioEntity.*;
import static com.tsp.new_tsp_admin.api.domain.portfolio.QAdminPortFolioEntity.adminPortFolioEntity;
import static com.tsp.new_tsp_admin.api.domain.production.QAdminProductionEntity.adminProductionEntity;
import static com.tsp.new_tsp_admin.common.StringUtil.getString;
import static com.tsp.new_tsp_admin.exception.ApiExceptionType.*;
import static java.util.Collections.emptyList;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AdminPortfolioJpaQueryRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private BooleanExpression searchPortfolio(Map<String, Object> portfolioMap) {
        String searchType = getString(portfolioMap.get("searchType"), "");
        String searchKeyword = getString(portfolioMap.get("searchKeyword"), "");

        if (!Objects.equals(searchKeyword, "")) {
            return "0".equals(searchType) ?
                    adminPortFolioEntity.title.contains(searchKeyword)
                            .or(adminPortFolioEntity.description.contains(searchKeyword)) :
                    "1".equals(searchType) ?
                            adminPortFolioEntity.title.contains(searchKeyword) :
                            adminPortFolioEntity.description.contains(searchKeyword);
        } else {
            return null;
        }
    }

    /**
     * <pre>
     * 1. MethodName : findPortfolioList
     * 2. ClassName  : AdminPortfolioJpaRepository.java
     * 3. Comment    : 관리자 포트폴리오 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 13.
     * </pre>
     */
    public Page<AdminPortFolioDTO> findPortfolioList(Map<String, Object> portfolioMap, PageRequest pageRequest) {
        List<AdminPortFolioEntity> portfolioList = queryFactory
                .selectFrom(adminPortFolioEntity)
                .orderBy(adminPortFolioEntity.idx.desc())
                .where(searchPortfolio(portfolioMap))
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        return new PageImpl<>(AdminPortFolioEntity.toDtoList(portfolioList), pageRequest, portfolioList.size());
    }

    /**
     * <pre>
     * 1. MethodName : findOnePortfolio
     * 2. ClassName  : AdminPortfolioJpaRepository.java
     * 3. Comment    : 관리자 포트폴리오 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 13.
     * </pre>
     */
    public AdminPortFolioDTO findOnePortfolio(Long idx) {
        // 포트폴리오 상세 조회
        AdminPortFolioEntity findOnePortfolio = Optional.ofNullable(queryFactory
                .selectFrom(adminPortFolioEntity)
                .leftJoin(adminPortFolioEntity.commonImageEntityList, commonImageEntity)
                .fetchJoin()
                .where(adminPortFolioEntity.idx.eq(idx)
                        .and(adminPortFolioEntity.visible.eq("Y")
                                .and(commonImageEntity.typeName.eq(EntityType.PORTFOLIO))))
                .fetchOne()).orElseThrow(() -> new TspException(NOT_FOUND_PORTFOLIO));

        return toDto(findOnePortfolio);
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOnePortfolio
     * 2. ClassName  : AdminPortfolioJpaRepository.java
     * 3. Comment    : 관리자 이전 포트폴리오 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 14.
     * </pre>
     */
    public AdminPortFolioDTO findPrevOnePortfolio(Long idx) {
        // 이전 포트폴리오 조회
        AdminPortFolioEntity findPrevOnePortfolio = Optional.ofNullable(queryFactory
                .selectFrom(adminPortFolioEntity)
                .orderBy(adminPortFolioEntity.idx.desc())
                .where(adminPortFolioEntity.idx.lt(idx)
                        .and(adminProductionEntity.visible.eq("Y")))
                .fetchFirst()).orElseThrow(() -> new TspException(NOT_FOUND_PORTFOLIO));

        return toDto(findPrevOnePortfolio);
    }

    /**
     * <pre>
     * 1. MethodName : findNextOnePortfolio
     * 2. ClassName  : AdminPortfolioJpaRepository.java
     * 3. Comment    : 관리자 다음 포트폴리오 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 14.
     * </pre>
     */
    public AdminPortFolioDTO findNextOnePortfolio(Long idx) {
        // 다음 포트폴리오 조회
        AdminPortFolioEntity findPrevOnePortfolio = Optional.ofNullable(queryFactory
                .selectFrom(adminPortFolioEntity)
                .orderBy(adminPortFolioEntity.idx.desc())
                .where(adminPortFolioEntity.idx.gt(idx)
                        .and(adminProductionEntity.visible.eq("Y")))
                .fetchFirst()).orElseThrow(() -> new TspException(NOT_FOUND_PORTFOLIO));

        return toDto(findPrevOnePortfolio);
    }

    /**
     * <pre>
     * 1. MethodName : insertPortfolioImage
     * 2. ClassName  : AdminPortfolioJpaRepository.java
     * 3. Comment    : 관리자 포트폴리오 이미지 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 14.
     * </pre>
     */
    public Long insertPortfolioImage(CommonImageEntity commonImageEntity) {
        em.persist(commonImageEntity);
        return commonImageEntity.getIdx();
    }
}
