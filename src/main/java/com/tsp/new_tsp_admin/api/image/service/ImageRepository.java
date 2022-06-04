package com.tsp.new_tsp_admin.api.image.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import com.tsp.new_tsp_admin.api.domain.common.QCommonImageEntity;
import com.tsp.new_tsp_admin.common.StringUtil;
import com.tsp.new_tsp_admin.exception.ApiExceptionType;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

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
     *
     * @return
     */
    public String currentDate() {
        String pattern = "MMddHHmmssSSS";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.KOREA);
        Timestamp ts = new Timestamp(System.currentTimeMillis());

        return simpleDateFormat.format(ts.getTime());
    }

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
    @Modifying(clearAutomatically = true)
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
     * 1. MethodName : uploadImageFile
     * 2. ClassName  : ImageServiceImpl.java
     * 3. Comment    : 이미지 파일 업로드
     * 4. 작성자       : CHO
     * 5. 작성일       : 2022. 05. 07.
     * </pre>
     *
     * @param commonImageEntity
     * @param files
     * @param flag
     * @return
     * @throws Exception
     */
    public String uploadImageFile(CommonImageEntity commonImageEntity,
                                  List<MultipartFile> files, String flag) throws Exception {
        String ext;        // 파일 확장자
        String fileId;      // 파일명
        String fileMask;    // 파일 Mask
        long fileSize;      // 파일 사이즈
        int mainCnt = 0;

        File dir = new File(uploadPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        if (files != null) {
            if ("update".equals(flag)) {
                if ("production".equals(commonImageEntity.getTypeName())) {
                    commonImageEntity.setImageType("main");
                    commonImageEntity.setTypeIdx(commonImageEntity.getIdx());
                    deleteImage(commonImageEntity);
                }
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
                            e.getStackTrace();
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

                    Runtime.getRuntime().exec("chmod -R 755 " + filePath);

                    commonImageEntity.setFileNum(mainCnt);
                    commonImageEntity.setFileName(file.getOriginalFilename());
                    commonImageEntity.setFileSize(fileSize);
                    commonImageEntity.setFileMask(fileMask);
                    commonImageEntity.setFilePath(filePath);

                    CommonImageEntity newCommonImageEntity = CommonImageEntity.builder()
                                    .imageType(commonImageEntity.getImageType())
                                    .typeIdx(commonImageEntity.getTypeIdx())
                                    .typeName("model")
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
                    throw new Exception();
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
            throw new TspException(ApiExceptionType.ERROR_DELETE_IMAGE);
        }
    }
}
