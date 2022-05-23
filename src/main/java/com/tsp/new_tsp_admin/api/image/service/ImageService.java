package com.tsp.new_tsp_admin.api.image.service;

import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface ImageService {

    /**
     * <pre>
     * 1. MethodName : uploadImageFile
     * 2. ClassName  : ImageService.java
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
    String uploadImageFile(CommonImageEntity commonImageEntity, List<MultipartFile> files, String flag) throws Exception;
}
