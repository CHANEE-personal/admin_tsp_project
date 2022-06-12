package com.tsp.new_tsp_admin.api.support.service;

import com.tsp.new_tsp_admin.api.domain.model.AdminModelDTO;
import com.tsp.new_tsp_admin.api.domain.support.AdminSupportDTO;
import com.tsp.new_tsp_admin.api.domain.support.AdminSupportEntity;

import java.util.List;
import java.util.Map;

public interface AdminSupportJpaService {

    /**
     * <pre>
     * 1. MethodName : findSupportsCount
     * 2. ClassName  : AdminSupportJpaService.java
     * 3. Comment    : 관리자 지원모델 리스트 수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     */
    Long findSupportsCount(Map<String, Object> modelMap) throws Exception;

    /**
     * <pre>
     * 1. MethodName : findSupportsList
     * 2. ClassName  : AdminSupportJpaService.java
     * 3. Comment    : 관리자 지원모델 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     */
    List<AdminSupportDTO> findSupportsList(Map<String, Object> modelMap) throws Exception;

    /**
     * <pre>
     * 1. MethodName : updateSupportModel
     * 2. ClassName  : AdminSupportJpaService.java
     * 3. Comment    : 관리자 지원모델 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     */
    AdminSupportDTO updateSupportModel(AdminSupportEntity adminSupportEntity) throws Exception;

    /**
     * <pre>
     * 1. MethodName : deleteSupportModel
     * 2. ClassName  : AdminSupportJpaService.java
     * 3. Comment    : 관리자 지원모델 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     */
    AdminSupportDTO deleteSupportModel(AdminSupportEntity adminSupportEntity) throws Exception;
}
