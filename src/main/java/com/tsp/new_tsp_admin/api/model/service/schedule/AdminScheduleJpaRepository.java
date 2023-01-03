package com.tsp.new_tsp_admin.api.model.service.schedule;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelDTO;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import com.tsp.new_tsp_admin.api.domain.model.schedule.AdminScheduleDTO;
import com.tsp.new_tsp_admin.api.domain.model.schedule.AdminScheduleEntity;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity.toDto;
import static com.tsp.new_tsp_admin.api.domain.model.QAdminModelEntity.adminModelEntity;
import static com.tsp.new_tsp_admin.api.domain.model.schedule.AdminScheduleEntity.toDto;
import static com.tsp.new_tsp_admin.api.domain.model.schedule.AdminScheduleEntity.toDtoList;
import static com.tsp.new_tsp_admin.api.domain.model.schedule.QAdminScheduleEntity.adminScheduleEntity;
import static com.tsp.new_tsp_admin.common.StringUtil.getInt;
import static com.tsp.new_tsp_admin.common.StringUtil.getString;
import static com.tsp.new_tsp_admin.exception.ApiExceptionType.NOT_FOUND_MODEL_SCHEDULE;
import static java.time.LocalDate.now;
import static java.time.LocalDateTime.of;
import static java.util.Collections.emptyList;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AdminScheduleJpaRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private BooleanExpression searchModelSchedule(Map<String, Object> scheduleMap) {
        String searchKeyword = getString(scheduleMap.get("searchKeyword"), "");
        LocalDateTime searchStartTime = scheduleMap.get("searchStartTime") != null ? (LocalDateTime) scheduleMap.get("searchStartTime") : now().minusDays(now().getDayOfMonth() - 1).atStartOfDay();
        LocalDateTime searchEndTime = scheduleMap.get("searchEndTime") != null ? (LocalDateTime) scheduleMap.get("searchStartTime") : of(now().minusDays(now().getDayOfMonth()).plusMonths(1), LocalTime.of(23, 59, 59));

        return !Objects.equals(searchKeyword, "") ?
                        adminScheduleEntity.modelSchedule.contains(searchKeyword) :
                adminScheduleEntity.modelScheduleTime.between(searchStartTime, searchEndTime);
    }

    /**
     * <pre>
     * 1. MethodName : findScheduleCount
     * 2. ClassName  : AdminScheduleJpaRepository.java
     * 3. Comment    : 관리자 모델 스케줄 리스트 갯수 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 31.
     * </pre>
     */
    public int findScheduleCount(Map<String, Object> scheduleMap) {
        return queryFactory.selectFrom(adminScheduleEntity)
                .where(searchModelSchedule(scheduleMap))
                .fetch().size();
    }

    /**
     * <pre>
     * 1. MethodName : findModelScheduleList
     * 2. ClassName  : AdminScheduleJpaRepository.java
     * 3. Comment    : 관리자 모델 스케줄 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 31.
     * </pre>
     */
    public List<AdminScheduleDTO> findScheduleList(Map<String, Object> scheduleMap) {
        List<AdminScheduleEntity> scheduleList = queryFactory
                .selectFrom(adminScheduleEntity)
                .orderBy(adminScheduleEntity.idx.desc())
                .where(searchModelSchedule(scheduleMap)
                        .and(adminScheduleEntity.visible.eq("Y")))
                .offset(getInt(scheduleMap.get("jpaStartPage"), 0))
                .limit(getInt(scheduleMap.get("size"), 0))
                .fetch();

        return scheduleList != null ? toDtoList(scheduleList) : emptyList();
    }

    /**
     * <pre>
     * 1. MethodName : findOneModelSchedule
     * 2. ClassName  : AdminScheduleJpaRepository.java
     * 3. Comment    : 관리자 모델 스케줄 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 31.
     * </pre>
     */
    public AdminModelDTO findOneModelSchedule(AdminScheduleEntity existAdminScheduleEntity) {
        AdminModelEntity findOneModelSchedule = Optional.ofNullable(queryFactory
                .selectFrom(adminModelEntity)
                .leftJoin(adminModelEntity.scheduleList, adminScheduleEntity)
                .fetchJoin()
                .where(adminModelEntity.visible.eq("Y")
                        .and(adminScheduleEntity.visible.eq("Y"))
                        .and(adminModelEntity.idx.eq(existAdminScheduleEntity.getModelIdx()))
                        .and(adminScheduleEntity.idx.eq(existAdminScheduleEntity.getIdx())))
                .fetchOne()).orElseThrow(() -> new TspException(NOT_FOUND_MODEL_SCHEDULE, new Throwable()));

        return toDto(findOneModelSchedule);
    }

    /**
     * <pre>
     * 1. MethodName : findOneSchedule
     * 2. ClassName  : AdminScheduleJpaRepository.java
     * 3. Comment    : 관리자 모델 스케줄 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 31.
     * </pre>
     */
    public AdminScheduleDTO findOneSchedule(Long idx) {
        AdminScheduleEntity findOneSchedule = Optional.ofNullable(queryFactory
                .selectFrom(adminScheduleEntity)
                .orderBy(adminScheduleEntity.idx.desc())
                .where(adminScheduleEntity.visible.eq("Y")
                        .and(adminScheduleEntity.idx.eq(idx)))
                .fetchOne()).orElseThrow(() -> new TspException(NOT_FOUND_MODEL_SCHEDULE, new Throwable()));

        return toDto(findOneSchedule);
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneSchedule
     * 2. ClassName  : AdminScheduleJpaRepository.java
     * 3. Comment    : 관리자 이전 모델 스케줄 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 22.
     * </pre>
     */
    public AdminScheduleDTO findPrevOneSchedule(Long idx) {
        // 이전 모델 스케줄 조회
        AdminScheduleEntity findPrevOneSchedule = Optional.ofNullable(queryFactory
                .selectFrom(adminScheduleEntity)
                .orderBy(adminScheduleEntity.idx.desc())
                .where(adminScheduleEntity.idx.lt(idx)
                        .and(adminScheduleEntity.visible.eq("Y")))
                .fetchFirst()).orElseThrow(() -> new TspException(NOT_FOUND_MODEL_SCHEDULE, new Throwable()));

        return toDto(findPrevOneSchedule);
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneSchedule
     * 2. ClassName  : AdminScheduleJpaRepository.java
     * 3. Comment    : 관리자 다음 모델 스케줄 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 22.
     * </pre>
     */
    public AdminScheduleDTO findNextOneSchedule(Long idx) {
        // 다음 모델 스케줄 조회
        AdminScheduleEntity findNextOneSchedule = Optional.ofNullable(queryFactory
                .selectFrom(adminScheduleEntity)
                .orderBy(adminScheduleEntity.idx.asc())
                .where(adminScheduleEntity.idx.gt(idx)
                        .and(adminScheduleEntity.visible.eq("Y")))
                .fetchFirst()).orElseThrow(() -> new TspException(NOT_FOUND_MODEL_SCHEDULE, new Throwable()));

        return toDto(findNextOneSchedule);
    }

    /**
     * <pre>
     * 1. MethodName : insertSchedule
     * 2. ClassName  : AdminScheduleJpaRepository.java
     * 3. Comment    : 관리자 모델 스케줄 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 31.
     * </pre>
     */
    public AdminScheduleDTO insertSchedule(AdminScheduleEntity adminScheduleEntity) {
        em.persist(adminScheduleEntity);
        return toDto(adminScheduleEntity);
    }

    /**
     * <pre>
     * 1. MethodName : updateSchedule
     * 2. ClassName  : AdminScheduleJpaRepository.java
     * 3. Comment    : 관리자 모델 스케줄 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 31.
     * </pre>
     */
    public AdminScheduleDTO updateSchedule(AdminScheduleEntity existAdminScheduleEntity) {
        em.merge(existAdminScheduleEntity);
        return toDto(existAdminScheduleEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deleteSchedule
     * 2. ClassName  : AdminScheduleJpaRepository.java
     * 3. Comment    : 관리자 모델 스케줄 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 31.
     * </pre>
     */
    public Long deleteSchedule(Long idx) {
        em.remove(em.find(AdminScheduleEntity.class, idx));
        return idx;
    }
}
