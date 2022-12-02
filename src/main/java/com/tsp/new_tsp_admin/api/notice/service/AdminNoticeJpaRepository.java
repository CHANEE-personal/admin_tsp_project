package com.tsp.new_tsp_admin.api.notice.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.domain.notice.AdminNoticeDTO;
import com.tsp.new_tsp_admin.api.domain.notice.AdminNoticeEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_admin.api.domain.notice.QAdminNoticeEntity.*;
import static com.tsp.new_tsp_admin.api.notice.mapper.NoticeMapper.INSTANCE;
import static com.tsp.new_tsp_admin.common.StringUtil.getInt;
import static com.tsp.new_tsp_admin.common.StringUtil.getString;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AdminNoticeJpaRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private BooleanExpression searchNotice(Map<String, Object> noticeMap) {
        String searchType = getString(noticeMap.get("searchType"), "");
        String searchKeyword = getString(noticeMap.get("searchKeyword"), "");

        if ("0".equals(searchType)) {
            return adminNoticeEntity.title.contains(searchKeyword)
                    .or(adminNoticeEntity.description.contains(searchKeyword));
        } else if ("1".equals(searchType)) {
            return adminNoticeEntity.title.contains(searchKeyword);
        } else {
            return adminNoticeEntity.description.contains(searchKeyword);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findNoticeCount
     * 2. ClassName  : AdminNoticeJpaRepository.java
     * 3. Comment    : 관리자 공지사항 리스트 갯수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 16.
     * </pre>
     */
    public Integer findNoticeCount(Map<String, Object> noticeMap) {
        return queryFactory.selectFrom(adminNoticeEntity).where(searchNotice(noticeMap)).fetch().size();
    }

    /**
     * <pre>
     * 1. MethodName : findNoticeList
     * 2. ClassName  : AdminNoticeJpaRepository.java
     * 3. Comment    : 관리자 공지사항 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 16.
     * </pre>
     */
    public List<AdminNoticeDTO> findNoticeList(Map<String, Object> noticeMap) {
        List<AdminNoticeEntity> noticeList = queryFactory
                .selectFrom(adminNoticeEntity)
                .orderBy(adminNoticeEntity.idx.desc())
                .where(searchNotice(noticeMap))
                .offset(getInt(noticeMap.get("jpaStartPage"), 0))
                .limit(getInt(noticeMap.get("size"), 0))
                .fetch();

        noticeList.forEach(list -> noticeList.get(noticeList.indexOf(list))
                .setRnum(getInt(noticeMap.get("startPage"), 1) * (getInt(noticeMap.get("size"), 1)) - (2 - noticeList.indexOf(list))));

        return INSTANCE.toDtoList(noticeList);
    }

    /**
     * <pre>
     * 1. MethodName : findOneNotice
     * 2. ClassName  : AdminNoticeJpaRepository.java
     * 3. Comment    : 관리자 공지사항 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 16.
     * </pre>
     */
    AdminNoticeDTO findOneNotice(Long idx) {
        AdminNoticeEntity findOneNotice = queryFactory
                .selectFrom(adminNoticeEntity)
                .orderBy(adminNoticeEntity.idx.desc())
                .where(adminNoticeEntity.idx.eq(idx)
                        .and(adminNoticeEntity.visible.eq("Y")))
                .fetchOne();

        return INSTANCE.toDto(findOneNotice);
    }

    /**
     * <pre>
     * 1. MethodName : findPrevOneNotice
     * 2. ClassName  : AdminNoticeJpaRepository.java
     * 3. Comment    : 관리자 이전 공지사항 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 18.
     * </pre>
     */
    AdminNoticeDTO findPrevOneNotice(Long idx) {
        AdminNoticeEntity findPrevOneNotice = queryFactory
                .selectFrom(adminNoticeEntity)
                .orderBy(adminNoticeEntity.idx.desc())
                .where(adminNoticeEntity.idx.lt(idx)
                        .and(adminNoticeEntity.visible.eq("Y")))
                .fetchFirst();

        return INSTANCE.toDto(findPrevOneNotice);
    }

    /**
     * <pre>
     * 1. MethodName : findNextOneNotice
     * 2. ClassName  : AdminNoticeJpaRepository.java
     * 3. Comment    : 관리자 이전 공지사항 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 18.
     * </pre>
     */
    AdminNoticeDTO findNextOneNotice(Long idx) {
        AdminNoticeEntity findNextOneNotice = queryFactory
                .selectFrom(adminNoticeEntity)
                .orderBy(adminNoticeEntity.idx.desc())
                .where(adminNoticeEntity.idx.gt(idx)
                        .and(adminNoticeEntity.visible.eq("Y")))
                .fetchFirst();

        return INSTANCE.toDto(findNextOneNotice);
    }

    /**
     * <pre>
     * 1. MethodName : insertNotice
     * 2. ClassName  : AdminNoticeJpaRepository.java
     * 3. Comment    : 관리자 공지사항 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 16.
     * </pre>
     */
    public AdminNoticeDTO insertNotice(AdminNoticeEntity adminNoticeEntity) {
        em.persist(adminNoticeEntity);
        return INSTANCE.toDto(adminNoticeEntity);
    }

    /**
     * <pre>
     * 1. MethodName : updateNotice
     * 2. ClassName  : AdminNoticeJpaRepository.java
     * 3. Comment    : 관리자 공지사항 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 16.
     * </pre>
     */
    public AdminNoticeDTO updateNotice(AdminNoticeEntity existAdminNoticeEntity) {
        em.merge(existAdminNoticeEntity);
        em.flush();
        em.clear();
        return INSTANCE.toDto(existAdminNoticeEntity);
    }

    /**
     * <pre>
     * 1. MethodName : toggleFixed
     * 2. ClassName  : AdminNoticeJpaRepository.java
     * 3. Comment    : 관리자 공지사항 상단 고정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 09. 23.
     * </pre>
     */
    public AdminNoticeDTO toggleFixed(Long idx) {
        Boolean fixed = !em.find(AdminNoticeEntity.class, idx).getTopFixed();

        queryFactory
                .update(adminNoticeEntity)
                .where(adminNoticeEntity.idx.eq(idx))
                .set(adminNoticeEntity.topFixed, fixed)
                .execute();

        em.flush();
        em.clear();

        return INSTANCE.toDto(em.find(AdminNoticeEntity.class, idx));
    }

    /**
     * <pre>
     * 1. MethodName : deleteNotice
     * 2. ClassName  : AdminNoticeJpaRepository.java
     * 3. Comment    : 관리자 공지사항 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 08. 16.
     * </pre>
     */
    public Long deleteNotice(Long idx) {
        em.remove(em.find(AdminNoticeEntity.class, idx));
        em.flush();
        em.clear();
        return idx;
    }
}
