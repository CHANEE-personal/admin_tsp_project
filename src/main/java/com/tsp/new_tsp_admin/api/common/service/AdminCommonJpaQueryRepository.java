package com.tsp.new_tsp_admin.api.common.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.domain.common.CommonCodeDTO;
import com.tsp.new_tsp_admin.api.domain.common.CommonCodeEntity;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.tsp.new_tsp_admin.api.domain.common.CommonCodeEntity.toDto;
import static com.tsp.new_tsp_admin.api.domain.common.CommonCodeEntity.toDtoList;
import static com.tsp.new_tsp_admin.api.domain.common.QCommonCodeEntity.commonCodeEntity;
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
    public List<CommonCodeDTO> findCommonCodeList(Map<String, Object> commonMap) {
        List<CommonCodeEntity> commonCodeList = queryFactory
                .selectFrom(commonCodeEntity)
                .fetch();

        return commonCodeList != null ? toDtoList(commonCodeList) : emptyList();
    }

    /**
     * <pre>
     * 1. MethodName : findOneCommonCode
     * 2. ClassName  : AdminCommonJpaRepository.java
     * 3. Comment    : 관리자 공통 코드 상세 조회
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    public CommonCodeDTO findOneCommonCode(Long idx) {
        //모델 상세 조회
        CommonCodeEntity findOneCommonCode = Optional.ofNullable(queryFactory
                .selectFrom(commonCodeEntity)
                .orderBy(commonCodeEntity.idx.desc())
                .where(commonCodeEntity.idx.eq(idx)
                        .and(commonCodeEntity.visible.eq("Y")))
                .fetchOne()).orElseThrow(() -> new TspException(NOT_FOUND_COMMON));

        return toDto(findOneCommonCode);
    }

    /**
     * <pre>
     * 1. MethodName : insertCommonCode
     * 2. ClassName  : AdminCommonJpaRepository.java
     * 3. Comment    : 관리자 공통 코드 등록
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    public CommonCodeDTO insertCommonCode(CommonCodeEntity commonCodeEntity) {
        em.persist(commonCodeEntity);
        return toDto(commonCodeEntity);
    }

    /**
     * <pre>
     * 1. MethodName : updateCommonCode
     * 2. ClassName  : AdminCommonJpaRepository.java
     * 3. Comment    : 관리자 공통 코드 수정
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    public CommonCodeDTO updateCommonCode(CommonCodeEntity existCommonCodeEntity) {
        em.merge(existCommonCodeEntity);
        return toDto(existCommonCodeEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deleteCommonCode
     * 2. ClassName  : AdminCommonJpaRepository.java
     * 3. Comment    : 관리자 공통 코드 삭제
     * 4. 작성자      : CHO
     * 5. 작성일      : 2022. 05. 02.
     * </pre>
     */
    public Long deleteCommonCode(Long idx) {
        em.remove(em.find(CommonCodeEntity.class, idx));
        return idx;
    }
}
