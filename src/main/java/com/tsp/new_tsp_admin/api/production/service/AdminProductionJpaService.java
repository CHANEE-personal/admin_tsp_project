package com.tsp.new_tsp_admin.api.production.service;

import com.tsp.new_tsp_admin.api.domain.production.AdminProductionDTO;
import com.tsp.new_tsp_admin.api.domain.production.AdminProductionEntity;

import java.util.List;
import java.util.Map;

public interface AdminProductionJpaService {

    /**
     * <pre>
     * 1. MethodName : findProductionsCount
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

    /**
     * <pre>
     * 1. MethodName : findOneProduction
     * 2. ClassName  : AdminProductionJpaService.java
     * 3. Comment    : 관리자 프로덕션 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 15.
     * </pre>
     *
     * @param adminProductionEntity
     */
    AdminProductionDTO findOneProduction(AdminProductionEntity adminProductionEntity);

    /**
     * <pre>
     * 1. MethodName : insertProduction
     * 2. ClassName  : AdminProductionJpaService.java
     * 3. Comment    : 관리자 프로덕션 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 14.
     * </pre>
     *
     * @param adminProductionEntity
     */
    Integer insertProduction(AdminProductionEntity adminProductionEntity);

    /**
     * <pre>
     * 1. MethodName : updateProduction
     * 2. ClassName  : AdminProductionJpaService.java
     * 3. Comment    : 관리자 프로덕션 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 16.
     * </pre>
     *
     * @param adminProductionEntity
     */
    AdminProductionDTO updateProduction(AdminProductionEntity adminProductionEntity);

    /**
     * <pre>
     * 1. MethodName : deleteProduction
     * 2. ClassName  : AdminProductionJpaService.java
     * 3. Comment    : 관리자 프로덕션 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 17.
     * </pre>
     *
     * @param adminProductionEntity
     */
    AdminProductionEntity deleteProduction(AdminProductionEntity adminProductionEntity);
}
