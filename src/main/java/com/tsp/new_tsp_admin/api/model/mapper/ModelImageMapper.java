package com.tsp.new_tsp_admin.api.model.mapper;

import com.tsp.new_tsp_admin.api.domain.common.CommonImageDTO;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import com.tsp.new_tsp_admin.common.StructMapper;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper
public interface ModelImageMapper extends StructMapper<CommonImageDTO, CommonImageEntity> {
	ModelImageMapper INSTANCE = getMapper(ModelImageMapper.class);

	@Override
	CommonImageDTO toDto(CommonImageEntity entity);

	@Override
	CommonImageEntity toEntity(CommonImageDTO dto);

	@Override
	List<CommonImageDTO> toDtoList(List<CommonImageEntity> entityList);

	@Override
	List<CommonImageEntity> toEntityList(List<CommonImageDTO> dtoList);
}
