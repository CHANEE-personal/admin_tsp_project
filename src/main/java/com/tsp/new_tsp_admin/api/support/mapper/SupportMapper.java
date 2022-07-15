package com.tsp.new_tsp_admin.api.support.mapper;

import com.tsp.new_tsp_admin.api.domain.support.AdminSupportDTO;
import com.tsp.new_tsp_admin.api.domain.support.AdminSupportEntity;
import com.tsp.new_tsp_admin.common.StructMapper;

import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

public interface SupportMapper extends StructMapper<AdminSupportDTO, AdminSupportEntity> {
    SupportMapper INSTANCE = getMapper(SupportMapper.class);

    @Override
    AdminSupportDTO toDto(AdminSupportEntity entity);

    @Override
    AdminSupportEntity toEntity(AdminSupportDTO dto);

    @Override
    List<AdminSupportDTO> toDtoList(List<AdminSupportEntity> entityList);

    @Override
    List<AdminSupportEntity> toEntityList(List<AdminSupportDTO> dtoList);
}
