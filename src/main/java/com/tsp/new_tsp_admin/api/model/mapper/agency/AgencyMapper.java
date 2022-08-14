package com.tsp.new_tsp_admin.api.model.mapper.agency;

import com.tsp.new_tsp_admin.api.domain.model.agency.AdminAgencyDTO;
import com.tsp.new_tsp_admin.api.domain.model.agency.AdminAgencyEntity;
import com.tsp.new_tsp_admin.common.StructMapper;

import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

public interface AgencyMapper extends StructMapper<AdminAgencyDTO, AdminAgencyEntity> {

    AgencyMapper INSTANCE = getMapper(AgencyMapper.class);

    @Override
    AdminAgencyDTO toDto(AdminAgencyEntity entity);

    @Override
    AdminAgencyEntity toEntity(AdminAgencyDTO dto);

    @Override
    List<AdminAgencyDTO> toDtoList(List<AdminAgencyEntity> entityList);

    @Override
    List<AdminAgencyEntity> toEntityList(List<AdminAgencyDTO> dtoList);
}
