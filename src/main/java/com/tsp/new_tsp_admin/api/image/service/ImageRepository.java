package com.tsp.new_tsp_admin.api.image.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import com.tsp.new_tsp_admin.common.StringUtil;
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

    @Value("${image.uploadPath}")
    private String uploadPath;

    /**
     * <pre>
     * 1. MethodName : currentDate
     * 2. ClassName  : ImageServiceImpl.java
     * 3. Comment    : 현재 날짜 구하기
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 07.
     * </pre>
     */
    public String currentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMddHHmmssSSS", KOREA);
        return simpleDateFormat.format(new Timestamp(currentTimeMillis()).getTime());
    }

    /**
     * <pre>
     * 1. MethodName : maxSubEntity
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
    public Integer insertImage(CommonImageEntity commonImageEntity) {
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
    public Integer deleteModelImage(Integer idx) {
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
     * 1. MethodName : uploadImageFile
     * 2. ClassName  : ImageServiceImpl.java
     * 3. Comment    : 이미지 파일 업로드
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 07.
     * </pre>
     */
    public String uploadImageFile(CommonImageEntity commonImageEntity,
                                  List<MultipartFile> files, String flag) {
        String ext;        // 파일 확장자
        String fileId;      // 파일명
        String fileMask;    // 파일 Mask
        long fileSize;      // 파일 사이즈
        int mainCnt = 0;

        File dir = new File(uploadPath);
        if (!dir.exists()) dir.mkdirs();

        if (files != null) {
            if ("update".equals(flag) && "production".equals(commonImageEntity.getTypeName())) {
                commonImageEntity.setImageType("main");
                commonImageEntity.setTypeIdx(commonImageEntity.getIdx());
                deleteImage(commonImageEntity);
            }

            for (MultipartFile file : files) {
                try {
                    ext = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
                    fileId = currentDate();
                    fileMask = fileId + '.' + ext;
                    fileSize = file.getSize();

                    if (!new File(uploadPath).exists()) {
                        try {
                            new File(uploadPath).mkdir();
                        } catch (Exception e) {
                            throw new TspException(ERROR_IMAGE, e);
                        }
                    }

                    if ("insert".equals(flag)) {
                        if (mainCnt == 0) {
                            commonImageEntity.setImageType("main");
                        } else {
                            commonImageEntity.setImageType("sub" + mainCnt);
                        }
                    } else {
                        int cnt = StringUtil.getInt(maxSubCnt(commonImageEntity), 0);
                        if ("production".equals(commonImageEntity.getTypeName())) {
                            commonImageEntity.setImageType("main");
                        } else {
                            if (cnt == 1) {
                                commonImageEntity.setImageType("main");
                            } else {
                                commonImageEntity.setImageType("sub" + cnt);
                                commonImageEntity.setFileNum(cnt);
                            }
                        }
                    }

                    String filePath = uploadPath + fileMask;
                    file.transferTo(new File(filePath));

                    getRuntime().exec("chmod -R 755 " + filePath);

                    commonImageEntity.setFileNum(mainCnt);
                    commonImageEntity.setFileName(file.getOriginalFilename());
                    commonImageEntity.setFileSize(fileSize);
                    commonImageEntity.setFileMask(fileMask);
                    commonImageEntity.setFilePath(filePath);

                    CommonImageEntity newCommonImageEntity = CommonImageEntity.builder()
                            .imageType(commonImageEntity.getImageType())
                            .typeIdx(commonImageEntity.getTypeIdx())
                            .typeName(commonImageEntity.getTypeName())
                            .fileNum(commonImageEntity.getFileNum())
                            .fileName(commonImageEntity.getFileName())
                            .fileSize(commonImageEntity.getFileSize())
                            .fileMask(commonImageEntity.getFileMask())
                            .filePath(commonImageEntity.getFilePath())
                            .visible(commonImageEntity.getVisible())
                            .build();

                    em.persist(newCommonImageEntity);
                    if (newCommonImageEntity.getIdx() > 0) {
                        mainCnt++;
                    }
                } catch (Exception e) {
                    throw new TspException(ERROR_IMAGE, e);
                }
            }
        }
        return "Y";
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
