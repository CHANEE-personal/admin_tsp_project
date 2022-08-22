package com.tsp.new_tsp_admin.api.notice.service;

import com.tsp.new_tsp_admin.api.domain.notice.AdminNoticeDTO;
import com.tsp.new_tsp_admin.api.domain.notice.AdminNoticeEntity;
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
public class AdminNoticeJpaServiceImpl implements AdminNoticeJpaService {
    private final AdminNoticeJpaRepository adminNoticeJpaRepository;

    /**
     * <pre>
     * 1. MethodName : findNoticeCount
     * 2. ClassName  : AdminNoticeServiceImpl.java
     * 3. Comment    : 관리자 공지사항 리스트 수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 16.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public Integer findNoticeCount(Map<String, Object> noticeMap) throws Exception {
        try {
            return adminNoticeJpaRepository.findNoticeCount(noticeMap);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_NOTICE_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findNoticesList
     * 2. ClassName  : AdminNoticeServiceImpl.java
     * 3. Comment    : 관리자 공지사항 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 16.
     * </pre>
     */
    @Override
    @Cacheable("notice")
    @Transactional(readOnly = true)
    public List<AdminNoticeDTO> findNoticesList(Map<String, Object> noticeMap) throws Exception {
        try {
            return adminNoticeJpaRepository.findNoticesList(noticeMap);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_NOTICE_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findOneNotice
     * 2. ClassName  : AdminNoticeServiceImpl.java
     * 3. Comment    : 관리자 공지사항 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 16.
     * </pre>
     */
    @Override
    @Cacheable("notice")
    @Transactional(readOnly = true)
    public AdminNoticeDTO findOneNotice(AdminNoticeEntity adminNoticeEntity) throws Exception {
        try {
            return adminNoticeJpaRepository.findOneNotice(adminNoticeEntity);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_NOTICE, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : insertNotice
     * 2. ClassName  : AdminNoticeServiceImpl.java
     * 3. Comment    : 관리자 공지사항 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 16.
     * </pre>
     */
    @Override
    @CachePut("notice")
    @Modifying(clearAutomatically = true)
    @Transactional
    public AdminNoticeDTO insertNotice(AdminNoticeEntity adminNoticeEntity) throws Exception {
        try {
            return adminNoticeJpaRepository.insertNotice(adminNoticeEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_NOTICE, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateNotice
     * 2. ClassName  : AdminNoticeServiceImpl.java
     * 3. Comment    : 관리자 공지사항 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 16.
     * </pre>
     */
    @Override
    @CachePut("notice")
    @Modifying(clearAutomatically = true)
    @Transactional
    public AdminNoticeDTO updateNotice(AdminNoticeEntity adminNoticeEntity) throws Exception {
        try {
            return adminNoticeJpaRepository.updateNotice(adminNoticeEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_UPDATE_NOTICE, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteNotice
     * 2. ClassName  : AdminNoticeServiceImpl.java
     * 3. Comment    : 관리자 공지사항 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 16.
     * </pre>
     */
    @Override
    @CacheEvict("notice")
    @Modifying(clearAutomatically = true)
    @Transactional
    public Integer deleteNotice(Integer idx) throws Exception {
        try {
            return adminNoticeJpaRepository.deleteNotice(idx);
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_NOTICE, e);
        }
    }
}