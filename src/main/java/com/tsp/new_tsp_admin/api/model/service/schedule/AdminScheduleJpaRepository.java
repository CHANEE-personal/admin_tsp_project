package com.tsp.new_tsp_admin.api.model.service.schedule;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.domain.model.schedule.AdminScheduleDTO;
import com.tsp.new_tsp_admin.api.domain.model.schedule.AdminScheduleEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_admin.api.domain.model.schedule.QAdminScheduleEntity.adminScheduleEntity;
import static com.tsp.new_tsp_admin.api.model.mapper.schedule.ScheduleMapper.*;
import static com.tsp.new_tsp_admin.common.StringUtil.getInt;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AdminScheduleJpaRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    /**
     * <pre>
     * 1. MethodName : findScheduleCount
     * 2. ClassName  : AdminScheduleJpaRepository.java
     * 3. Comment    : 관리자 모델 스케줄 리스트 갯수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 31.
     * </pre>
     */
    public Integer findScheduleCount() {
        return queryFactory.selectFrom(adminScheduleEntity).fetch().size();
    }

    /**
     * <pre>
     * 1. MethodName : findScheduleList
     * 2. ClassName  : AdminScheduleJpaRepository.java
     * 3. Comment    : 관리자 모델 스케줄 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 31.
     * </pre>
     */
    public List<AdminScheduleDTO> findScheduleList(Map<String, Object> scheduleMap) {
        List<AdminScheduleEntity> scheduleList = queryFactory
                .selectFrom(adminScheduleEntity)
                .orderBy(adminScheduleEntity.idx.desc())
                .where(adminScheduleEntity.visible.eq("Y"))
                .offset(getInt(scheduleMap.get("jpaStartPage"), 0))
                .limit(getInt(scheduleMap.get("size"), 0))
                .fetch();

        scheduleList.forEach(list -> scheduleList.get(scheduleList.indexOf(list))
                .setRnum(getInt(scheduleMap.get("startPage"), 1) * (getInt(scheduleMap.get("size"), 1)) - (2 - scheduleList.indexOf(list))));

        return INSTANCE.toDtoList(scheduleList);
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
