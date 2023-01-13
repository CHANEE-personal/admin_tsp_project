package com.tsp.new_tsp_admin.api.model.service.negotiation;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelDTO;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import com.tsp.new_tsp_admin.api.domain.model.negotiation.AdminNegotiationDTO;
import com.tsp.new_tsp_admin.api.domain.model.negotiation.AdminNegotiationEntity;
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
import static com.tsp.new_tsp_admin.api.domain.model.negotiation.AdminNegotiationEntity.toDto;
import static com.tsp.new_tsp_admin.api.domain.model.negotiation.AdminNegotiationEntity.toDtoList;
import static com.tsp.new_tsp_admin.api.domain.model.negotiation.QAdminNegotiationEntity.*;
import static com.tsp.new_tsp_admin.common.StringUtil.getInt;
import static com.tsp.new_tsp_admin.common.StringUtil.getString;
import static com.tsp.new_tsp_admin.exception.ApiExceptionType.NOT_FOUND_MODEL_NEGOTIATION;
import static java.time.LocalDate.now;
import static java.time.LocalDateTime.of;
import static java.util.Collections.emptyList;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AdminNegotiationJpaQueryRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private BooleanExpression searchNegotiation(Map<String, Object> negotiationMap) {
        String searchKeyword = getString(negotiationMap.get("searchKeyword"), "");
        LocalDateTime searchStartTime = negotiationMap.get("searchStartTime") != null ? (LocalDateTime) negotiationMap.get("searchStartTime") : now().minusDays(now().getDayOfMonth() - 1).atStartOfDay();
        LocalDateTime searchEndTime = negotiationMap.get("searchEndTime") != null ? (LocalDateTime) negotiationMap.get("searchStartTime") : of(now().minusDays(now().getDayOfMonth()).plusMonths(1), LocalTime.of(23, 59, 59));

        return !Objects.equals(searchKeyword, "") ?
                    adminNegotiationEntity.modelNegotiationDesc.contains(searchKeyword) :
                adminNegotiationEntity.modelNegotiationDate.between(searchStartTime, searchEndTime);
    }

    /**
     * <pre>
     * 1. MethodName : findModelNegotiationCount
     * 2. ClassName  : AdminNegotiationJpaRepository.java
     * 3. Comment    : 관리자 모델 섭외 리스트 갯수 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 09.
     * </pre>
     */
    public int findNegotiationCount(Map<String, Object> negotiationMap) {
        return queryFactory.selectFrom(adminNegotiationEntity)
                .where(searchNegotiation(negotiationMap))
                .fetch().size();
    }

    /**
     * <pre>
     * 1. MethodName : findNegotiationList
     * 2. ClassName  : AdminNegotiationJpaRepository.java
     * 3. Comment    : 관리자 모델 섭외 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 09.
     * </pre>
     */
    public List<AdminNegotiationDTO> findNegotiationList(Map<String, Object> negotiationMap) {
        List<AdminNegotiationEntity> negotiationList = queryFactory
                .selectFrom(adminNegotiationEntity)
                .orderBy(adminNegotiationEntity.idx.desc())
                .where(searchNegotiation(negotiationMap)
                        .and(adminNegotiationEntity.visible.eq("Y")))
                .offset(getInt(negotiationMap.get("jpaStartPage"), 0))
                .limit(getInt(negotiationMap.get("size"), 0))
                .fetch();

        return negotiationList != null ? toDtoList(negotiationList) : emptyList();
    }

    /**
     * <pre>
     * 1. MethodName : findOneNegotiation
     * 2. ClassName  : AdminNegotiationJpaRepository.java
     * 3. Comment    : 관리자 모델 섭외 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일       : 2022. 09. 09.
     * </pre>
     */
    public AdminNegotiationDTO findOneNegotiation(Long idx) {
        AdminNegotiationEntity findOneNegotiation = Optional.ofNullable(queryFactory
                .selectFrom(adminNegotiationEntity)
                .orderBy(adminNegotiationEntity.idx.desc())
                .where(adminNegotiationEntity.visible.eq("Y")
                        .and(adminNegotiationEntity.idx.eq(idx)))
                .fetchOne()).orElseThrow(() -> new TspException(NOT_FOUND_MODEL_NEGOTIATION));

        return toDto(findOneNegotiation);
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneNegotiation
     * 2. ClassName  : AdminNegotiationJpaRepository.java
     * 3. Comment    : 관리자 이전 모델 섭외 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 21.
     * </pre>
     */
    public AdminNegotiationDTO findPrevOneNegotiation(Long idx) {
        // 이전 모델 섭외 조회
        AdminNegotiationEntity findPrevOneNegotiation = Optional.ofNullable(queryFactory
                .selectFrom(adminNegotiationEntity)
                .orderBy(adminNegotiationEntity.idx.desc())
                .where(adminNegotiationEntity.idx.lt(idx)
                        .and(adminNegotiationEntity.visible.eq("Y")))
                .fetchFirst()).orElseThrow(() -> new TspException(NOT_FOUND_MODEL_NEGOTIATION));

        return toDto(findPrevOneNegotiation);
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneNegotiation
     * 2. ClassName  : AdminNegotiationJpaRepository.java
     * 3. Comment    : 관리자 다음 모델 섭외 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 09. 21.
     * </pre>
     */
    public AdminNegotiationDTO findNextOneNegotiation(Long idx) {
        // 다음 모델 섭외 조회
        AdminNegotiationEntity findNextOneNegotiation = Optional.ofNullable(queryFactory
                .selectFrom(adminNegotiationEntity)
                .orderBy(adminNegotiationEntity.idx.asc())
                .where(adminNegotiationEntity.idx.gt(idx)
                        .and(adminNegotiationEntity.visible.eq("Y")))
                .fetchFirst()).orElseThrow(() -> new TspException(NOT_FOUND_MODEL_NEGOTIATION));

        return toDto(findNextOneNegotiation);
    }
}
