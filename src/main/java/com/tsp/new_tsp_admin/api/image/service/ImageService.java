package com.tsp.new_tsp_admin.api.image.service;

import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface ImageService {

    String uploadImageFile(CommonImageEntity commonImageEntity,
                           MultipartFile[] files, String flag) throws Exception;
}
