package com.tsp.new_tsp_admin.api.common.service;

import com.tsp.new_tsp_admin.api.domain.common.CommonCodeDTO;

import java.util.List;
import java.util.Map;

public interface AdminCommonJpaService {

    /**
     * <pre>
     * 1. MethodName : commonCodeListCount
     * 2. ClassName  : AdminCommonJpaService.java
     * 3. Comment    : 관리자 공통 코드 리스트 수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    Integer commonCodeListCount(Map<String, Object> commonMap) throws Exception;

    /**
     * <pre>
     * 1. MethodName : commonCodeList
     * 2. ClassName  : AdminCommonJpaService.java
     * 3. Comment    : 관리자 공통 코드 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    List<CommonCodeDTO> commonCodeList(Map<String, Object> commonMap) throws Exception;
}
