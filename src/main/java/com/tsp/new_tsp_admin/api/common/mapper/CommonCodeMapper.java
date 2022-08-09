package com.tsp.new_tsp_admin.api.common.mapper;

import com.tsp.new_tsp_admin.api.domain.common.CommonCodeDTO;
import com.tsp.new_tsp_admin.api.domain.common.CommonCodeEntity;
import com.tsp.new_tsp_admin.common.StructMapper;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper
public interface CommonCodeMapper extends StructMapper<CommonCodeDTO, CommonCodeEntity> {
    CommonCodeMapper INSTANCE = getMapper(CommonCodeMapper.class);

    @Override
    CommonCodeDTO toDto(CommonCodeEntity entity);

    @Override
    CommonCodeEntity toEntity(CommonCodeDTO dto);

    @Override
    List<CommonCodeDTO> toDtoList(List<CommonCodeEntity> entityList);

    @Override
    List<CommonCodeEntity> toEntityList(List<CommonCodeDTO> dtoList);
}
