package com.tsp.new_tsp_admin.api.model.service.schedule;

import com.tsp.new_tsp_admin.api.domain.model.AdminModelDTO;
import com.tsp.new_tsp_admin.api.domain.model.schedule.AdminScheduleDTO;
import com.tsp.new_tsp_admin.api.domain.model.schedule.AdminScheduleEntity;

import java.util.List;
import java.util.Map;

public interface AdminScheduleJpaService {
    /**
     * <pre>
     * 1. MethodName : findScheduleCount
     * 2. ClassName  : AdminScheduleJpaService.java
     * 3. Comment    : 관리자 모델 스케줄 리스트 수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 31.
     * </pre>
     */
    int findScheduleCount(Map<String, Object> scheduleMap);

    /**
     * <pre>
     * 1. MethodName : findModelScheduleList
     * 2. ClassName  : AdminScheduleJpaService.java
     * 3. Comment    : 관리자 모델 스케줄 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 31.
     * </pre>
     */
    List<AdminModelDTO> findModelScheduleList(Map<String, Object> scheduleMap);

    /**
     * <pre>
     * 1. MethodName : findOneSchedule
     * 2. ClassName  : AdminScheduleJpaService.java
     * 3. Comment    : 관리자 모델 스케줄 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 31.
     * </pre>
     */
    AdminScheduleDTO findOneSchedule(Long idx);

    /**
     * <pre>
     * 1. MethodName : findPrevOneSchedule
     * 2. ClassName  : AdminScheduleJpaService.java
     * 3. Comment    : 관리자 모델 스케줄 이전 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 22.
     * </pre>
     */
    AdminScheduleDTO findPrevOneSchedule(Long idx);

    /**
     * <pre>
     * 1. MethodName : findNextOneSchedule
     * 2. ClassName  : AdminScheduleJpaService.java
     * 3. Comment    : 관리자 모델 스케줄 다음 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 22.
     * </pre>
     */
    AdminScheduleDTO findNextOneSchedule(Long idx);

    /**
     * <pre>
     * 1. MethodName : insertSchedule
     * 2. ClassName  : AdminScheduleJpaService.java
     * 3. Comment    : 관리자 모델 스케줄 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 31.
     * </pre>
     */
    AdminScheduleDTO insertSchedule(AdminScheduleEntity adminScheduleEntity);

    /**
     * <pre>
     * 1. MethodName : updateSchedule
     * 2. ClassName  : AdminScheduleJpaService.java
     * 3. Comment    : 관리자 모델 스케줄 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 31.
     * </pre>
     */
    AdminScheduleDTO updateSchedule(AdminScheduleEntity adminScheduleEntity);

    /**
     * <pre>
     * 1. MethodName : deleteSchedule
     * 2. ClassName  : AdminScheduleJpaService.java
     * 3. Comment    : 관리자 모델 스케줄 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 31.
     * </pre>
     */
    Long deleteSchedule(Long idx);
}
