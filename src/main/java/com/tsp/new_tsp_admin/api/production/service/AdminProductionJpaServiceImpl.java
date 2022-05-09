package com.tsp.new_tsp_admin.api.production.service;

import com.tsp.new_tsp_admin.api.domain.production.AdminProductionDTO;
import com.tsp.new_tsp_admin.api.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminProductionJpaServiceImpl implements AdminProductionJpaService {

    private final AdminProductionJpaRepository adminProductionJpaRepository;
    private final ImageService imageService;

    /**
     * <pre>
     * 1. MethodName : findProductionsCount
     * 2. ClassName  : AdminProductionJpaServiceImpl.java
     * 3. Comment    : 관리자 프로덕션 리스트 수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 09.
     * </pre>
     *
     * @param productionMap
     */
    @Override
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
     * @param productionMap
     */
    @Override
    public List<AdminProductionDTO> findProductionsList(Map<String, Object> productionMap) {
        return adminProductionJpaRepository.findProductionsList(productionMap);
    }
}
