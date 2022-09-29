package com.tsp.new_tsp_admin.api.model.service.negotiation;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelDTO;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import com.tsp.new_tsp_admin.api.domain.model.negotiation.AdminNegotiationDTO;
import com.tsp.new_tsp_admin.api.domain.model.negotiation.AdminNegotiationEntity;
import com.tsp.new_tsp_admin.api.model.mapper.ModelMapper;
import com.tsp.new_tsp_admin.api.model.mapper.negotiation.NegotiationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_admin.api.domain.model.QAdminModelEntity.adminModelEntity;
import static com.tsp.new_tsp_admin.api.domain.model.negotiation.QAdminNegotiationEntity.*;
import static com.tsp.new_tsp_admin.api.domain.model.schedule.QAdminScheduleEntity.adminScheduleEntity;
import static com.tsp.new_tsp_admin.common.StringUtil.getInt;
import static com.tsp.new_tsp_admin.common.StringUtil.getString;
import static java.time.LocalDate.now;
import static java.time.LocalDateTime.of;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AdminNegotiationJpaRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private BooleanExpression searchNegotiation(Map<String, Object> negotiationMap) {
        String searchKeyword = getString(negotiationMap.get("searchKeyword"), "");
        LocalDateTime searchStartTime = (LocalDateTime) negotiationMap.get("searchStartTime");
        LocalDateTime searchEndTime = (LocalDateTime) negotiationMap.get("searchEndTime");

        if (searchStartTime != null && searchEndTime != null) {
            searchStartTime = (LocalDateTime) negotiationMap.get("searchStartTime");
            searchEndTime = (LocalDateTime) negotiationMap.get("searchEndTime");
        } else {
            searchStartTime = now().minusDays(now().getDayOfMonth()-1).atStartOfDay();
            searchEndTime = of(now().minusDays(now().getDayOfMonth()).plusMonths(1), LocalTime.of(23,59,59));
        }

        if (!"".equals(searchKeyword)) {
            return adminModelEntity.modelKorName.contains(searchKeyword)
                    .or(adminModelEntity.modelEngName.contains(searchKeyword)
                            .or(adminModelEntity.modelDescription.contains(searchKeyword)))
                    .or(adminNegotiationEntity.modelNegotiationDesc.contains(searchKeyword));
        } else {
            return adminNegotiationEntity.modelNegotiationDate.between(searchStartTime, searchEndTime);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findModelNegotiationCount
     * 2. ClassName  : AdminNegotiationJpaRepository.java
     * 3. Comment    : 관리자 모델 섭외 리스트 갯수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 09.
     * </pre>
     */
    public Integer findNegotiationCount(Map<String, Object> negotiationMap) {
        return queryFactory.selectFrom(adminNegotiationEntity)
                .where(searchNegotiation(negotiationMap))
                .fetch().size();
    }

    /**
     * <pre>
     * 1. MethodName : findModelNegotiationList
     * 2. ClassName  : AdminNegotiationJpaRepository.java
     * 3. Comment    : 관리자 모델 섭외 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 09.
     * </pre>
     */
    public List<AdminModelDTO> findModelNegotiationList(Map<String, Object> negotiationMap) {
        List<AdminModelEntity> modelNegotiationList = queryFactory
                .selectFrom(adminModelEntity)
                .orderBy(adminScheduleEntity.idx.desc())
                .leftJoin(adminModelEntity.negotiationList, adminNegotiationEntity)
                .fetchJoin()
                .where(searchNegotiation(negotiationMap)
                        .and(adminModelEntity.visible.eq("Y"))
                        .and(adminScheduleEntity.visible.eq("Y")))
                .offset(getInt(negotiationMap.get("jpaStartPage"), 0))
                .limit(getInt(negotiationMap.get("size"), 0))
                .fetch();

        modelNegotiationList.forEach(list -> modelNegotiationList.get(modelNegotiationList.indexOf(list))
                .setRnum(getInt(negotiationMap.get("startPage"), 1) * (getInt(negotiationMap.get("size"), 1)) - (2 - modelNegotiationList.indexOf(list))));

        return ModelMapper.INSTANCE.toDtoList(modelNegotiationList);
    }

    /**
     * <pre>
     * 1. MethodName : findOneNegotiation
     * 2. ClassName  : AdminNegotiationJpaRepository.java
     * 3. Comment    : 관리자 모델 섭외 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 09.
     * </pre>
     */
    public AdminNegotiationDTO findOneNegotiation(AdminNegotiationEntity existAdminNegotiationEntity) {
        AdminNegotiationEntity findOneNegotiation = queryFactory
                .selectFrom(adminNegotiationEntity)
                .orderBy(adminNegotiationEntity.idx.desc())
                .where(adminNegotiationEntity.visible.eq("Y")
                        .and(adminNegotiationEntity.idx.eq(existAdminNegotiationEntity.getIdx())))
                .fetchOne();

        return NegotiationMapper.INSTANCE.toDto(findOneNegotiation);
    }

    /**
     * <pre>
     * 1. MethodName : findOneModelNegotiation
     * 2. ClassName  : AdminNegotiationJpaRepository.java
     * 3. Comment    : 관리자 모델 섭외 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 09.
     * </pre>
     */
    public AdminModelDTO findOneModelNegotiation(AdminNegotiationEntity existAdminNegotiationEntity) {
        AdminModelEntity findOneModelNegotiation = queryFactory
                .selectFrom(adminModelEntity)
                .leftJoin(adminModelEntity.negotiationList, adminNegotiationEntity)
                .fetchJoin()
                .where(adminModelEntity.visible.eq("Y")
                        .and(adminNegotiationEntity.visible.eq("Y"))
                        .and(adminModelEntity.idx.eq(existAdminNegotiationEntity.getModelIdx()))
                        .and(adminNegotiationEntity.idx.eq(existAdminNegotiationEntity.getIdx())))
                .fetchOne();

        return ModelMapper.INSTANCE.toDto(findOneModelNegotiation);
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneNegotiation
     * 2. ClassName  : AdminNegotiationJpaRepository.java
     * 3. Comment    : 관리자 이전 모델 섭외 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 21.
     * </pre>
     */
    public AdminNegotiationDTO findPrevOneNegotiation(AdminNegotiationEntity existAdminNegotiationEntity) {
        // 이전 모델 섭외 조회
        AdminNegotiationEntity findPrevOneNegotiation = queryFactory
                .selectFrom(adminNegotiationEntity)
                .orderBy(adminNegotiationEntity.idx.desc())
                .where(adminNegotiationEntity.idx.lt(existAdminNegotiationEntity.getIdx())
                        .and(adminNegotiationEntity.visible.eq("Y")))
                .fetchFirst();

        return NegotiationMapper.INSTANCE.toDto(findPrevOneNegotiation);
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneNegotiation
     * 2. ClassName  : AdminNegotiationJpaRepository.java
     * 3. Comment    : 관리자 다음 모델 섭외 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 21.
     * </pre>
     */
    public AdminNegotiationDTO findNextOneNegotiation(AdminNegotiationEntity existAdminNegotiationEntity) {
        // 다음 모델 섭외 조회
        AdminNegotiationEntity findNextOneNegotiation = queryFactory
                .selectFrom(adminNegotiationEntity)
                .orderBy(adminNegotiationEntity.idx.asc())
                .where(adminNegotiationEntity.idx.gt(existAdminNegotiationEntity.getIdx())
                        .and(adminNegotiationEntity.visible.eq("Y")))
                .fetchFirst();

        return NegotiationMapper.INSTANCE.toDto(findNextOneNegotiation);
    }

    /**
     * <pre>
     * 1. MethodName : insertModelNegotiation
     * 2. ClassName  : AdminNegotiationJpaRepository.java
     * 3. Comment    : 관리자 모델 섭외 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 09.
     * </pre>
     */
    public AdminNegotiationDTO insertModelNegotiation(AdminNegotiationEntity adminNegotiationEntity) {
        em.persist(adminNegotiationEntity);
        return NegotiationMapper.INSTANCE.toDto(adminNegotiationEntity);
    }

    /**
     * <pre>
     * 1. MethodName : updateModelNegotiation
     * 2. ClassName  : AdminNegotiationJpaRepository.java
     * 3. Comment    : 관리자 모델 섭외 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 09.
     * </pre>
     */
    public AdminNegotiationDTO updateModelNegotiation(AdminNegotiationEntity existAdminNegotiationEntity) {
        em.merge(existAdminNegotiationEntity);
        em.flush();
        em.clear();
        return NegotiationMapper.INSTANCE.toDto(existAdminNegotiationEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deleteModelNegotiation
     * 2. ClassName  : AdminNegotiationJpaRepository.java
     * 3. Comment    : 관리자 모델 섭외 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 09.
     * </pre>
     */
    public Long deleteModelNegotiation(Long idx) {
        em.remove(em.find(AdminNegotiationEntity.class, idx));
        em.flush();
        em.clear();
        return idx;
    }
}
