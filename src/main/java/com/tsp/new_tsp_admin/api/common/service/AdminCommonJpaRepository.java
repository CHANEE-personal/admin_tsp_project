package com.tsp.new_tsp_admin.api.common.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.domain.common.CommonCodeDTO;
import com.tsp.new_tsp_admin.api.domain.common.CommonCodeEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

import static com.tsp.new_tsp_admin.api.common.mapper.CommonCodeMapper.INSTANCE;
import static com.tsp.new_tsp_admin.api.domain.common.QCommonCodeEntity.commonCodeEntity;
import static com.tsp.new_tsp_admin.common.StringUtil.getInt;

@Slf4j
@RequiredArgsConstructor
@Repository
public class AdminCommonJpaRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    /**
     * <pre>
     * 1. MethodName : findCommonCodeListCount
     * 2. ClassName  : AdminCommonJpaRepository.java
     * 3. Comment    : 관리자 공통 코드 리스트 갯수 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    public Integer findCommonCodeListCount(Map<String, Object> commonMap) {
        return queryFactory.selectFrom(commonCodeEntity).fetch().size();
    }


    /**
     * <pre>
     * 1. MethodName : findCommonCodeList
     * 2. ClassName  : AdminCommonJpaRepository.java
     * 3. Comment    : 관리자 공통 코드 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    public List<CommonCodeDTO> findCommonCodeList(Map<String, Object> commonMap) {
        List<CommonCodeEntity> commonCodeList = queryFactory
                .selectFrom(commonCodeEntity)
                .fetch();

        commonCodeList.forEach(list -> commonCodeList.get(commonCodeList.indexOf(list))
                .setRnum(getInt(commonMap.get("startPage"), 1) * (getInt(commonMap.get("size"), 1)) - (2 - commonCodeList.indexOf(list))));

        return INSTANCE.toDtoList(commonCodeList);
    }

    /**
     * <pre>
     * 1. MethodName : findOneCommonCode
     * 2. ClassName  : AdminCommonJpaRepository.java
     * 3. Comment    : 관리자 공통 코드 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    public CommonCodeDTO findOneCommonCode(CommonCodeEntity existCommonCode) {
        //모델 상세 조회
        CommonCodeEntity findOneCommonCode = queryFactory
                .selectFrom(commonCodeEntity)
                .orderBy(commonCodeEntity.idx.desc())
                .where(commonCodeEntity.idx.eq(existCommonCode.getIdx())
                        .and(commonCodeEntity.visible.eq("Y")))
                .fetchOne();

        return INSTANCE.toDto(findOneCommonCode);
    }

    /**
     * <pre>
     * 1. MethodName : insertCommonCode
     * 2. ClassName  : AdminCommonJpaRepository.java
     * 3. Comment    : 관리자 공통 코드 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    public CommonCodeDTO insertCommonCode(CommonCodeEntity commonCodeEntity) {
        em.persist(commonCodeEntity);
        return INSTANCE.toDto(commonCodeEntity);
    }
}
