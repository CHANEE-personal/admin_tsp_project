package com.tsp.new_tsp_admin.api.common.service;

import com.tsp.new_tsp_admin.api.domain.common.CommonCodeDTO;
import com.tsp.new_tsp_admin.api.domain.common.CommonCodeEntity;

import java.util.List;
import java.util.Map;

public interface AdminCommonJpaService {

    /**
     * <pre>
     * 1. MethodName : findCommonCodeListCount
     * 2. ClassName  : AdminCommonJpaService.java
     * 3. Comment    : 관리자 공통 코드 리스트 수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    int findCommonCodeListCount(Map<String, Object> commonMap);

    /**
     * <pre>
     * 1. MethodName : findCommonCodeList
     * 2. ClassName  : AdminCommonJpaService.java
     * 3. Comment    : 관리자 공통 코드 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    List<CommonCodeDTO> findCommonCodeList(Map<String, Object> commonMap);

    /**
     * <pre>
     * 1. MethodName : findOneCommonCode
     * 2. ClassName  : AdminCommonJpaService.java
     * 3. Comment    : 관리자 공통 코드 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    CommonCodeDTO findOneCommonCode(Long idx);

    /**
     * <pre>
     * 1. MethodName : insertCommonCode
     * 2. ClassName  : AdminCommonJpaService.java
     * 3. Comment    : 관리자 공통 코드 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    CommonCodeDTO insertCommonCode(CommonCodeEntity commonCodeEntity);

    /**
     * <pre>
     * 1. MethodName : updateCommonCode
     * 2. ClassName  : AdminCommonJpaService.java
     * 3. Comment    : 관리자 공통 코드 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    CommonCodeDTO updateCommonCode(CommonCodeEntity commonCodeEntity);

    /**
     * <pre>
     * 1. MethodName : deleteCommonCode
     * 2. ClassName  : AdminModelJpaService.java
     * 3. Comment    : 관리자 공통 코드 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    Long deleteCommonCode(Long idx);
}
