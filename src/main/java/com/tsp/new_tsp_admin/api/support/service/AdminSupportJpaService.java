package com.tsp.new_tsp_admin.api.support.service;

import com.tsp.new_tsp_admin.api.domain.support.AdminSupportDTO;
import com.tsp.new_tsp_admin.api.domain.support.AdminSupportEntity;
import com.tsp.new_tsp_admin.exception.TspException;

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
    Integer findSupportsCount(Map<String, Object> supportMap) throws TspException;

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
    List<AdminSupportDTO> findSupportsList(Map<String, Object> supportMap) throws TspException;

    /**
     * <pre>
     * 1. MethodName : findOneSupportModel
     * 2. ClassName  : AdminSupportJpaService.java
     * 3. Comment    : 관리자 지원모델 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     */
    AdminSupportDTO findOneSupportModel(AdminSupportEntity adminSupportEntity) throws TspException;

    /**
     * <pre>
     * 1. MethodName : insertSupportModel
     * 2. ClassName  : AdminSupportJpaService.java
     * 3. Comment    : 관리자 지원모델 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     */
    AdminSupportDTO insertSupportModel(AdminSupportEntity adminSupportEntity) throws TspException;

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
    AdminSupportDTO updateSupportModel(AdminSupportEntity adminSupportEntity) throws TspException;

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
    Integer deleteSupportModel(Integer idx) throws TspException;
}
