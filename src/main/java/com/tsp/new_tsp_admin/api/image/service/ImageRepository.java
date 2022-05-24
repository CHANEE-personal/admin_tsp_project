package com.tsp.new_tsp_admin.api.image.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import com.tsp.new_tsp_admin.api.domain.common.QCommonImageEntity;
import com.tsp.new_tsp_admin.exception.ApiExceptionType;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static com.tsp.new_tsp_admin.api.domain.common.QCommonImageEntity.commonImageEntity;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ImageRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    /**
     * <pre>
     * 1. MethodName : maxSubEntity
     * 2. ClassName  : ImageRepository.java
     * 3. Comment    : 이미지 파일 최대 entity 가져오기
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 07.
     * </pre>
     *
     * @param commonImageEntity
     * @return
     * @throws Exception
     */
    @Transactional
    public CommonImageEntity findOneImage(CommonImageEntity commonImageEntity) {
        return queryFactory
                .selectFrom(QCommonImageEntity.commonImageEntity)
                .where(QCommonImageEntity.commonImageEntity.idx.eq(commonImageEntity.getIdx())
                        .and(QCommonImageEntity.commonImageEntity.visible.eq("Y"))
                        .and(QCommonImageEntity.commonImageEntity.typeName.eq(commonImageEntity.getTypeName())))
                .fetchOne();
    }

    /**
     * <pre>
     * 1. MethodName : maxSubCnt
     * 2. ClassName  : ImageRepository.java
     * 3. Comment    : 이미지 파일 최대 값 가져오기
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 07.
     * </pre>
     *
     * @param commonImageEntity
     * @return
     * @throws Exception
     */
    @Transactional
    public Integer maxSubCnt(CommonImageEntity commonImageEntity) {
        return queryFactory.selectFrom(QCommonImageEntity.commonImageEntity)
                .select(QCommonImageEntity.commonImageEntity.fileNum.max())
                .where(QCommonImageEntity.commonImageEntity.typeName.eq(commonImageEntity.getTypeName()))
                .fetchFirst();
    }

    /**
     * <pre>
     * 1. MethodName : insertImage
     * 2. ClassName  : ImageRepository.java
     * 3. Comment    : 관리자 이미지 등록
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 07.
     * </pre>
     *
     * @param commonImageEntity
     */
    @Transactional
    public Integer insertImage(CommonImageEntity commonImageEntity) {
        try {
            em.persist(commonImageEntity);

            return commonImageEntity.getIdx();
        } catch (Exception e) {
            throw new TspException(ApiExceptionType.NOT_EXIST_IMAGE);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteImage
     * 2. ClassName  : ImageRepository.java
     * 3. Comment    : 관리자 이미지 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 07.
     * </pre>
     *
     * @param commonImageEntity
     */
    @Transactional
    public CommonImageEntity deleteImage(CommonImageEntity commonImageEntity) {
        try {
            commonImageEntity = em.find(CommonImageEntity.class, commonImageEntity.getIdx());
            em.remove(commonImageEntity);
            em.flush();
            em.clear();

            return commonImageEntity;
        } catch (Exception e) {
            e.printStackTrace();
            throw new TspException(ApiExceptionType.ERROR_DELETE_IMAGE);
        }
    }
}
