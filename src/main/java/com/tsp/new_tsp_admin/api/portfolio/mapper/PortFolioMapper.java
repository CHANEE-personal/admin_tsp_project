package com.tsp.new_tsp_admin.api.portfolio.mapper;

import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioDTO;
import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioEntity;
import com.tsp.new_tsp_admin.common.StructMapper;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper
public interface PortFolioMapper extends StructMapper<AdminPortFolioDTO, AdminPortFolioEntity> {
    PortFolioMapper INSTANCE = getMapper(PortFolioMapper.class);

    @Override
    AdminPortFolioDTO toDto(AdminPortFolioEntity entity);

    @Override
    AdminPortFolioEntity toEntity(AdminPortFolioDTO dto);

    @Override
    List<AdminPortFolioDTO> toDtoList(List<AdminPortFolioEntity> entityList);

    @Override
    List<AdminPortFolioEntity> toEntityList(List<AdminPortFolioDTO> dtoList);
}
