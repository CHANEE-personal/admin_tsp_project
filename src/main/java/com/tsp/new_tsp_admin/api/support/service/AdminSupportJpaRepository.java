package com.tsp.new_tsp_admin.api.support.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.domain.support.AdminSupportDTO;
import com.tsp.new_tsp_admin.api.domain.support.AdminSupportEntity;
import com.tsp.new_tsp_admin.api.support.mapper.SupportMapper;
import com.tsp.new_tsp_admin.common.StringUtil;
import com.tsp.new_tsp_admin.exception.ApiExceptionType;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_admin.api.domain.support.QAdminSupportEntity.adminSupportEntity;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AdminSupportJpaRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    private BooleanExpression searchSupport(Map<String, Object> supportMap) {
        String searchType = StringUtil.getString(supportMap.get("searchType"),"");
        String searchKeyword = StringUtil.getString(supportMap.get("searchKeyword"),"");

        if ("0".equals(searchType)) {
            return adminSupportEntity.supportName.contains(searchKeyword)
                    .or(adminSupportEntity.supportMessage.contains(searchKeyword));
        } else if ("1".equals(searchType)) {
            return adminSupportEntity.supportName.contains(searchKeyword);
        } else {
            return adminSupportEntity.supportMessage.contains(searchKeyword);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findSupportsCount
     * 2. ClassName  : AdminSupportJpaRepository.java
     * 3. Comment    : 관리자 지원모델 리스트 갯수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     */
    public int findSupportsCount(Map<String, Object> supportMap) {

        try {
            return queryFactory.selectFrom(adminSupportEntity)
                    .where(searchSupport(supportMap))
                    .fetch().size();
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.NOT_FOUND_SUPPORT_LIST);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findSupportsList
     * 2. ClassName  : AdminSupportJpaRepository.java
     * 3. Comment    : 관리자 지원모델 리스트 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     */
    public List<AdminSupportDTO> findSupportsList(Map<String, Object> supportMap) {

        try {
            List<AdminSupportEntity> supportList = queryFactory.selectFrom(adminSupportEntity)
                    .where(searchSupport(supportMap))
                    .orderBy(adminSupportEntity.idx.desc())
                    .offset(StringUtil.getInt(supportMap.get("jpaStartPage"),0))
                    .limit(StringUtil.getInt(supportMap.get("size"),0))
                    .fetch();

            supportList.forEach(list -> supportList.get(supportList.indexOf(list)).setRnum(StringUtil.getInt(supportMap.get("startPage"),1)*(StringUtil.getInt(supportMap.get("size"),1))-(2-supportList.indexOf(list))));

            return SupportMapper.INSTANCE.toDtoList(supportList);
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.NOT_FOUND_SUPPORT_LIST);
        }
    }

    /**
     * <pre>
     * 1. MethodName : findOneSupportModel
     * 2. ClassName  : AdminSupportJpaRepository.java
     * 3. Comment    : 관리자 지원모델 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     */
    public AdminSupportDTO findOneSupportModel(AdminSupportEntity existAdminSupportEntity) {

        try {
            //모델 상세 조회
            AdminSupportEntity findOneSupportModel = queryFactory.selectFrom(adminSupportEntity)
                    .where(adminSupportEntity.idx.eq(existAdminSupportEntity.getIdx()))
                    .fetchOne();

            return SupportMapper.INSTANCE.toDto(findOneSupportModel);
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.NOT_FOUND_SUPPORT);
        }
    }

    /**
     * <pre>
     * 1. MethodName : updateSupportModel
     * 2. ClassName  : AdminSupportJpaRepository.java
     * 3. Comment    : 관리자 지원모델 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    public AdminSupportDTO updateSupportModel(AdminSupportEntity existAdminSupportEntity) {
        try {
            em.merge(existAdminSupportEntity);
            em.flush();
            em.clear();

            return SupportMapper.INSTANCE.toDto(existAdminSupportEntity);
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.ERROR_UPDATE_SUPPORT);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteSupportModel
     * 2. ClassName  : AdminSupportJpaRepository.java
     * 3. Comment    : 관리자 지원모델 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     *
     */
    @Modifying(clearAutomatically = true)
    @Transactional
    public AdminSupportDTO deleteSupportModel(AdminSupportEntity adminSupportEntity) {

        try {
            adminSupportEntity = em.find(AdminSupportEntity.class, adminSupportEntity.getIdx());
            em.remove(adminSupportEntity);
            em.flush();
            em.clear();

            return SupportMapper.INSTANCE.toDto(adminSupportEntity);
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.ERROR_DELETE_SUPPORT);
        }
    }
}
