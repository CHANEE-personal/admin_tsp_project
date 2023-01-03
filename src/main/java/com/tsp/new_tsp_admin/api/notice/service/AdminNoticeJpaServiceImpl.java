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
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 16.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public int findNoticeCount(Map<String, Object> noticeMap) {
        return adminNoticeJpaRepository.findNoticeCount(noticeMap);
    }

    /**
     * <pre>
     * 1. MethodName : findNoticeList
     * 2. ClassName  : AdminNoticeServiceImpl.java
     * 3. Comment    : 관리자 공지사항 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 16.
     * </pre>
     */
    @Override
    @Cacheable(value = "notice", key = "#noticeMap")
    @Transactional(readOnly = true)
    public List<AdminNoticeDTO> findNoticeList(Map<String, Object> noticeMap) {
        return adminNoticeJpaRepository.findNoticeList(noticeMap);
    }

    /**
     * <pre>
     * 1. MethodName : findOneNotice
     * 2. ClassName  : AdminNoticeServiceImpl.java
     * 3. Comment    : 관리자 공지사항 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 16.
     * </pre>
     */
    @Override
    @Cacheable(value = "notice", key = "#idx")
    @Transactional(readOnly = true)
    public AdminNoticeDTO findOneNotice(Long idx) {
        return adminNoticeJpaRepository.findOneNotice(idx);
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneNotice
     * 2. ClassName  : AdminNoticeServiceImpl.java
     * 3. Comment    : 관리자 이전 공지사항 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 18.
     * </pre>
     */
    @Override
    @Cacheable(value = "notice", key = "#idx")
    @Transactional(readOnly = true)
    public AdminNoticeDTO findPrevOneNotice(Long idx) {
        return adminNoticeJpaRepository.findPrevOneNotice(idx);
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneNotice
     * 2. ClassName  : AdminNoticeServiceImpl.java
     * 3. Comment    : 관리자 다음 공지사항 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 18.
     * </pre>
     */
    @Override
    @Cacheable(value = "notice", key = "#idx")
    @Transactional(readOnly = true)
    public AdminNoticeDTO findNextOneNotice(Long idx) {
        return adminNoticeJpaRepository.findNextOneNotice(idx);
    }

    /**
     * <pre>
     * 1. MethodName : insertNotice
     * 2. ClassName  : AdminNoticeServiceImpl.java
     * 3. Comment    : 관리자 공지사항 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 16.
     * </pre>
     */
    @Override
    @CachePut("notice")
    @Transactional
    public AdminNoticeDTO insertNotice(AdminNoticeEntity adminNoticeEntity) {
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
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 16.
     * </pre>
     */
    @Override
    @CachePut(value = "notice", key = "#adminNoticeEntity.idx")
    @Transactional
    public AdminNoticeDTO updateNotice(AdminNoticeEntity adminNoticeEntity) {
        try {
            return adminNoticeJpaRepository.updateNotice(adminNoticeEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_UPDATE_NOTICE, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : toggleFixed
     * 2. ClassName  : AdminNoticeServiceImpl.java
     * 3. Comment    : 관리자 공지사항 상단 고정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 23.
     * </pre>
     */
    @Override
    @CachePut(value = "notice", key = "#idx")
    @Transactional
    public Boolean toggleFixed(Long idx) {
        try {
            return adminNoticeJpaRepository.toggleFixed(idx);
        } catch (Exception e) {
            throw new TspException(ERROR_UPDATE_NOTICE, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteNotice
     * 2. ClassName  : AdminNoticeServiceImpl.java
     * 3. Comment    : 관리자 공지사항 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 16.
     * </pre>
     */
    @Override
    @CacheEvict(value = "notice", key = "#idx")
    @Transactional
    public Long deleteNotice(Long idx) {
        try {
            return adminNoticeJpaRepository.deleteNotice(idx);
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_NOTICE, e);
        }
    }
}
