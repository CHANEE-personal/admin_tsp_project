package com.tsp.new_tsp_admin.api.faq.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.domain.faq.AdminFaqDTO;
import com.tsp.new_tsp_admin.api.domain.faq.AdminFaqEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.tsp.new_tsp_admin.api.domain.faq.QAdminFaqEntity.adminFaqEntity;
import static com.tsp.new_tsp_admin.common.StringUtil.getInt;
import static com.tsp.new_tsp_admin.common.StringUtil.getString;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AdminFaqJpaRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private BooleanExpression searchFaq(Map<String, Object> faqMap) {
        String searchType = getString(faqMap.get("searchType"), "");
        String searchKeyword = getString(faqMap.get("searchKeyword"), "");

        if ("0".equals(searchType)) {
            return adminFaqEntity.title.contains(searchKeyword)
                    .or(adminFaqEntity.description.contains(searchKeyword));
        } else if ("1".equals(searchType)) {
            return adminFaqEntity.title.contains(searchKeyword);
        } else {
            return adminFaqEntity.description.contains(searchKeyword);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findFaqCount
     * 2. ClassName  : AdminFaqJpaRepository.java
     * 3. Comment    : 관리자 FAQ 리스트 갯수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 18.
     * </pre>
     */
    public Integer findFaqCount(Map<String, Object> faqMap) {
        return queryFactory.selectFrom(adminFaqEntity).where(searchFaq(faqMap)).fetch().size();
    }

    /**
     * <pre>
     * 1. MethodName : findFaqList
     * 2. ClassName  : AdminFaqJpaRepository.java
     * 3. Comment    : 관리자 FAQ 리스트 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 08. 18.
     * </pre>
     */
    public List<AdminFaqDTO> findFaqList(Map<String, Object> faqMap) {
        List<AdminFaqEntity> faqList = queryFactory
                .selectFrom(adminFaqEntity)
                .orderBy(adminFaqEntity.idx.desc())
                .where(searchFaq(faqMap))
                .offset(getInt(faqMap.get("jpaStartPage"), 0))
                .limit(getInt(faqMap.get("size"), 0))
                .fetch();

        faqList.forEach(list -> faqList.get(faqList.indexOf(list))
                .setRowNum(getInt(faqMap.get("startPage"), 1) * (getInt(faqMap.get("size"), 1)) - (2 - faqList.indexOf(list))));

        return AdminFaqEntity.toDtoList(faqList);
    }

    /**
     * <pre>
     * 1. MethodName : findOneFaq
     * 2. ClassName  : AdminFaqJpaRepository.java
     * 3. Comment    : 관리자 FAQ 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 18.
     * </pre>
     */
    public AdminFaqDTO findOneFaq(Long idx) {
        AdminFaqEntity findOneFaq = queryFactory
                .selectFrom(adminFaqEntity)
                .orderBy(adminFaqEntity.idx.desc())
                .where(adminFaqEntity.idx.eq(idx)
                        .and(adminFaqEntity.visible.eq("Y")))
                .fetchOne();

        assert findOneFaq != null;
        return AdminFaqEntity.toDto(findOneFaq);
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneFaq
     * 2. ClassName  : AdminFaqJpaRepository.java
     * 3. Comment    : 관리자 이전 FAQ 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 18.
     * </pre>
     */
    public AdminFaqDTO findPrevOneFaq(Long idx) {
        AdminFaqEntity findPrevOneFaq = queryFactory
                .selectFrom(adminFaqEntity)
                .orderBy(adminFaqEntity.idx.desc())
                .where(adminFaqEntity.idx.lt(idx)
                        .and(adminFaqEntity.visible.eq("Y")))
                .fetchOne();

        assert findPrevOneFaq != null;
        return AdminFaqEntity.toDto(findPrevOneFaq);
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneFaq
     * 2. ClassName  : AdminFaqJpaRepository.java
     * 3. Comment    : 관리자 다음 FAQ 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 18.
     * </pre>
     */
    public AdminFaqDTO findNextOneFaq(Long idx) {
        AdminFaqEntity findNextOneFaq = queryFactory
                .selectFrom(adminFaqEntity)
                .orderBy(adminFaqEntity.idx.desc())
                .where(adminFaqEntity.idx.gt(idx)
                        .and(adminFaqEntity.visible.eq("Y")))
                .fetchOne();

        assert findNextOneFaq != null;
        return AdminFaqEntity.toDto(findNextOneFaq);
    }

    /**
     * <pre>
     * 1. MethodName : insertFaq
     * 2. ClassName  : AdminFaqJpaRepository.java
     * 3. Comment    : 관리자 FAQ 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 18.
     * </pre>
     */
    public AdminFaqDTO insertFaq(AdminFaqEntity adminFaqEntity) {
        em.persist(adminFaqEntity);
        return AdminFaqEntity.toDto(adminFaqEntity);
    }

    /**
     * <pre>
     * 1. MethodName : updateFaq
     * 2. ClassName  : AdminFaqJpaRepository.java
     * 3. Comment    : 관리자 FAQ 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 18.
     * </pre>
     */
    public AdminFaqDTO updateFaq(AdminFaqEntity existAdminFaqEntity) {
        em.merge(existAdminFaqEntity);
        em.flush();
        em.clear();
        return AdminFaqEntity.toDto(existAdminFaqEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deleteFaq
     * 2. ClassName  : AdminFaqJpaRepository.java
     * 3. Comment    : 관리자 FAQ 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 18.
     * </pre>
     */
    public Long deleteFaq(Long idx) {
        em.remove(em.find(AdminFaqEntity.class, idx));
        em.flush();
        em.clear();
        return idx;
    }
}
