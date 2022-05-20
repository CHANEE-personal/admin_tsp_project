package com.tsp.new_tsp_admin.api.portfolio.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioDTO;
import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioEntity;
import com.tsp.new_tsp_admin.api.portfolio.mapper.PortFolioMapper;
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
import static com.tsp.new_tsp_admin.api.domain.portfolio.QAdminPortFolioEntity.adminPortFolioEntity;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AdminPortfolioJpaRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private BooleanExpression searchPortfolio(Map<String, Object> portfolioMap) {
        String searchType = StringUtil.getString(portfolioMap.get("searchType"),"");
        String searchKeyword = StringUtil.getString(portfolioMap.get("searchKeyword"),"");

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
     * 3. Comment    : 관리자 포트폴리오 리스트 갯수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 12.
     * </pre>
     *
     * @param portfolioMap
     */
    public Long findPortfoliosCount(Map<String, Object> portfolioMap) {
        try {
            return queryFactory.selectFrom(adminPortFolioEntity)
                    .where(searchPortfolio(portfolioMap))
                    .fetchCount();
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.NOT_FOUND_PORTFOLIO_LIST);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findPortfoliosList
     * 2. ClassName  : AdminPortfolioJpaRepository.java
     * 3. Comment    : 관리자 포트폴리오 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 13.
     * </pre>
     *
     * @param portfolioMap
     */
    public List<AdminPortFolioDTO> findPortfoliosList(Map<String, Object> portfolioMap) {
        try {
            List<AdminPortFolioEntity> portfolioList =  queryFactory
                    .selectFrom(adminPortFolioEntity)
                    .orderBy(adminPortFolioEntity.idx.desc())
                    .where(searchPortfolio(portfolioMap))
                    .offset(StringUtil.getInt(portfolioMap.get("jpaStartPage"),0))
                    .limit(StringUtil.getInt(portfolioMap.get("size"),0))
                    .fetch();

            portfolioList.forEach(list -> portfolioList.get(portfolioList.indexOf(list)).setRnum(StringUtil.getInt(portfolioMap.get("startPage"),1)*(StringUtil.getInt(portfolioMap.get("size"),1))-(2-portfolioList.indexOf(list))));

            return PortFolioMapper.INSTANCE.toDtoList(portfolioList);
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.NOT_FOUND_PORTFOLIO_LIST);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findOnePortfolio
     * 2. ClassName  : AdminPortfolioJpaRepository.java
     * 3. Comment    : 관리자 포트폴리오 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 13.
     * </pre>
     *
     * @param existAdminPortfolioEntity
     */
    public AdminPortFolioDTO findOnePortfolio(AdminPortFolioEntity existAdminPortfolioEntity) {

        try {
            // 포트폴리오 상세 조회
            AdminPortFolioEntity findOnePortfolio = queryFactory
                    .selectFrom(adminPortFolioEntity)
                    .leftJoin(adminPortFolioEntity.commonImageEntityList, commonImageEntity)
                    .fetchJoin()
                    .where(adminPortFolioEntity.idx.eq(existAdminPortfolioEntity.getIdx())
                            .and(adminPortFolioEntity.visible.eq("Y")
                            .and(commonImageEntity.typeName.eq("portfolio"))))
                    .fetchOne();

            return PortFolioMapper.INSTANCE.toDto(findOnePortfolio);
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.NOT_FOUND_MODEL);
        }
    }

    /**
     * <pre>
     * 1. MethodName : insertPortfolio
     * 2. ClassName  : AdminPortfolioJpaRepository.java
     * 3. Comment    : 관리자 포트폴리오 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 13.
     * </pre>
     *
     * @param adminPortfolioEntity
     */
    @Transactional
    public AdminPortFolioDTO insertPortfolio(AdminPortFolioEntity adminPortfolioEntity) {
        try {
            em.persist(adminPortfolioEntity);

            return PortFolioMapper.INSTANCE.toDto(adminPortfolioEntity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TspException(ApiExceptionType.ERROR_PORTFOLIO);
        }
    }

    /**
     * <pre>
     * 1. MethodName : insertPortfolioImage
     * 2. ClassName  : AdminPortfolioJpaRepository.java
     * 3. Comment    : 관리자 포트폴리오 이미지 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 14.
     * </pre>
     *
     * @param commonImageEntity
     */
    @Transactional
    public Integer insertPortfolioImage(CommonImageEntity commonImageEntity) {
        try {
            em.persist(commonImageEntity);

            return commonImageEntity.getIdx();
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.ERROR_PORTFOLIO);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updatePortfolio
     * 2. ClassName  : AdminPortfolioJpaRepository.java
     * 3. Comment    : 관리자 포트폴리오 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 13.
     * </pre>
     *
     * @param existAdminPortfolioEntity
     */
    @Modifying
    @Transactional
    public AdminPortFolioDTO updatePortfolio(AdminPortFolioEntity existAdminPortfolioEntity) {
        try{
            em.merge(existAdminPortfolioEntity);
            em.flush();
            em.clear();
            return PortFolioMapper.INSTANCE.toDto(existAdminPortfolioEntity);
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.ERROR_UPDATE_PORTFOLIO);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deletePortfolio
     * 2. ClassName  : AdminPortfolioJpaRepository.java
     * 3. Comment    : 관리자 포트폴리오 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 14.
     * </pre>
     *
     * @param adminPortfolioEntity
     */
    public AdminPortFolioDTO deletePortfolio(AdminPortFolioEntity adminPortfolioEntity) {
        try {
            adminPortfolioEntity = em.find(AdminPortFolioEntity.class, adminPortfolioEntity.getIdx());
            em.remove(adminPortfolioEntity);
            em.flush();
            em.clear();

            return PortFolioMapper.INSTANCE.toDto(adminPortfolioEntity);
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.ERROR_DELETE_PORTFOLIO);
        }
    }
}
