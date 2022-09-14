package com.tsp.new_tsp_admin.api.portfolio.service;

import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentDTO;
import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioDTO;
import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioEntity;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_admin.exception.ApiExceptionType.*;

@Service
@RequiredArgsConstructor
public class AdminPortfolioJpaServiceImpl implements AdminPortfolioJpaService {
    private final AdminPortfolioJpaRepository adminPortfolioJpaRepository;

    /**
     * <pre>
     * 1. MethodName : findPortfoliosCount
     * 2. ClassName  : AdminPortfolioJpaServiceImpl.java
     * 3. Comment    : 관리자 포트폴리오 리스트 수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 14.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public Integer findPortfoliosCount(Map<String, Object> portfolioMap) throws TspException {
        try {
            return adminPortfolioJpaRepository.findPortfoliosCount(portfolioMap);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_PORTFOLIO_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findPortfoliosList
     * 2. ClassName  : AdminPortfolioJpaServiceImpl.java
     * 3. Comment    : 관리자 포트폴리오 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 14.
     * </pre>
     */
    @Override
    @Cacheable("portfolio")
    @Transactional(readOnly = true)
    public List<AdminPortFolioDTO> findPortfoliosList(Map<String, Object> portfolioMap) throws TspException {
        try {
            return adminPortfolioJpaRepository.findPortfoliosList(portfolioMap);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_PORTFOLIO_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findOnePortfolio
     * 2. ClassName  : AdminPortfolioJpaServiceImpl.java
     * 3. Comment    : 관리자 포트폴리오 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 18.
     * </pre>
     */
    @Override
    @Cacheable("portfolio")
    @Transactional(readOnly = true)
    public AdminPortFolioDTO findOnePortfolio(AdminPortFolioEntity adminPortFolioEntity) throws TspException {
        try {
            return adminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_PORTFOLIO, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOnePortfolio
     * 2. ClassName  : AdminPortfolioJpaServiceImpl.java
     * 3. Comment    : 관리자 이전 포트폴리오 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 14.
     * </pre>
     */
    @Override
    @Cacheable("portfolio")
    @Transactional(readOnly = true)
    public AdminPortFolioDTO findPrevOnePortfolio(AdminPortFolioEntity adminPortFolioEntity) throws TspException {
        try {
            return adminPortfolioJpaRepository.findPrevOnePortfolio(adminPortFolioEntity);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_PORTFOLIO, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findNextOnePortfolio
     * 2. ClassName  : AdminPortfolioJpaServiceImpl.java
     * 3. Comment    : 관리자 다음 포트폴리오 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 14.
     * </pre>
     */
    @Override
    @Cacheable("portfolio")
    @Transactional(readOnly = true)
    public AdminPortFolioDTO findNextOnePortfolio(AdminPortFolioEntity adminPortFolioEntity) throws TspException {
        try {
            return adminPortfolioJpaRepository.findNextOnePortfolio(adminPortFolioEntity);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_PORTFOLIO, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : insertPortfolio
     * 2. ClassName  : AdminPortfolioJpaServiceImpl.java
     * 3. Comment    : 관리자 포트폴리오 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 18.
     * </pre>
     */
    @Override
    @CachePut("portfolio")
    @Modifying(clearAutomatically = true)
    @Transactional
    public AdminPortFolioDTO insertPortfolio(AdminPortFolioEntity adminPortFolioEntity) throws TspException {
        try {
            return adminPortfolioJpaRepository.insertPortfolio(adminPortFolioEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_PORTFOLIO, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updatePortfolio
     * 2. ClassName  : AdminPortfolioJpaServiceImpl.java
     * 3. Comment    : 관리자 포트폴리오 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 18.
     * </pre>
     */
    @Override
    @CachePut("portfolio")
    @Modifying(clearAutomatically = true)
    @Transactional
    public AdminPortFolioDTO updatePortfolio(AdminPortFolioEntity adminPortFolioEntity) throws TspException {
        try {
            return adminPortfolioJpaRepository.updatePortfolio(adminPortFolioEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_UPDATE_PORTFOLIO, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deletePortfolio
     * 2. ClassName  : AdminPortfolioJpaServiceImpl.java
     * 3. Comment    : 관리자 포트폴리오 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 18.
     * </pre>
     */
    @Override
    @CacheEvict("portfolio")
    @Modifying(clearAutomatically = true)
    @Transactional
    public Integer deletePortfolio(Integer idx) throws TspException {
        try {
            return adminPortfolioJpaRepository.deletePortfolio(idx);
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_PORTFOLIO, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findPortfolioAdminComment
     * 2. ClassName  : AdminPortfolioJpaServiceImpl.java
     * 3. Comment    : 관리자 포트폴리오 어드민 코멘트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 26.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public List<AdminCommentDTO> findPortfolioAdminComment(AdminPortFolioEntity adminPortfolioEntity) throws TspException{
        try {
            return adminPortfolioJpaRepository.findPortfolioAdminComment(adminPortfolioEntity);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_COMMENT_LIST, e);
        }
    }
}
