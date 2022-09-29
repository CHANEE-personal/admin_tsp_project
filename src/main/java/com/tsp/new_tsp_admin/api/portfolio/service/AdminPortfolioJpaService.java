package com.tsp.new_tsp_admin.api.portfolio.service;

import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentDTO;
import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioDTO;
import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioEntity;

import java.util.List;
import java.util.Map;

public interface AdminPortfolioJpaService {

    /**
     * <pre>
     * 1. MethodName : findPortfoliosCount
     * 2. ClassName  : AdminPortfolioJpaService.java
     * 3. Comment    : 관리자 포트폴리오 리스트 수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 14.
     * </pre>
     */
    Integer findPortfoliosCount(Map<String, Object> portfolioMap) throws Exception;

    /**
     * <pre>
     * 1. MethodName : findPortfoliosList
     * 2. ClassName  : AdminPortfolioJpaService.java
     * 3. Comment    : 관리자 포트폴리오 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 14.
     * </pre>
     */
    List<AdminPortFolioDTO> findPortfoliosList(Map<String, Object> portfolioMap) throws Exception;

    /**
     * <pre>
     * 1. MethodName : findOnePortfolio
     * 2. ClassName  : AdminPortfolioJpaService.java
     * 3. Comment    : 관리자 포트폴리오 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 18.
     * </pre>
     */
    AdminPortFolioDTO findOnePortfolio(AdminPortFolioEntity adminPortFolioEntity) throws Exception;

    /**
     * <pre>
     * 1. MethodName : findPrevOnePortfolio
     * 2. ClassName  : AdminPortfolioJpaService.java
     * 3. Comment    : 관리자 이전 포트폴리오 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 14.
     * </pre>
     */
    AdminPortFolioDTO findPrevOnePortfolio(AdminPortFolioEntity adminPortFolioEntity) throws Exception;

    /**
     * <pre>
     * 1. MethodName : findNextOnePortfolio
     * 2. ClassName  : AdminPortfolioJpaService.java
     * 3. Comment    : 관리자 다음 포트폴리오 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 14.
     * </pre>
     */
    AdminPortFolioDTO findNextOnePortfolio(AdminPortFolioEntity adminPortFolioEntity) throws Exception;

    /**
     * <pre>
     * 1. MethodName : insertPortfolio
     * 2. ClassName  : AdminPortfolioJpaService.java
     * 3. Comment    : 관리자 포트폴리오 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 18.
     * </pre>
     */
    AdminPortFolioDTO insertPortfolio(AdminPortFolioEntity adminPortFolioEntity) throws Exception;

    /**
     * <pre>
     * 1. MethodName : updatePortfolio
     * 2. ClassName  : AdminPortfolioJpaService.java
     * 3. Comment    : 관리자 포트폴리오 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 18.
     * </pre>
     */
    AdminPortFolioDTO updatePortfolio(AdminPortFolioEntity adminPortFolioEntity) throws Exception;

    /**
     * <pre>
     * 1. MethodName : deletePortfolio
     * 2. ClassName  : AdminModeJpaService.java
     * 3. Comment    : 관리자 포트폴리오 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 17.
     * </pre>
     */
    Long deletePortfolio(Long idx) throws Exception;

    /**
     * <pre>
     * 1. MethodName : findPortfolioAdminComment
     * 2. ClassName  : AdminPortfolioJpaService.java
     * 3. Comment    : 관리자 포트폴리오 어드민 코멘트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 26.
     * </pre>
     */
    List<AdminCommentDTO> findPortfolioAdminComment(AdminPortFolioEntity adminPortfolioEntity) throws Exception;
}
