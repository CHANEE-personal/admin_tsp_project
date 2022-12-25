package com.tsp.new_tsp_admin.api.image.service;

import com.tsp.new_tsp_admin.api.domain.common.CommonImageDTO;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import com.tsp.new_tsp_admin.exception.TspException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.tsp.new_tsp_admin.exception.ApiExceptionType.*;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    @Transactional(readOnly = true)
    public CommonImageDTO findOneImage(CommonImageEntity commonImageEntity) {
        try {
            return imageRepository.findOneImage(commonImageEntity);
        } catch (Exception e) {
            throw new TspException(NOT_FOUND_IMAGE, e);
        }
    }

    @Modifying(clearAutomatically = true)
    @Transactional
    public Long insertImage(CommonImageEntity commonImageEntity) {
        try {
            return imageRepository.insertImage(commonImageEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_IMAGE, e);
        }
    }

    @Modifying(clearAutomatically = true)
    @Transactional
    public Long deleteImage(CommonImageEntity commonImageEntity) {
        try {
            return imageRepository.deleteImage(commonImageEntity);
        } catch (Exception e) {
            throw new TspException(ERROR_DELETE_IMAGE, e);
        }
    }
}
