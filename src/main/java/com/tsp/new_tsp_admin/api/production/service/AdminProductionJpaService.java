package com.tsp.new_tsp_admin.api.production.service;

import com.tsp.new_tsp_admin.api.domain.production.AdminProductionDTO;

import java.util.List;
import java.util.Map;

public interface AdminProductionJpaService {

    /**
     * <pre>
     * 1. MethodName : findProductionCount
     * 2. ClassName  : AdminProductionJpaService.java
     * 3. Comment    : 관리자 프로덕션 리스트 수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 09.
     * </pre>
     *
     * @param productionMap
     */
    Long findProductionsCount(Map<String, Object> productionMap);

    /**
     * <pre>
     * 1. MethodName : findProductionsList
     * 2. ClassName  : AdminProductionJpaService.java
     * 3. Comment    : 관리자 프로덕션 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 09.
     * </pre>
     *
     * @param productionMap
     */
    List<AdminProductionDTO> findProductionsList(Map<String, Object> productionMap);
}
