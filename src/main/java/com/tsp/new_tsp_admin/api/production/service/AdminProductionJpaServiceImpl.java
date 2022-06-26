package com.tsp.new_tsp_admin.api.production.service;

import com.tsp.new_tsp_admin.api.domain.production.AdminProductionDTO;
import com.tsp.new_tsp_admin.api.domain.production.AdminProductionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminProductionJpaServiceImpl implements AdminProductionJpaService {

    private final AdminProductionJpaRepository adminProductionJpaRepository;

    /**
     * <pre>
     * 1. MethodName : findProductionsCount
     * 2. ClassName  : AdminProductionJpaServiceImpl.java
     * 3. Comment    : 관리자 프로덕션 리스트 수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 09.
     * </pre>
     *
     */
    @Override
    @Cacheable("production")
    public Long findProductionsCount(Map<String, Object> productionMap) {
        return adminProductionJpaRepository.findProductionsCount(productionMap);
    }

    /**
     * <pre>
     * 1. MethodName : findProductionsList
     * 2. ClassName  : AdminProductionJpaServiceImpl.java
     * 3. Comment    : 관리자 프로덕션 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 09.
     * </pre>
     *
     */
    @Override
//    @Cacheable("production")
    public List<AdminProductionDTO> findProductionsList(Map<String, Object> productionMap) {
        return adminProductionJpaRepository.findProductionsList(productionMap);
    }

    /**
     * <pre>
     * 1. MethodName : findOneProduction
     * 2. ClassName  : AdminProductionJpaServiceImpl.java
     * 3. Comment    : 관리자 프로덕션 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 15.
     * </pre>
     *
     */
    @Override
//    @Cacheable("production")
    public AdminProductionDTO findOneProduction(AdminProductionEntity adminProductionEntity) {
        return adminProductionJpaRepository.findOneProduction(adminProductionEntity);
    }

    /**
     * <pre>
     * 1. MethodName : insertProduction
     * 2. ClassName  : AdminProductionJpaServiceImpl.java
     * 3. Comment    : 관리자 프로덕션 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 16.
     * </pre>
     *
     */
    @Override
    @CachePut("production")
    public AdminProductionDTO insertProduction(AdminProductionEntity adminProductionEntity) {
        return adminProductionJpaRepository.insertProduction(adminProductionEntity);
    }

    /**
     * <pre>
     * 1. MethodName : updateProduction
     * 2. ClassName  : AdminProductionJpaServiceImpl.java
     * 3. Comment    : 관리자 프로덕션 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 16.
     * </pre>
     *
     */
    @Override
    @CachePut("production")
    public AdminProductionDTO updateProduction(AdminProductionEntity adminProductionEntity) {
        return adminProductionJpaRepository.updateProductionByEm(adminProductionEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deleteProduction
     * 2. ClassName  : AdminProductionJpaServiceImpl.java
     * 3. Comment    : 관리자 프로덕션 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 17.
     * </pre>
     *
     */
    @Override
    @CacheEvict("production")
    public AdminProductionDTO deleteProduction(AdminProductionEntity adminProductionEntity) {
        return adminProductionJpaRepository.deleteProductionByEm(adminProductionEntity);
    }
}
