package com.tsp.new_tsp_admin.api.faq.service;

import com.tsp.new_tsp_admin.api.domain.faq.AdminFaqDTO;
import com.tsp.new_tsp_admin.api.domain.faq.AdminFaqEntity;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_admin.exception.ApiExceptionType.*;

@Service
@RequiredArgsConstructor
public class AdminFaqJpaServiceImpl implements AdminFaqJpaService {
    private final AdminFaqJpaRepository adminFaqJpaRepository;

    /**
     * <pre>
     * 1. MethodName : findFaqCount
     * 2. ClassName  : AdminFaqJpaServiceImpl.java
     * 3. Comment    : 관리자 FAQ 리스트 수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 22.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public Integer findFaqCount(Map<String, Object> faqMap) throws Exception {
        try {
            return adminFaqJpaRepository.findFaqCount(faqMap);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_FAQ_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findFaqsList
     * 2. ClassName  : AdminFaqJpaServiceImpl.java
     * 3. Comment    : 관리자 FAQ 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 22.
     * </pre>
     */
    @Override
    @Cacheable("faq")
    @Transactional(readOnly = true)
    public List<AdminFaqDTO> findFaqsList(Map<String, Object> faqMap) throws Exception {
        try {
            return adminFaqJpaRepository.findFaqsList(faqMap);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_FAQ_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findOneFaq
     * 2. ClassName  : AdminFaqJpaServiceImpl.java
     * 3. Comment    : 관리자 FAQ 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 22.
     * </pre>
     */
    @Override
    @Cacheable("faq")
    @Transactional(readOnly = true)
    public AdminFaqDTO findOneFaq(AdminFaqEntity adminFaqEntity) throws Exception {
        try {
            return adminFaqJpaRepository.findOneFaq(adminFaqEntity);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_FAQ, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : insertFaq
     * 2. ClassName  : AdminFaqServiceImpl.java
     * 3. Comment    : 관리자 FAQ 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 22.
     * </pre>
     */
    @Override
    @CachePut("faq")
    @Modifying(clearAutomatically = true)
    @Transactional
    public AdminFaqDTO insertFaq(AdminFaqEntity adminFaqEntity) throws Exception {
        try {
            return adminFaqJpaRepository.insertFaq(adminFaqEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_FAQ, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateFaq
     * 2. ClassName  : AdminFaqServiceImpl.java
     * 3. Comment    : 관리자 FAQ 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 22.
     * </pre>
     */
    @Override
    @CachePut("faq")
    @Modifying(clearAutomatically = true)
    @Transactional
    public AdminFaqDTO updateFaq(AdminFaqEntity adminFaqEntity) throws Exception {
        try {
            return adminFaqJpaRepository.updateFaq(adminFaqEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_UPDATE_FAQ, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteFaq
     * 2. ClassName  : AdminFaqServiceImpl.java
     * 3. Comment    : 관리자 FAQ 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 22.
     * </pre>
     */
    @Override
    @CacheEvict("faq")
    @Modifying(clearAutomatically = true)
    @Transactional
    public Integer deleteFaq(Integer idx) throws Exception {
        try {
            return adminFaqJpaRepository.deleteFaq(idx);
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_FAQ, e);
        }
    }
}
