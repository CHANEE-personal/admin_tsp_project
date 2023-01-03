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
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 22.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public int findFaqCount(Map<String, Object> faqMap) {
        return adminFaqJpaRepository.findFaqCount(faqMap);
    }

    /**
     * <pre>
     * 1. MethodName : findFaqList
     * 2. ClassName  : AdminFaqJpaServiceImpl.java
     * 3. Comment    : 관리자 FAQ 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 22.
     * </pre>
     */
    @Override
    @Cacheable(value = "faq", key = "#faqMap")
    @Transactional(readOnly = true)
    public List<AdminFaqDTO> findFaqList(Map<String, Object> faqMap) {
        return adminFaqJpaRepository.findFaqList(faqMap);
    }

    /**
     * <pre>
     * 1. MethodName : findOneFaq
     * 2. ClassName  : AdminFaqJpaServiceImpl.java
     * 3. Comment    : 관리자 FAQ 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 22.
     * </pre>
     */
    @Override
    @Cacheable(value = "faq", key = "#idx")
    @Transactional(readOnly = true)
    public AdminFaqDTO findOneFaq(Long idx) {
        return adminFaqJpaRepository.findOneFaq(idx);
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneFaq
     * 2. ClassName  : AdminFaqJpaServiceImpl.java
     * 3. Comment    : 관리자 이전 FAQ 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 18.
     * </pre>
     */
    @Override
    @Cacheable(value = "faq", key = "#idx")
    @Transactional(readOnly = true)
    public AdminFaqDTO findPrevOneFaq(Long idx) {
        return adminFaqJpaRepository.findPrevOneFaq(idx);
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneFaq
     * 2. ClassName  : AdminFaqJpaServiceImpl.java
     * 3. Comment    : 관리자 다음 FAQ 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 18.
     * </pre>
     */
    @Override
    @Cacheable(value = "faq", key = "#idx")
    @Transactional(readOnly = true)
    public AdminFaqDTO findNextOneFaq(Long idx) {
        return adminFaqJpaRepository.findNextOneFaq(idx);
    }

    /**
     * <pre>
     * 1. MethodName : insertFaq
     * 2. ClassName  : AdminFaqServiceImpl.java
     * 3. Comment    : 관리자 FAQ 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 22.
     * </pre>
     */
    @Override
    @CachePut("faq")
    @Transactional
    public AdminFaqDTO insertFaq(AdminFaqEntity adminFaqEntity) {
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
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 22.
     * </pre>
     */
    @Override
    @CachePut(value = "faq", key = "#adminFaqEntity.idx")
    @Transactional
    public AdminFaqDTO updateFaq(AdminFaqEntity adminFaqEntity) {
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
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 22.
     * </pre>
     */
    @Override
    @CacheEvict(value = "faq", key = "#idx")
    @Transactional
    public Long deleteFaq(Long idx) {
        try {
            return adminFaqJpaRepository.deleteFaq(idx);
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_FAQ, e);
        }
    }
}
