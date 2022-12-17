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
import java.util.stream.Collectors;

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
    public Integer findCommonCodeListCount() {
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
                .setRowNum(getInt(commonMap.get("startPage"), 1) * (getInt(commonMap.get("size"), 1)) - (2 - commonCodeList.indexOf(list))));

        return CommonCodeEntity.toDtoList(commonCodeList);
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

        assert findOneCommonCode != null;
        return CommonCodeEntity.toDto(findOneCommonCode);
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
        return CommonCodeEntity.toDto(commonCodeEntity);
    }

    /**
     * <pre>
     * 1. MethodName : updateCommonCode
     * 2. ClassName  : AdminCommonJpaRepository.java
     * 3. Comment    : 관리자 공통 코드 수정
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    public CommonCodeDTO updateCommonCode(CommonCodeEntity existCommonCodeEntity) {
        em.merge(existCommonCodeEntity);
        em.flush();
        em.clear();
        return CommonCodeEntity.toDto(existCommonCodeEntity);
    }

    /**
     * <pre>
     * 1. MethodName : deleteCommonCode
     * 2. ClassName  : AdminCommonJpaRepository.java
     * 3. Comment    : 관리자 공통 코드 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 02.
     * </pre>
     */
    public Long deleteCommonCode(Long idx) {
        em.remove(em.find(CommonCodeEntity.class, idx));
        em.flush();
        em.clear();
        return idx;
    }
}
