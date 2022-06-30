package com.tsp.new_tsp_admin.api.portfolio.service;

import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioDTO;
import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
     *
     */
    @Override
    @Cacheable("portfolio")
    public Long findPortfoliosCount(Map<String, Object> portfolioMap) {
        return adminPortfolioJpaRepository.findPortfoliosCount(portfolioMap);
    }

    /**
     * <pre>
     * 1. MethodName : findPortfoliosList
     * 2. ClassName  : AdminPortfolioJpaServiceImpl.java
     * 3. Comment    : 관리자 포트폴리오 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 14.
     * </pre>
     *
     */
    @Override
    @Cacheable("portfolio")
    public List<AdminPortFolioDTO> findPortfoliosList(Map<String, Object> portfolioMap) {
        return adminPortfolioJpaRepository.findPortfoliosList(portfolioMap);
    }

    /**
     * <pre>
     * 1. MethodName : findOnePortfolio
     * 2. ClassName  : AdminPortfolioJpaServiceImpl.java
     * 3. Comment    : 관리자 포트폴리오 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 18.
     * </pre>
     *
     */
    @Override
    @Cacheable("portfolio")
    public AdminPortFolioDTO findOnePortfolio(AdminPortFolioEntity adminPortFolioEntity) {
        return adminPortfolioJpaRepository.findOnePortfolio(adminPortFolioEntity);
    }

    /**
     * <pre>
     * 1. MethodName : insertPortfolio
     * 2. ClassName  : AdminPortfolioJpaServiceImpl.java
     * 3. Comment    : 관리자 포트폴리오 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 18.
     * </pre>
     *
     */
    @Override
    @CachePut("portfolio")
    public AdminPortFolioDTO insertPortfolio(AdminPortFolioEntity adminPortFolioEntity) {
        return adminPortfolioJpaRepository.insertPortfolio(adminPortFolioEntity);
    }

    /**
     * <pre>
     * 1. MethodName : updatePortfolio
     * 2. ClassName  : AdminPortfolioJpaServiceImpl.java
     * 3. Comment    : 관리자 포트폴리오 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 18.
     * </pre>
     *
     */
    @Override
    @CachePut("portfolio")
    public AdminPortFolioDTO updatePortfolio(AdminPortFolioEntity adminPortFolioEntity) {
        return adminPortfolioJpaRepository.updatePortfolio(adminPortFolioEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deletePortfolio
     * 2. ClassName  : AdminPortfolioJpaServiceImpl.java
     * 3. Comment    : 관리자 포트폴리오 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 18.
     * </pre>
     *
     */
    @Override
    @CacheEvict("portfolio")
    public AdminPortFolioDTO deletePortfolio(AdminPortFolioEntity adminPortFolioEntity) {
        return adminPortfolioJpaRepository.deletePortfolio(adminPortFolioEntity);
    }
}
