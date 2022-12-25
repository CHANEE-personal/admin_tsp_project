package com.tsp.new_tsp_admin.api.portfolio.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.common.EntityType;
import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentDTO;
import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentEntity;
import com.tsp.new_tsp_admin.api.domain.comment.QAdminCommentEntity;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioDTO;
import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioEntity;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_admin.api.domain.common.QCommonImageEntity.commonImageEntity;
import static com.tsp.new_tsp_admin.api.domain.portfolio.QAdminPortFolioEntity.adminPortFolioEntity;
import static com.tsp.new_tsp_admin.api.domain.production.QAdminProductionEntity.adminProductionEntity;
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
     * 1. MethodName : findPortfolioCount
     * 2. ClassName  : AdminPortfolioJpaRepository.java
     * 3. Comment    : 관리자 포트폴리오 리스트 갯수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 12.
     * </pre>
     */
    public int findPortfolioCount(Map<String, Object> portfolioMap) {
        return queryFactory.selectFrom(adminPortFolioEntity).where(searchPortfolio(portfolioMap)).fetch().size();
    }

    /**
     * <pre>
     * 1. MethodName : findPortfolioList
     * 2. ClassName  : AdminPortfolioJpaRepository.java
     * 3. Comment    : 관리자 포트폴리오 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 13.
     * </pre>
     */
    public List<AdminPortFolioDTO> findPortfolioList(Map<String, Object> portfolioMap) {
        List<AdminPortFolioEntity> portfolioList = queryFactory
                .selectFrom(adminPortFolioEntity)
                .orderBy(adminPortFolioEntity.idx.desc())
                .where(searchPortfolio(portfolioMap))
                .offset(getInt(portfolioMap.get("jpaStartPage"), 0))
                .limit(getInt(portfolioMap.get("size"), 0))
                .fetch();

        portfolioList.forEach(list -> portfolioList.get(portfolioList.indexOf(list))
                .setRowNum(getInt(portfolioMap.get("startPage"), 1) * (getInt(portfolioMap.get("size"), 1)) - (2 - portfolioList.indexOf(list))));

        return AdminPortFolioEntity.toDtoList(portfolioList);
    }

    /**
     * <pre>
     * 1. MethodName : findOnePortfolio
     * 2. ClassName  : AdminPortfolioJpaRepository.java
     * 3. Comment    : 관리자 포트폴리오 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 13.
     * </pre>
     */
    public AdminPortFolioDTO findOnePortfolio(Long idx) {
        // 포트폴리오 상세 조회
        AdminPortFolioEntity findOnePortfolio = queryFactory
                .selectFrom(adminPortFolioEntity)
                .leftJoin(adminPortFolioEntity.commonImageEntityList, commonImageEntity)
                .fetchJoin()
                .where(adminPortFolioEntity.idx.eq(idx)
                        .and(adminPortFolioEntity.visible.eq("Y")
                                .and(commonImageEntity.typeName.eq(EntityType.PORTFOLIO))))
                .fetchOne();

        assert findOnePortfolio != null;
        return AdminPortFolioEntity.toDto(findOnePortfolio);
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOnePortfolio
     * 2. ClassName  : AdminPortfolioJpaRepository.java
     * 3. Comment    : 관리자 이전 포트폴리오 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 14.
     * </pre>
     */
    public AdminPortFolioDTO findPrevOnePortfolio(Long idx) {
        // 이전 포트폴리오 조회
        AdminPortFolioEntity findPrevOnePortfolio = queryFactory
                .selectFrom(adminPortFolioEntity)
                .orderBy(adminPortFolioEntity.idx.desc())
                .where(adminPortFolioEntity.idx.lt(idx)
                        .and(adminProductionEntity.visible.eq("Y")))
                .fetchFirst();

        return AdminPortFolioEntity.toDto(findPrevOnePortfolio);
    }

    /**
     * <pre>
     * 1. MethodName : findNextOnePortfolio
     * 2. ClassName  : AdminPortfolioJpaRepository.java
     * 3. Comment    : 관리자 다음 포트폴리오 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 14.
     * </pre>
     */
    public AdminPortFolioDTO findNextOnePortfolio(Long idx) {
        // 다음 포트폴리오 조회
        AdminPortFolioEntity findPrevOnePortfolio = queryFactory
                .selectFrom(adminPortFolioEntity)
                .orderBy(adminPortFolioEntity.idx.desc())
                .where(adminPortFolioEntity.idx.gt(idx)
                        .and(adminProductionEntity.visible.eq("Y")))
                .fetchFirst();

        return AdminPortFolioEntity.toDto(findPrevOnePortfolio);
    }

    /**
     * <pre>
     * 1. MethodName : insertPortfolio
     * 2. ClassName  : AdminPortfolioJpaRepository.java
     * 3. Comment    : 관리자 포트폴리오 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 13.
     * </pre>
     */
    public AdminPortFolioDTO insertPortfolio(AdminPortFolioEntity adminPortfolioEntity) {
        em.persist(adminPortfolioEntity);
        return AdminPortFolioEntity.toDto(adminPortfolioEntity);
    }

    /**
     * <pre>
     * 1. MethodName : insertPortfolioImage
     * 2. ClassName  : AdminPortfolioJpaRepository.java
     * 3. Comment    : 관리자 포트폴리오 이미지 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 14.
     * </pre>
     */
    public Long insertPortfolioImage(CommonImageEntity commonImageEntity) {
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
     * 3. Comment    : 관리자 포트폴리오 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 13.
     * </pre>
     */
    public AdminPortFolioDTO updatePortfolio(AdminPortFolioEntity existAdminPortfolioEntity) {
        em.merge(existAdminPortfolioEntity);
        em.flush();
        em.clear();
        return AdminPortFolioEntity.toDto(existAdminPortfolioEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deletePortfolio
     * 2. ClassName  : AdminPortfolioJpaRepository.java
     * 3. Comment    : 관리자 포트폴리오 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 14.
     * </pre>
     */
    public Long deletePortfolio(Long idx) {
        em.remove(em.find(AdminPortFolioEntity.class, idx));
        em.flush();
        em.clear();
        return idx;
    }

    /**
     * <pre>
     * 1. MethodName : findPortfolioAdminComment
     * 2. ClassName  : AdminPortfolioJpaRepository.java
     * 3. Comment    : 관리자 포트폴리오 어드민 코멘트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 26.
     * </pre>
     */
    public List<AdminCommentDTO> findPortfolioAdminComment(Long idx) {
        List<AdminCommentEntity> adminCommentEntity = queryFactory
                .selectFrom(QAdminCommentEntity.adminCommentEntity)
                .where(QAdminCommentEntity.adminCommentEntity.commentType.eq("portfolio")
                        .and(QAdminCommentEntity.adminCommentEntity.idx.eq(idx))
                        .and(QAdminCommentEntity.adminCommentEntity.visible.eq("Y")))
                .fetch();

        return AdminCommentEntity.toDtoList(adminCommentEntity);
    }
}
