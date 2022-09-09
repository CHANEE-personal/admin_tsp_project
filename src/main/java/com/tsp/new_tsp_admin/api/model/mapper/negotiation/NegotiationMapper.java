package com.tsp.new_tsp_admin.api.model.mapper.negotiation;

import com.tsp.new_tsp_admin.api.domain.model.negotiation.AdminNegotiationDTO;
import com.tsp.new_tsp_admin.api.domain.model.negotiation.AdminNegotiationEntity;
import com.tsp.new_tsp_admin.common.StructMapper;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper
public interface NegotiationMapper extends StructMapper<AdminNegotiationDTO, AdminNegotiationEntity> {
    NegotiationMapper INSTANCE = getMapper(NegotiationMapper.class);

    @Override
    AdminNegotiationDTO toDto(AdminNegotiationEntity entity);

    @Override
    AdminNegotiationEntity toEntity(AdminNegotiationDTO dto);

    @Override
    List<AdminNegotiationDTO> toDtoList(List<AdminNegotiationEntity> entityList);

    @Override
    List<AdminNegotiationEntity> toEntityList(List<AdminNegotiationDTO> dtoList);
}
