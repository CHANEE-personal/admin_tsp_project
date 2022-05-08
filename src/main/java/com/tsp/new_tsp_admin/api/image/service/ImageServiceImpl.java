package com.tsp.new_tsp_admin.api.image.service;

import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import com.tsp.new_tsp_admin.common.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;

@Service("ImageService")
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

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
    @Override
    public String uploadImageFile(CommonImageEntity commonImageEntity,
                                  MultipartFile[] files, String flag) throws Exception {
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
                    CommonImageEntity.builder().imageType("main").typeIdx(commonImageEntity.getIdx());
                    imageRepository.deleteImage(commonImageEntity);
                }
            }

            for (MultipartFile file: files) {
                try {
                    ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
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
                            CommonImageEntity.builder().imageType("main").build();
                        } else {
                            CommonImageEntity.builder().imageType("sub" + mainCnt);
                        }
                    } else {
                        if ("production".equals(commonImageEntity.getTypeName())) {
                            CommonImageEntity.builder().imageType("main");
                        } else {
                            if (imageRepository.maxSubCnt(commonImageEntity) == 1) {
                                CommonImageEntity.builder().imageType("main");
                            } else {
                                CommonImageEntity.builder().imageType("sub" + StringUtil.getInt(imageRepository.maxSubCnt(commonImageEntity), 0));
                                CommonImageEntity.builder().fileNum(StringUtil.getInt(imageRepository.maxSubCnt(commonImageEntity), 0));
                            }
                        }
                    }

                    String filePath = uploadPath + fileMask;
                    file.transferTo(new File(filePath));

                    Runtime.getRuntime().exec("chmod -R 755 " + filePath);

                    CommonImageEntity.builder().fileNum(StringUtil.getInt(imageRepository.maxSubCnt(commonImageEntity), 0))
                            .fileName(file.getOriginalFilename())
                            .fileSize(fileSize)
                            .fileMask(fileMask)
                            .filePath(uploadPath + fileMask)
                            .visible("Y").build();

                    if (imageRepository.insertImage(commonImageEntity) > 0) {
                        mainCnt++;
                    }
                } catch (Exception e) {
                    throw new Exception();
                }
            }
        }

        return "Y";
    }
}
