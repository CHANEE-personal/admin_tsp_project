package com.tsp.new_tsp_admin.api.model.service.schedule;

import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import com.tsp.new_tsp_admin.api.domain.model.schedule.AdminScheduleDTO;
import com.tsp.new_tsp_admin.api.domain.model.schedule.AdminScheduleEntity;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.tsp.new_tsp_admin.exception.ApiExceptionType.*;

@Service
@RequiredArgsConstructor
public class AdminScheduleJpaServiceImpl implements AdminScheduleJpaService {
    private final AdminScheduleJpaQueryRepository adminScheduleJpaQueryRepository;
    private final AdminScheduleJpaRepository adminScheduleJpaRepository;

    /**
     * <pre>
     * 1. MethodName : findScheduleCount
     * 2. ClassName  : AdminScheduleJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 스케줄 리스트 수 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 31.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public int findScheduleCount(Map<String, Object> scheduleMap) {
        return adminScheduleJpaQueryRepository.findScheduleCount(scheduleMap);
    }

    /**
     * <pre>
     * 1. MethodName : findScheduleList
     * 2. ClassName  : AdminScheduleJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 스케줄 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 31.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public List<AdminScheduleDTO> findScheduleList(Map<String, Object> scheduleMap) {
        return adminScheduleJpaQueryRepository.findScheduleList(scheduleMap);
    }

    /**
     * <pre>
     * 1. MethodName : findOneSchedule
     * 2. ClassName  : AdminScheduleJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 스케줄 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 31.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public AdminScheduleDTO findOneSchedule(Long idx) {
        AdminScheduleEntity oneSchedule = adminScheduleJpaRepository.findById(idx).orElseThrow(() -> new TspException(NOT_FOUND_MODEL_SCHEDULE));
        return AdminScheduleEntity.toDto(oneSchedule);
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneSchedule
     * 2. ClassName  : AdminScheduleJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 이전 스케줄 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 22.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public AdminScheduleDTO findPrevOneSchedule(Long idx) {
        return adminScheduleJpaQueryRepository.findPrevOneSchedule(idx);
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneSchedule
     * 2. ClassName  : AdminScheduleJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 다음 스케줄 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 22.
     * </pre>
     */
    @Override
    @Transactional(readOnly = true)
    public AdminScheduleDTO findNextOneSchedule(Long idx) {
        return adminScheduleJpaQueryRepository.findNextOneSchedule(idx);
    }

    /**
     * <pre>
     * 1. MethodName : insertSchedule
     * 2. ClassName  : AdminScheduleJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 스케줄 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 31.
     * </pre>
     */
    @Override
    @Transactional
    public AdminScheduleDTO insertSchedule(AdminModelEntity adminModelEntity, AdminScheduleEntity adminScheduleEntity) {
        try {
            adminModelEntity.addSchedule(adminScheduleEntity);
            return AdminScheduleEntity.toDto(adminScheduleJpaRepository.save(adminScheduleEntity));
        } catch (Exception e) {
            throw new TspException(ERROR_MODEL_SCHEDULE);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateSchedule
     * 2. ClassName  : AdminScheduleJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 스케줄 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 31.
     * </pre>
     */
    @Override
    @Transactional
    public AdminScheduleDTO updateSchedule(AdminScheduleEntity adminScheduleEntity) {
        try {
            Optional<AdminScheduleEntity> oneSchedule = Optional.ofNullable(adminScheduleJpaRepository.findById(adminScheduleEntity.getIdx())
                    .orElseThrow(() -> new TspException(NOT_FOUND_MODEL_SCHEDULE)));
            oneSchedule.ifPresent(adminSchedule -> adminSchedule.update(adminScheduleEntity));
            return AdminScheduleEntity.toDto(adminScheduleEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_UPDATE_MODEL_SCHEDULE);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteSchedule
     * 2. ClassName  : AdminScheduleJpaServiceImpl.java
     * 3. Comment    : 관리자 모델 스케줄 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 31.
     * </pre>
     */
    @Override
    @Transactional
    public Long deleteSchedule(Long idx) {
        try {
            adminScheduleJpaRepository.deleteById(idx);
            return idx;
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_MODEL_SCHEDULE);
        }
    }
}
