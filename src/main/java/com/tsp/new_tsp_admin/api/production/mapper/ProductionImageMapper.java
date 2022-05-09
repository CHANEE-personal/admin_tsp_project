package com.tsp.new_tsp_admin.api.production.mapper;

import com.tsp.new_tsp_admin.api.domain.common.CommonImageDTO;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import com.tsp.new_tsp_admin.common.StructMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductionImageMapper extends StructMapper<CommonImageDTO, CommonImageEntity> {

    ProductionImageMapper INSTANCE = Mappers.getMapper(ProductionImageMapper.class);

    @Override
    CommonImageDTO toDto(CommonImageEntity entity);

    @Override
    CommonImageEntity toEntity(CommonImageDTO dto);

    @Override
    List<CommonImageDTO> toDtoList(List<CommonImageEntity> entityList);

    @Override
    List<CommonImageEntity> toEntityList(List<CommonImageDTO> dtoList);
}
