package com.tsp.new_tsp_admin.api.model.service.schedule;

import com.tsp.new_tsp_admin.api.domain.model.AdminModelDTO;
import com.tsp.new_tsp_admin.api.domain.model.schedule.AdminScheduleDTO;
import com.tsp.new_tsp_admin.api.domain.model.schedule.AdminScheduleEntity;
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
public class AdminScheduleJpaServiceImpl implements AdminScheduleJpaService {
    private final AdminScheduleJpaRepository adminScheduleJpaRepository;

    /**
     * <pre>
     * 1. MethodName : findScheduleCount
     * 2. ClassName  : AdminScheduleJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 스케줄 리스트 수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 31.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public Integer findScheduleCount(Map<String, Object> scheduleMap) throws TspException {
        try {
            return adminScheduleJpaRepository.findScheduleCount(scheduleMap);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_MODEL_SCHEDULE_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findScheduleList
     * 2. ClassName  : AdminScheduleJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 스케줄 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 31.
     * </pre>
     */
    @Override
    @Cacheable("model")
    @Transactional(readOnly = true)
    public List<AdminModelDTO> findModelScheduleList(Map<String, Object> scheduleMap) throws TspException {
        try {
            return adminScheduleJpaRepository.findModelScheduleList(scheduleMap);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_MODEL_SCHEDULE_LIST, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findOneSchedule
     * 2. ClassName  : AdminScheduleJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 스케줄 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 31.
     * </pre>
     */
    @Override
    @Cacheable("schedule")
    @Transactional(readOnly = true)
    public AdminScheduleDTO findOneSchedule(Long idx) throws TspException {
        try {
            return adminScheduleJpaRepository.findOneSchedule(idx);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_MODEL_SCHEDULE, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneSchedule
     * 2. ClassName  : AdminScheduleJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 이전 스케줄 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 22.
     * </pre>
     */
    @Override
    @Cacheable("schedule")
    @Transactional(readOnly = true)
    public AdminScheduleDTO findPrevOneSchedule(Long idx) throws TspException {
        try {
            return adminScheduleJpaRepository.findPrevOneSchedule(idx);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_MODEL_SCHEDULE, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneSchedule
     * 2. ClassName  : AdminScheduleJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 다음 스케줄 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 22.
     * </pre>
     */
    @Override
    @Cacheable("schedule")
    @Transactional(readOnly = true)
    public AdminScheduleDTO findNextOneSchedule(Long idx) throws TspException {
        try {
            return adminScheduleJpaRepository.findNextOneSchedule(idx);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_MODEL_SCHEDULE, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : insertSchedule
     * 2. ClassName  : AdminScheduleJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 스케줄 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 31.
     * </pre>
     */
    @Override
    @CachePut("schedule")
    @Modifying(clearAutomatically = true)
    @Transactional
    public AdminScheduleDTO insertSchedule(AdminScheduleEntity adminScheduleEntity) throws TspException {
        try {
            return adminScheduleJpaRepository.insertSchedule(adminScheduleEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_MODEL_SCHEDULE, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateSchedule
     * 2. ClassName  : AdminScheduleJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 스케줄 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 31.
     * </pre>
     */
    @Override
    @CachePut("schedule")
    @Modifying(clearAutomatically = true)
    @Transactional
    public AdminScheduleDTO updateSchedule(AdminScheduleEntity adminScheduleEntity) throws TspException {
        try {
            return adminScheduleJpaRepository.updateSchedule(adminScheduleEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_UPDATE_MODEL_SCHEDULE, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteSchedule
     * 2. ClassName  : AdminScheduleJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 스케줄 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 31.
     * </pre>
     */
    @Override
    @CacheEvict("schedule")
    @Modifying(clearAutomatically = true)
    @Transactional
    public Long deleteSchedule(Long idx) throws TspException {
        try {
            return adminScheduleJpaRepository.deleteSchedule(idx);
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_MODEL_SCHEDULE, e);
        }
    }
}
