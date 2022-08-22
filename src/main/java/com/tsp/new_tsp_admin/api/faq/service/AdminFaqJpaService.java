package com.tsp.new_tsp_admin.api.faq.service;

import com.tsp.new_tsp_admin.api.domain.faq.AdminFaqDTO;
import com.tsp.new_tsp_admin.api.domain.faq.AdminFaqEntity;

import java.util.List;
import java.util.Map;

public interface AdminFaqJpaService {
    /**
     * <pre>
     * 1. MethodName : findFaqCount
     * 2. ClassName  : AdminFaqJpaService.java
     * 3. Comment    : 관리자 FAQ 리스트 수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 22.
     * </pre>
     */
    Integer findFaqCount(Map<String, Object> faqMap) throws Exception;

    /**
     * <pre>
     * 1. MethodName : findFaqsList
     * 2. ClassName  : AdminFaqJpaService.java
     * 3. Comment    : 관리자 FAQ 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 22.
     * </pre>
     */
    List<AdminFaqDTO> findFaqsList(Map<String, Object> faqMap) throws Exception;

    /**
     * <pre>
     * 1. MethodName : findOneFaq
     * 2. ClassName  : AdminFaqJpaService.java
     * 3. Comment    : 관리자 FAQ 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 22.
     * </pre>
     */
    AdminFaqDTO findOneFaq(AdminFaqEntity adminFaqEntity) throws Exception;

    /**
     * <pre>
     * 1. MethodName : insertFaq
     * 2. ClassName  : AdminFaqJpaService.java
     * 3. Comment    : 관리자 FAQ 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 22.
     * </pre>
     */
    AdminFaqDTO insertFaq(AdminFaqEntity adminFaqEntity) throws Exception;

    /**
     * <pre>
     * 1. MethodName : updateFaq
     * 2. ClassName  : AdminFaqJpaService.java
     * 3. Comment    : 관리자 FAQ 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 22.
     * </pre>
     */
    AdminFaqDTO updateFaq(AdminFaqEntity adminFaqEntity) throws Exception;

    /**
     * <pre>
     * 1. MethodName : deleteFaq
     * 2. ClassName  : AdminFaqJpaService.java
     * 3. Comment    : 관리자 FAQ 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 22.
     * </pre>
     */
    Integer deleteFaq(Integer idx) throws Exception;
}
