package com.tsp.new_tsp_admin.api.portfolio.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioDTO;
import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioEntity;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_admin.api.domain.common.QCommonImageEntity.commonImageEntity;
import static com.tsp.new_tsp_admin.api.domain.portfolio.QAdminPortFolioEntity.adminPortFolioEntity;
import static com.tsp.new_tsp_admin.api.portfolio.mapper.PortFolioMapper.*;
import static com.tsp.new_tsp_admin.common.StringUtil.getInt;
import static com.tsp.new_tsp_admin.common.StringUtil.getString;
import static com.tsp.new_tsp_admin.exception.ApiExceptionType.*;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AdminPortfolioJpaRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private BooleanExpression searchPortfolio(Map<String, Object> portfolioMap) {
        String searchType = getString(portfolioMap.get("searchType"), "");
        String searchKeyword = getString(portfolioMap.get("searchKeyword"), "");

        if ("0".equals(searchType)) {
            return adminPortFolioEntity.title.contains(searchKeyword)
                    .or(adminPortFolioEntity.description.contains(searchKeyword));
        } else if ("1".equals(searchType)) {
            return adminPortFolioEntity.title.contains(searchKeyword);
        } else {
            return adminPortFolioEntity.description.contains(searchKeyword);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findPortfoliosCount
     * 2. ClassName  : AdminPortfolioJpaRepository.java
     * 3. Comment    : ????????? ??????????????? ????????? ?????? ??????
     * 4. ?????????       : CHO
     * 5. ?????????       : 2022. 05. 12.
     * </pre>
     */
    public Integer findPortfoliosCount(Map<String, Object> portfolioMap) {
        return queryFactory.selectFrom(adminPortFolioEntity).where(searchPortfolio(portfolioMap)).fetch().size();
    }

    /**
     * <pre>
     * 1. MethodName : findPortfoliosList
     * 2. ClassName  : AdminPortfolioJpaRepository.java
     * 3. Comment    : ????????? ??????????????? ????????? ??????
     * 4. ?????????       : CHO
     * 5. ?????????       : 2022. 05. 13.
     * </pre>
     */
    public List<AdminPortFolioDTO> findPortfoliosList(Map<String, Object> portfolioMap) {
        List<AdminPortFolioEntity> portfolioList = queryFactory
                .selectFrom(adminPortFolioEntity)
                .orderBy(adminPortFolioEntity.idx.desc())
                .where(searchPortfolio(portfolioMap))
                .offset(getInt(portfolioMap.get("jpaStartPage"), 0))
                .limit(getInt(portfolioMap.get("size"), 0))
                .fetch();

        portfolioList.forEach(list -> portfolioList.get(portfolioList.indexOf(list))
                .setRnum(getInt(portfolioMap.get("startPage"), 1) * (getInt(portfolioMap.get("size"), 1)) - (2 - portfolioList.indexOf(list))));

        return INSTANCE.toDtoList(portfolioList);
    }

    /**
     * <pre>
     * 1. MethodName : findOnePortfolio
     * 2. ClassName  : AdminPortfolioJpaRepository.java
     * 3. Comment    : ????????? ??????????????? ?????? ??????
     * 4. ?????????       : CHO
     * 5. ?????????       : 2022. 05. 13.
     * </pre>
     */
    public AdminPortFolioDTO findOnePortfolio(AdminPortFolioEntity existAdminPortfolioEntity) {
        // ??????????????? ?????? ??????
        AdminPortFolioEntity findOnePortfolio = queryFactory
                .selectFrom(adminPortFolioEntity)
                .leftJoin(adminPortFolioEntity.commonImageEntityList, commonImageEntity)
                .fetchJoin()
                .where(adminPortFolioEntity.idx.eq(existAdminPortfolioEntity.getIdx())
                        .and(adminPortFolioEntity.visible.eq("Y")
                                .and(commonImageEntity.typeName.eq("portfolio"))))
                .fetchOne();

        return INSTANCE.toDto(findOnePortfolio);
    }

    /**
     * <pre>
     * 1. MethodName : insertPortfolio
     * 2. ClassName  : AdminPortfolioJpaRepository.java
     * 3. Comment    : ????????? ??????????????? ??????
     * 4. ?????????       : CHO
     * 5. ?????????       : 2022. 05. 13.
     * </pre>
     */
    public AdminPortFolioDTO insertPortfolio(AdminPortFolioEntity adminPortfolioEntity) {
        em.persist(adminPortfolioEntity);
        return INSTANCE.toDto(adminPortfolioEntity);
    }

    /**
     * <pre>
     * 1. MethodName : insertPortfolioImage
     * 2. ClassName  : AdminPortfolioJpaRepository.java
     * 3. Comment    : ????????? ??????????????? ????????? ??????
     * 4. ?????????       : CHO
     * 5. ?????????       : 2022. 05. 14.
     * </pre>
     */
    public Integer insertPortfolioImage(CommonImageEntity commonImageEntity) throws TspException {
        try {
            em.persist(commonImageEntity);

            return commonImageEntity.getIdx();
        } catch (Exception e) {
            throw new TspException(ERROR_PORTFOLIO, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updatePortfolio
     * 2. ClassName  : AdminPortfolioJpaRepository.java
     * 3. Comment    : ????????? ??????????????? ??????
     * 4. ?????????       : CHO
     * 5. ?????????       : 2022. 05. 13.
     * </pre>
     */
    public AdminPortFolioDTO updatePortfolio(AdminPortFolioEntity existAdminPortfolioEntity) {
        em.merge(existAdminPortfolioEntity);
        em.flush();
        em.clear();

        return INSTANCE.toDto(existAdminPortfolioEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deletePortfolio
     * 2. ClassName  : AdminPortfolioJpaRepository.java
     * 3. Comment    : ????????? ??????????????? ??????
     * 4. ?????????       : CHO
     * 5. ?????????       : 2022. 05. 14.
     * </pre>
     */
    public Integer deletePortfolio(Integer idx) {
        em.remove(em.find(AdminPortFolioEntity.class, idx));
        em.flush();
        em.clear();

        return idx;
    }
}
