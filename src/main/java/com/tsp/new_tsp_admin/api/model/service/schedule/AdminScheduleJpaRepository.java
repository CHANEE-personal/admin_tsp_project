package com.tsp.new_tsp_admin.api.model.service.schedule;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelDTO;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import com.tsp.new_tsp_admin.api.domain.model.schedule.AdminScheduleDTO;
import com.tsp.new_tsp_admin.api.domain.model.schedule.AdminScheduleEntity;
import com.tsp.new_tsp_admin.api.model.mapper.ModelMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_admin.api.domain.model.QAdminModelEntity.adminModelEntity;
import static com.tsp.new_tsp_admin.api.domain.model.schedule.QAdminScheduleEntity.adminScheduleEntity;
import static com.tsp.new_tsp_admin.api.model.mapper.schedule.ScheduleMapper.*;
import static com.tsp.new_tsp_admin.common.StringUtil.getInt;
import static com.tsp.new_tsp_admin.common.StringUtil.getString;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AdminScheduleJpaRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private BooleanExpression searchModelSchedule(Map<String, Object> scheduleMap) {
        String searchKeyword = getString(scheduleMap.get("searchKeyword"), "");
        LocalDateTime searchStartTime = (LocalDateTime) scheduleMap.get("searchStartTime");
        LocalDateTime searchEndTime = (LocalDateTime) scheduleMap.get("searchEndTime");

        if (searchStartTime != null && searchEndTime != null) {
            searchStartTime = (LocalDateTime) scheduleMap.get("searchStartTime");
            searchEndTime = (LocalDateTime) scheduleMap.get("searchEndTime");
        } else {
            searchStartTime = LocalDate.now().minusDays(LocalDate.now().getDayOfMonth()-1).atStartOfDay();
            searchEndTime = LocalDateTime.of(LocalDate.now().minusDays(LocalDate.now().getDayOfMonth()).plusMonths(1), LocalTime.of(23,59,59));
        }

        if (!"".equals(searchKeyword)) {
            return adminModelEntity.modelKorName.contains(searchKeyword)
                    .or(adminModelEntity.modelEngName.contains(searchKeyword)
                            .or(adminModelEntity.modelDescription.contains(searchKeyword)))
                    .or(adminScheduleEntity.modelSchedule.contains(searchKeyword));
        } else {
            return adminScheduleEntity.modelScheduleTime.between(searchStartTime, searchEndTime);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findScheduleCount
     * 2. ClassName  : AdminScheduleJpaRepository.java
     * 3. Comment    : 관리자 모델 스케줄 리스트 갯수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 31.
     * </pre>
     */
    public Integer findScheduleCount(Map<String, Object> scheduleMap) {
        return queryFactory.selectFrom(adminScheduleEntity)
                .where(searchModelSchedule(scheduleMap))
                .fetch().size();
    }

    /**
     * <pre>
     * 1. MethodName : findModelScheduleList
     * 2. ClassName  : AdminScheduleJpaRepository.java
     * 3. Comment    : 관리자 모델 스케줄 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 31.
     * </pre>
     */
    public List<AdminModelDTO> findModelScheduleList(Map<String, Object> scheduleMap) {
        List<AdminModelEntity> modelScheduleList = queryFactory
                .selectFrom(adminModelEntity)
                .orderBy(adminScheduleEntity.idx.desc())
                .leftJoin(adminModelEntity.scheduleList, adminScheduleEntity)
                .fetchJoin()
                .where(searchModelSchedule(scheduleMap)
                        .and(adminModelEntity.visible.eq("Y"))
                        .and(adminScheduleEntity.visible.eq("Y")))
                .offset(getInt(scheduleMap.get("jpaStartPage"), 0))
                .limit(getInt(scheduleMap.get("size"), 0))
                .fetch();

        modelScheduleList.forEach(list -> modelScheduleList.get(modelScheduleList.indexOf(list))
                .setRnum(getInt(scheduleMap.get("startPage"), 1) * (getInt(scheduleMap.get("size"), 1)) - (2 - modelScheduleList.indexOf(list))));

        return ModelMapper.INSTANCE.toDtoList(modelScheduleList);
    }

    /**
     * <pre>
     * 1. MethodName : findOneModelSchedule
     * 2. ClassName  : AdminScheduleJpaRepository.java
     * 3. Comment    : 관리자 모델 스케줄 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 31.
     * </pre>
     */
    public AdminModelDTO findOneModelSchedule(AdminScheduleEntity existAdminScheduleEntity) {
        AdminModelEntity findOneModelSchedule = queryFactory
                .selectFrom(adminModelEntity)
                .leftJoin(adminModelEntity.scheduleList, adminScheduleEntity)
                .fetchJoin()
                .where(adminModelEntity.visible.eq("Y")
                        .and(adminScheduleEntity.visible.eq("Y"))
                        .and(adminModelEntity.idx.eq(existAdminScheduleEntity.getModelIdx()))
                        .and(adminScheduleEntity.idx.eq(existAdminScheduleEntity.getIdx())))
                .fetchOne();

        return ModelMapper.INSTANCE.toDto(findOneModelSchedule);
    }

    /**
     * <pre>
     * 1. MethodName : findOneSchedule
     * 2. ClassName  : AdminScheduleJpaRepository.java
     * 3. Comment    : 관리자 모델 스케줄 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 31.
     * </pre>
     */
    public AdminScheduleDTO findOneSchedule(AdminScheduleEntity existAdminScheduleEntity) {
        AdminScheduleEntity findOneSchedule = queryFactory
                .selectFrom(adminScheduleEntity)
                .orderBy(adminScheduleEntity.idx.desc())
                .where(adminScheduleEntity.visible.eq("Y")
                        .and(adminScheduleEntity.idx.eq(existAdminScheduleEntity.getIdx())))
                .fetchOne();

        return INSTANCE.toDto(findOneSchedule);
    }

    /**
     * <pre>
     * 1. MethodName : insertSchedule
     * 2. ClassName  : AdminScheduleJpaRepository.java
     * 3. Comment    : 관리자 모델 스케줄 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 31.
     * </pre>
     */
    public AdminScheduleDTO insertSchedule(AdminScheduleEntity adminScheduleEntity) {
        em.persist(adminScheduleEntity);
        return INSTANCE.toDto(adminScheduleEntity);
    }

    /**
     * <pre>
     * 1. MethodName : updateSchedule
     * 2. ClassName  : AdminScheduleJpaRepository.java
     * 3. Comment    : 관리자 모델 스케줄 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 31.
     * </pre>
     */
    public AdminScheduleDTO updateSchedule(AdminScheduleEntity existAdminScheduleEntity) {
        em.merge(existAdminScheduleEntity);
        em.flush();
        em.clear();
        return INSTANCE.toDto(existAdminScheduleEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deleteSchedule
     * 2. ClassName  : AdminScheduleJpaRepository.java
     * 3. Comment    : 관리자 모델 스케줄 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 31.
     * </pre>
     */
    public Integer deleteSchedule(Integer idx) {
        em.remove(em.find(AdminScheduleEntity.class, idx));
        em.flush();
        em.clear();
        return idx;
    }
}
