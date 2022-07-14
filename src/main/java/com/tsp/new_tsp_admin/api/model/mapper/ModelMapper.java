package com.tsp.new_tsp_admin.api.model.mapper;

import com.tsp.new_tsp_admin.api.domain.model.AdminModelDTO;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import com.tsp.new_tsp_admin.common.StructMapper;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper
public interface ModelMapper extends StructMapper<AdminModelDTO, AdminModelEntity> {
    ModelMapper INSTANCE = getMapper(ModelMapper.class);

    @Override
    AdminModelDTO toDto(AdminModelEntity entity);

    @Override
    AdminModelEntity toEntity(AdminModelDTO dto);

    @Override
    List<AdminModelDTO> toDtoList(List<AdminModelEntity> entityList);

    @Override
    List<AdminModelEntity> toEntityList(List<AdminModelDTO> dtoList);
}
