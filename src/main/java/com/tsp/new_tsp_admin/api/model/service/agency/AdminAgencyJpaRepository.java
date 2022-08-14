package com.tsp.new_tsp_admin.api.model.service.agency;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.domain.model.agency.AdminAgencyDTO;
import com.tsp.new_tsp_admin.api.domain.model.agency.AdminAgencyEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_admin.api.domain.model.agency.QAdminAgencyEntity.adminAgencyEntity;
import static com.tsp.new_tsp_admin.api.model.mapper.agency.AgencyMapper.INSTANCE;
import static com.tsp.new_tsp_admin.common.StringUtil.getInt;
import static com.tsp.new_tsp_admin.common.StringUtil.getString;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AdminAgencyJpaRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private BooleanExpression searchAgency(Map<String, Object> agencyMap) {
        String searchType = getString(agencyMap.get("searchType"), "");
        String searchKeyword = getString(agencyMap.get("searchKeyword"), "");

        if ("0".equals(searchType)) {
            return adminAgencyEntity.agencyName.contains(searchKeyword)
                    .or(adminAgencyEntity.agencyDescription.contains(searchKeyword));
        } else if ("1".equals(searchType)) {
            return adminAgencyEntity.agencyName.contains(searchKeyword);
        } else {
            return adminAgencyEntity.agencyDescription.contains(searchKeyword);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findAgencyCount
     * 2. ClassName  : AdminAgencyJpaRepository.java
     * 3. Comment    : 관리자 모델 소속사 리스트 갯수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 14.
     * </pre>
     */
    public Integer findAgencyCount(Map<String, Object> agencyMap) {
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
                .where(searchAgency(agencyMap).and(adminAgencyEntity.visible.eq("Y")))
                .offset(getInt(agencyMap.get("jpaStartPage"), 0))
                .limit(getInt(agencyMap.get("size"), 0))
                .fetch();

        agencyList.forEach(list -> agencyList.get(agencyList.indexOf(list))
                .setRnum(getInt(agencyMap.get("startPage"), 1) * (getInt(agencyMap.get("size"), 1)) - (2 - agencyList.indexOf(list))));

        return INSTANCE.toDtoList(agencyList);
    }

    /**
     * <pre>
     * 1. MethodName : findOneAgency
     * 2. ClassName  : AdminAgencyJpaRepository.java
     * 3. Comment    : 관리자 모델 소속사 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 14.
     * </pre>
     */
    public AdminAgencyDTO findOneAgency(AdminAgencyEntity existAdminAgencyEntity) {
        AdminAgencyEntity findOneAgency = queryFactory
                .selectFrom(adminAgencyEntity)
                .orderBy(adminAgencyEntity.idx.desc())
                .where(adminAgencyEntity.visible.eq("Y")
                        .and(adminAgencyEntity.idx.eq(existAdminAgencyEntity.getIdx())))
                .fetchOne();

        return INSTANCE.toDto(findOneAgency);
    }

    /**
     * <pre>
     * 1. MethodName : insertAgency
     * 2. ClassName  : AdminAgencyRepository.java
     * 3. Comment    : 관리자 모델 소속사 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 14.
     * </pre>
     */
    public AdminAgencyDTO insertAgency(AdminAgencyEntity adminAgencyEntity) {
        em.persist(adminAgencyEntity);
        return INSTANCE.toDto(adminAgencyEntity);
    }

    /**
     * <pre>
     * 1. MethodName : updateAgency
     * 2. ClassName  : AdminAgencyRepository.java
     * 3. Comment    : 관리자 모델 소속사 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 14.
     * </pre>
     */
    public AdminAgencyDTO updateAgency(AdminAgencyEntity existAdminAgencyEntity) {
        em.merge(existAdminAgencyEntity);
        em.flush();
        em.clear();
        return INSTANCE.toDto(existAdminAgencyEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deleteAgency
     * 2. ClassName  : AdminAgencyRepository.java
     * 3. Comment    : 관리자 모델 소속사 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 14.
     * </pre>
     */
    public Integer deleteAgency(Integer idx) {
        em.remove(em.find(AdminAgencyEntity.class, idx));
        em.flush();
        em.clear();
        return idx;
    }
}
