package com.tsp.new_tsp_admin.api.model.service.negotiation;

import com.tsp.new_tsp_admin.api.domain.model.AdminModelDTO;
import com.tsp.new_tsp_admin.api.domain.model.negotiation.AdminNegotiationDTO;
import com.tsp.new_tsp_admin.api.domain.model.negotiation.AdminNegotiationEntity;

import java.util.List;
import java.util.Map;

public interface AdminNegotiationJpaService {
    /**
     * <pre>
     * 1. MethodName : findNegotiationCount
     * 2. ClassName  : AdminNegotiationJpaService.java
     * 3. Comment    : 관리자 모델 섭외 리스트 수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 09.
     * </pre>
     */
    Integer findNegotiationCount(Map<String, Object> negotiationMap) throws Exception;

    /**
     * <pre>
     * 1. MethodName : findModelNegotiationList
     * 2. ClassName  : AdminNegotiationJpaService.java
     * 3. Comment    : 관리자 모델 섭외 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 09.
     * </pre>
     */
    List<AdminModelDTO> findModelNegotiationList(Map<String, Object> negotiationMap) throws Exception;

    /**
     * <pre>
     * 1. MethodName : findOneNegotiation
     * 2. ClassName  : AdminNegotiationJpaService.java
     * 3. Comment    : 관리자 모델 섭외 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 09.
     * </pre>
     */
    AdminNegotiationDTO findOneNegotiation(AdminNegotiationEntity adminNegotiationEntity) throws Exception;

    /**
     * <pre>
     * 1. MethodName : insertModelNegotiation
     * 2. ClassName  : AdminNegotiationJpaService.java
     * 3. Comment    : 관리자 모델 섭외 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 09.
     * </pre>
     */
    AdminNegotiationDTO insertModelNegotiation(AdminNegotiationEntity adminNegotiationEntity) throws Exception;

    /**
     * <pre>
     * 1. MethodName : updateModelNegotiation
     * 2. ClassName  : AdminNegotiationJpaService.java
     * 3. Comment    : 관리자 모델 섭외 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 09.
     * </pre>
     */
    AdminNegotiationDTO updateModelNegotiation(AdminNegotiationEntity adminNegotiationEntity) throws Exception;

    /**
     * <pre>
     * 1. MethodName : deleteModelNegotiation
     * 2. ClassName  : AdminNegotiationJpaService.java
     * 3. Comment    : 관리자 모델 섭외 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 09.
     * </pre>
     */
    Integer deleteModelNegotiation(Integer idx) throws Exception;
}