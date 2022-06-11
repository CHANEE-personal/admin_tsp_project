package com.tsp.new_tsp_admin.api.support.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.domain.support.AdminSupportDTO;
import com.tsp.new_tsp_admin.api.domain.support.AdminSupportEntity;
import com.tsp.new_tsp_admin.common.StringUtil;
import com.tsp.new_tsp_admin.exception.ApiExceptionType;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

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
     * 5. 작성일       : 2021. 09. 26.
     * </pre>
     *
     * @param supportMap
     */
    public Long findSupportsCount(Map<String, Object> supportMap) {

        try {
            return queryFactory.selectFrom(adminSupportEntity)
                    .where(searchSupport(supportMap))
                    .fetchCount();
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
     * 5. 작성일       : 2021. 09. 26.
     * </pre>
     *
     * @param supportMap
     */
    public List<AdminSupportDTO> findSupportsList(Map<String, Object> supportMap) {

        try {
            List<AdminSupportEntity> supportList = queryFactory.selectFrom(adminSupportEntity)
                    .where(searchSupport(supportMap))
                    .orderBy(adminSupportEntity.idx.desc())
                    .offset(StringUtil.getInt(supportMap.get("jpaStartPage"),0))
                    .limit(StringUtil.getInt(supportMap.get("size"),0))
                    .fetch();

            List<AdminSupportDTO> supportDtoList = SupportMapper.INSTANCE.toDtoList(supportList);

            for(int i = 0; i < supportDtoList.size(); i++) {
                supportDtoList.get(i).setRnum(StringUtil.getInt(supportMap.get("startPage"),1)*(StringUtil.getInt(supportMap.get("size"),1))-(2-i));
            }

            return supportDtoList;
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.NOT_FOUND_SUPPORT_LIST);
        }
    }
}
