package com.tsp.new_tsp_admin.api.portfolio.service;

import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioDTO;
import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioEntity;
import lombok.RequiredArgsConstructor;
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
     * @param portfolioMap
     */
    @Override
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
     * @param portfolioMap
     */
    @Override
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
     * @param adminPortFolioEntity
     */
    @Override
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
     * @param adminPortFolioEntity
     */
    @Override
    public Integer insertPortfolio(AdminPortFolioEntity adminPortFolioEntity) {
        return adminPortfolioJpaRepository.insertPortfolio(adminPortFolioEntity);
    }
}
