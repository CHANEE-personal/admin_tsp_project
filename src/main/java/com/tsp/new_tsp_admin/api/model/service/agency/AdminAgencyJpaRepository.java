package com.tsp.new_tsp_admin.api.model.service.agency;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.domain.model.agency.AdminAgencyDTO;
import com.tsp.new_tsp_admin.api.domain.model.agency.AdminAgencyEntity;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.*;

import static com.tsp.new_tsp_admin.api.domain.model.agency.AdminAgencyEntity.toDto;
import static com.tsp.new_tsp_admin.api.domain.model.agency.AdminAgencyEntity.toDtoList;
import static com.tsp.new_tsp_admin.api.domain.model.agency.QAdminAgencyEntity.adminAgencyEntity;
import static com.tsp.new_tsp_admin.common.StringUtil.getInt;
import static com.tsp.new_tsp_admin.common.StringUtil.getString;
import static com.tsp.new_tsp_admin.exception.ApiExceptionType.NOT_FOUND_AGENCY;
import static java.util.Collections.emptyList;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AdminAgencyJpaRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private BooleanExpression searchAgency(Map<String, Object> agencyMap) {
        String searchType = getString(agencyMap.get("searchType"), "");
        String searchKeyword = getString(agencyMap.get("searchKeyword"), "");

        if (!Objects.equals(searchKeyword, "")) {
            return "0".equals(searchType) ?
                    adminAgencyEntity.agencyName.contains(searchKeyword)
                            .or(adminAgencyEntity.agencyDescription.contains(searchKeyword)) :
                    "1".equals(searchType) ?
                            adminAgencyEntity.agencyName.contains(searchKeyword) :
                            adminAgencyEntity.agencyDescription.contains(searchKeyword);
        } else {
            return null;
        }
    }

    /**
     * <pre>
     * 1. MethodName : findAgencyCount
     * 2. ClassName  : AdminAgencyJpaRepository.java
     * 3. Comment    : 관리자 모델 소속사 리스트 갯수 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 14.
     * </pre>
     */
    public int findAgencyCount(Map<String, Object> agencyMap) {
        return queryFactory.selectFrom(adminAgencyEntity).where(searchAgency(agencyMap)).fetch().size();
    }

    /**
     * <pre>
     * 1. MethodName : findAgencyList
     * 2. ClassName  : AdminAgencyJpaRepository.java
     * 3. Comment    : 관리자 모델 소속사 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 14.
     * </pre>
     */
    public List<AdminAgencyDTO> findAgencyList(Map<String, Object> agencyMap) {
        List<AdminAgencyEntity> agencyList = queryFactory
                .selectFrom(adminAgencyEntity)
                .orderBy(adminAgencyEntity.idx.desc())
                .where(searchAgency(agencyMap))
                .where(adminAgencyEntity.visible.eq("Y"))
                .offset(getInt(agencyMap.get("jpaStartPage"), 0))
                .limit(getInt(agencyMap.get("size"), 0))
                .fetch();

        return agencyList != null ? toDtoList(agencyList) : emptyList();
    }

    /**
     * <pre>
     * 1. MethodName : findOneAgency
     * 2. ClassName  : AdminAgencyJpaRepository.java
     * 3. Comment    : 관리자 모델 소속사 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 14.
     * </pre>
     */
    public AdminAgencyDTO findOneAgency(Long idx) {
        AdminAgencyEntity findOneAgency = Optional.ofNullable(queryFactory
                .selectFrom(adminAgencyEntity)
                .orderBy(adminAgencyEntity.idx.desc())
                .where(adminAgencyEntity.visible.eq("Y")
                        .and(adminAgencyEntity.idx.eq(idx)))
                .fetchOne()).orElseThrow(() -> new TspException(NOT_FOUND_AGENCY, new Throwable()));

        return toDto(findOneAgency);
    }

    /**
     * <pre>
     * 1. MethodName : insertAgency
     * 2. ClassName  : AdminAgencyRepository.java
     * 3. Comment    : 관리자 모델 소속사 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 14.
     * </pre>
     */
    public AdminAgencyDTO insertAgency(AdminAgencyEntity adminAgencyEntity) {
        em.persist(adminAgencyEntity);
        return toDto(adminAgencyEntity);
    }

    /**
     * <pre>
     * 1. MethodName : updateAgency
     * 2. ClassName  : AdminAgencyRepository.java
     * 3. Comment    : 관리자 모델 소속사 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 14.
     * </pre>
     */
    public AdminAgencyDTO updateAgency(AdminAgencyEntity existAdminAgencyEntity) {
        em.merge(existAdminAgencyEntity);
        return toDto(existAdminAgencyEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deleteAgency
     * 2. ClassName  : AdminAgencyRepository.java
     * 3. Comment    : 관리자 모델 소속사 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 14.
     * </pre>
     */
    public Long deleteAgency(Long idx) {
        em.remove(em.find(AdminAgencyEntity.class, idx));
        return idx;
    }
}
