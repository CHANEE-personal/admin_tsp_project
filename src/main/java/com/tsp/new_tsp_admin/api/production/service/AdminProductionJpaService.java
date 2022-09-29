package com.tsp.new_tsp_admin.api.production.service;

import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentDTO;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelDTO;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
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
     */
    Integer findProductionsCount(Map<String, Object> productionMap) throws Exception;

    /**
     * <pre>
     * 1. MethodName : findProductionsList
     * 2. ClassName  : AdminProductionJpaService.java
     * 3. Comment    : 관리자 프로덕션 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 09.
     * </pre>
     */
    List<AdminProductionDTO> findProductionsList(Map<String, Object> productionMap) throws Exception;

    /**
     * <pre>
     * 1. MethodName : findOneProduction
     * 2. ClassName  : AdminProductionJpaService.java
     * 3. Comment    : 관리자 프로덕션 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 15.
     * </pre>
     */
    AdminProductionDTO findOneProduction(AdminProductionEntity adminProductionEntity) throws Exception;

    /**
     * <pre>
     * 1. MethodName : findPrevOneProduction
     * 2. ClassName  : AdminProductionJpaService.java
     * 3. Comment    : 관리자 이전 프로덕션 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 13.
     * </pre>
     */
    AdminProductionDTO findPrevOneProduction(AdminProductionEntity adminProductionEntity) throws Exception;

    /**
     * <pre>
     * 1. MethodName : findNextOneProduction
     * 2. ClassName  : AdminProductionJpaService.java
     * 3. Comment    : 관리자 다음 프로덕션 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 13.
     * </pre>
     */
    AdminProductionDTO findNextOneProduction(AdminProductionEntity adminProductionEntity) throws Exception;

    /**
     * <pre>
     * 1. MethodName : insertProduction
     * 2. ClassName  : AdminProductionJpaService.java
     * 3. Comment    : 관리자 프로덕션 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 16.
     * </pre>
     */
    AdminProductionDTO insertProduction(AdminProductionEntity adminProductionEntity) throws Exception;

    /**
     * <pre>
     * 1. MethodName : updateProduction
     * 2. ClassName  : AdminProductionJpaService.java
     * 3. Comment    : 관리자 프로덕션 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 16.
     * </pre>
     */
    AdminProductionDTO updateProduction(AdminProductionEntity adminProductionEntity) throws Exception;

    /**
     * <pre>
     * 1. MethodName : deleteProduction
     * 2. ClassName  : AdminProductionJpaService.java
     * 3. Comment    : 관리자 프로덕션 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 17.
     * </pre>
     */
    Long deleteProduction(Long idx) throws Exception;

    /**
     * <pre>
     * 1. MethodName : findProductionAdminComment
     * 2. ClassName  : AdminProductionJpaService.java
     * 3. Comment    : 관리자 프로덕션 어드민 코멘트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 26.
     * </pre>
     */
    List<AdminCommentDTO> findProductionAdminComment(AdminProductionEntity adminProductionEntity) throws Exception;
}
