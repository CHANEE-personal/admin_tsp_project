package com.tsp.new_tsp_admin.api.production.mapper;


import com.tsp.new_tsp_admin.api.domain.common.CommonImageDTO;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;

import java.util.ArrayList;
import java.util.List;

import static com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity.*;

public class ProductionImageMapperImpl implements ProductionImageMapper {
    @Override
    public CommonImageDTO toDto(CommonImageEntity entity) {
        if (entity == null) return null;

        return CommonImageDTO.builder()
                .idx(entity.getIdx())
                .typeIdx(entity.getTypeIdx())
                .typeName(entity.getTypeName())
                .fileMask(entity.getFileMask())
                .fileSize(entity.getFileSize())
                .fileName(entity.getFileName())
                .fileNum(entity.getFileNum())
                .filePath(entity.getFilePath())
                .imageType(entity.getImageType())
                .visible(entity.getVisible())
                .regDate(entity.getRegDate())
                .build();
    }

    @Override
    public CommonImageEntity toEntity(CommonImageDTO dto) {
        if (dto == null) return null;

        return builder()
                .idx(dto.getIdx())
                .typeIdx(dto.getTypeIdx())
                .typeName(dto.getTypeName())
                .fileMask(dto.getFileMask())
                .fileName(dto.getFileName())
                .fileNum(dto.getFileNum())
                .filePath(dto.getFilePath())
                .fileSize(dto.getFileSize())
                .imageType(dto.getImageType())
                .regDate(dto.getRegDate())
                .build();
    }

    @Override
    public List<CommonImageDTO> toDtoList(List<CommonImageEntity> entityList) {
        if (entityList == null) return null;

        List<CommonImageDTO> list = new ArrayList<>(entityList.size());
        for (CommonImageEntity commonImageEntity : entityList) {
            list.add(toDto(commonImageEntity));
        }

        return list;
    }

    @Override
    public List<CommonImageEntity> toEntityList(List<CommonImageDTO> dtoList) {
        if (dtoList == null) return null;

        List<CommonImageEntity> list = new ArrayList<>(dtoList.size());
        for (CommonImageDTO commonImageDTO : dtoList) {
            list.add(toEntity(commonImageDTO));
        }

        return list;
    }
}
