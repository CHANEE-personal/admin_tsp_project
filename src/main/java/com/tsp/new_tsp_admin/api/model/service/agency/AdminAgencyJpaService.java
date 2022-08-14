package com.tsp.new_tsp_admin.api.model.service.agency;

import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import com.tsp.new_tsp_admin.api.domain.model.agency.AdminAgencyDTO;
import com.tsp.new_tsp_admin.api.domain.model.agency.AdminAgencyEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface AdminAgencyJpaService {

    /**
     * <pre>
     * 1. MethodName : findAgencyCount
     * 2. ClassName  : AdminAgencyJpaService.java
     * 3. Comment    : 관리자 소속사 리스트 수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 14.
     * </pre>
     */
    Integer findAgencyCount(Map<String, Object> agencyMap) throws Exception;

    /**
     * <pre>
     * 1. MethodName : findAgencyList
     * 2. ClassName  : AdminAgencyJpaService.java
     * 3. Comment    : 관리자 소속사 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 14.
     * </pre>
     */
    List<AdminAgencyDTO> findAgencyList(Map<String, Object> agencyMap) throws Exception;

    /**
     * <pre>
     * 1. MethodName : findOneAgency
     * 2. ClassName  : AdminAgencyJpaService.java
     * 3. Comment    : 관리자 소속사 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 14.
     * </pre>
     */
    AdminAgencyDTO findOneAgency(AdminAgencyEntity adminAgencyEntity) throws Exception;

    /**
     * <pre>
     * 1. MethodName : insertAgency
     * 2. ClassName  : AdminAgencyJpaService.java
     * 3. Comment    : 관리자 소속사 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 14.
     * </pre>
     */
    AdminAgencyDTO insertAgency(AdminAgencyEntity adminAgencyEntity) throws Exception;

    /**
     * <pre>
     * 1. MethodName : updateAgency
     * 2. ClassName  : AdminAgencyJpaService.java
     * 3. Comment    : 관리자 소속사 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 14.
     * </pre>
     */
    AdminAgencyDTO updateAgency(AdminAgencyEntity adminAgencyEntity) throws Exception;

    /**
     * <pre>
     * 1. MethodName : deleteAgency
     * 2. ClassName  : AdminAgencyJpaService.java
     * 3. Comment    : 관리자 소속사 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 14.
     * </pre>
     */
    Integer deleteAgency(Integer idx) throws Exception;

    /**
     * <pre>
     * 1. MethodName : insertAgencyImage
     * 2. ClassName  : AdminAgencyJpaService.java
     * 3. Comment    : 관리자 소속사 이미지 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 14.
     * </pre>
     */
    String insertAgencyImage(CommonImageEntity commonImageEntity, List<MultipartFile> fileName) throws Exception;

    /**
     * <pre>
     * 1. MethodName : deleteAgencyImage
     * 2. ClassName  : AdminAgencyJpaService.java
     * 3. Comment    : 관리자 소속사 이미지 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 14.
     * </pre>
     */
    Integer deleteAgencyImage(Integer idx) throws Exception;
}
