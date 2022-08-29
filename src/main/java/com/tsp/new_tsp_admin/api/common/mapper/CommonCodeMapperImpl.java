package com.tsp.new_tsp_admin.api.common.mapper;

import com.tsp.new_tsp_admin.api.domain.common.CommonCodeDTO;
import com.tsp.new_tsp_admin.api.domain.common.CommonCodeEntity;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper
public class CommonCodeMapperImpl implements CommonCodeMapper {
    @Override
    public CommonCodeDTO toDto(CommonCodeEntity entity) {
        return CommonCodeDTO.builder().idx(entity.getIdx())
                .rnum(entity.getRnum())
                .categoryCd(entity.getCategoryCd())
                .categoryNm(entity.getCategoryNm())
                .cmmType(entity.getCmmType())
                .visible(entity.getVisible())
                .build();
    }

    @Override
    public CommonCodeEntity toEntity(CommonCodeDTO dto) {
        if (dto == null) return null;

        return CommonCodeEntity.builder()
                .rnum(dto.getRnum())
                .idx(dto.getIdx())
                .categoryCd(dto.getCategoryCd())
                .categoryNm(dto.getCategoryNm())
                .cmmType(dto.getCmmType())
                .visible(dto.getVisible())
                .build();
    }

    @Override
    public List<CommonCodeDTO> toDtoList(List<CommonCodeEntity> entityList) {
        if (entityList == null) return null;

        List<CommonCodeDTO> list = new ArrayList<>(entityList.size());
        for (CommonCodeEntity commonCodeEntity : entityList) {
            list.add(toDto(commonCodeEntity));
        }

        return list;
    }

    @Override
    public List<CommonCodeEntity> toEntityList(List<CommonCodeDTO> dtoList) {
        if (dtoList == null) return null;

        List<CommonCodeEntity> list = new ArrayList<>(dtoList.size());
        for (CommonCodeDTO commonCodeDTO : dtoList) {
            list.add(toEntity(commonCodeDTO));
        }

        return list;
    }
}
