package com.tsp.new_tsp_admin.api.image.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

import static com.tsp.new_tsp_admin.api.domain.common.QCommonImageEntity.commonImageEntity;
import static com.tsp.new_tsp_admin.common.StringUtil.getInt;
import static com.tsp.new_tsp_admin.exception.ApiExceptionType.ERROR_DELETE_IMAGE;
import static com.tsp.new_tsp_admin.exception.ApiExceptionType.ERROR_IMAGE;
import static java.lang.Runtime.getRuntime;
import static java.lang.System.currentTimeMillis;
import static java.util.Locale.*;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ImageRepository {
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    /**
     * <pre>
     * 1. MethodName : findOneImage
     * 2. ClassName  : ImageRepository.java
     * 3. Comment    : 이미지 파일 상세 조회
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 07.
     * </pre>
     */
//    @Transactional(readOnly = true)
    public CommonImageEntity findOneImage(CommonImageEntity exCommonImageEntity) {
        return queryFactory
                .selectFrom(commonImageEntity)
                .where(commonImageEntity.idx.eq(exCommonImageEntity.getIdx())
                        .and(commonImageEntity.visible.eq("Y"))
                        .and(commonImageEntity.typeName.eq(exCommonImageEntity.getTypeName())))
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
     */
    public Integer maxSubCnt(CommonImageEntity exCommonImageEntity) {
        return queryFactory.selectFrom(commonImageEntity)
                .select(commonImageEntity.fileNum.max())
                .where(commonImageEntity.typeName.eq(exCommonImageEntity.getTypeName()))
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
     */
//    @Transactional
    @Modifying(clearAutomatically = true)
    public Long insertImage(CommonImageEntity commonImageEntity) {
        try {
            em.persist(commonImageEntity);
            return commonImageEntity.getIdx();
        } catch (Exception e) {
            throw new TspException(ERROR_IMAGE, e);
        }
    }

    /**
     * <pre>
     * 1. MethodName : deleteModelImage
     * 2. ClassName  : ImageRepository.java
     * 3. Comment    : 관리자 모델 이미지 삭제
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 07.
     * </pre>
     */
//    @Transactional
    @Modifying(clearAutomatically = true)
    public Long deleteModelImage(Long idx) {
        try {
            em.remove(em.find(CommonImageEntity.class, idx));
            em.flush();
            em.clear();

            return idx;
        } catch (Exception e) {
            throw new TspException(ERROR_IMAGE, e);
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
     */
    public CommonImageEntity deleteImage(CommonImageEntity exCommonImageEntity) {
        try {
            em.remove(em.find(CommonImageEntity.class, exCommonImageEntity.getIdx()));
            em.flush();
            em.clear();

            return exCommonImageEntity;
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_IMAGE, e);
        }
    }
}
