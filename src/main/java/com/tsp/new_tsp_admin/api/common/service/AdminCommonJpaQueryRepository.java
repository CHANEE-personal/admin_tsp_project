package com.tsp.new_tsp_admin.api.common.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.domain.common.*;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.tsp.new_tsp_admin.api.domain.common.CommonCodeEntity.toDto;
import static com.tsp.new_tsp_admin.api.domain.common.CommonCodeEntity.toDtoList;
import static com.tsp.new_tsp_admin.api.domain.common.QCommonCodeEntity.commonCodeEntity;
import static com.tsp.new_tsp_admin.api.domain.common.QNewCodeEntity.newCodeEntity;
import static com.tsp.new_tsp_admin.exception.ApiExceptionType.NOT_FOUND_COMMON;
import static java.util.Collections.emptyList;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AdminCommonJpaQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    /**
     * <pre>
     * 1. MethodName : findCommonCodeListCount
     * 2. ClassName  : AdminCommonJpaRepository.java
     * 3. Comment    : 관리자 공통 코드 리스트 갯수 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    public int findCommonCodeListCount(Map<String, Object> commonMap) {
        return queryFactory.selectFrom(commonCodeEntity).fetch().size();
    }


    /**
     * <pre>
     * 1. MethodName : findCommonCodeList
     * 2. ClassName  : AdminCommonJpaRepository.java
     * 3. Comment    : 관리자 공통 코드 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    public Page<NewCodeDTO> findCommonCodeList(Map<String, Object> commonMap, PageRequest pageRequest) {
        List<NewCodeEntity> commonCodeList = queryFactory
                .selectFrom(newCodeEntity)
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        return new PageImpl<>(NewCodeEntity.toDtoList(commonCodeList), pageRequest, commonCodeList.size());
    }
}
