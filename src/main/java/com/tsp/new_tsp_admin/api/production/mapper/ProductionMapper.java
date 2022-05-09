package com.tsp.new_tsp_admin.api.production.mapper;

import com.tsp.new_tsp_admin.api.domain.production.AdminProductionDTO;
import com.tsp.new_tsp_admin.api.domain.production.AdminProductionEntity;
import com.tsp.new_tsp_admin.common.StructMapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

public interface ProductionMapper extends StructMapper<AdminProductionDTO, AdminProductionEntity> {

	ProductionMapper INSTANCE = Mappers.getMapper(ProductionMapper.class);

	@Override
	AdminProductionDTO toDto(AdminProductionEntity entity);

	@Override
	AdminProductionEntity toEntity(AdminProductionDTO dto);

	@Override
	List<AdminProductionDTO> toDtoList(List<AdminProductionEntity> entityList);

	@Override
	List<AdminProductionEntity> toEntityList(List<AdminProductionDTO> dtoList);
}
