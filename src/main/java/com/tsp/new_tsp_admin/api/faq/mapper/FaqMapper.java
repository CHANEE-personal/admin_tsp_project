package com.tsp.new_tsp_admin.api.faq.mapper;

import com.tsp.new_tsp_admin.api.domain.faq.AdminFaqDTO;
import com.tsp.new_tsp_admin.api.domain.faq.AdminFaqEntity;
import com.tsp.new_tsp_admin.common.StructMapper;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper
public interface FaqMapper extends StructMapper<AdminFaqDTO, AdminFaqEntity> {
    FaqMapper INSTANCE = getMapper(FaqMapper.class);

    @Override
    AdminFaqDTO toDto(AdminFaqEntity entity);

    @Override
    AdminFaqEntity toEntity(AdminFaqDTO dto);

    @Override
    List<AdminFaqDTO> toDtoList(List<AdminFaqEntity> entityList);

    @Override
    List<AdminFaqEntity> toEntityList(List<AdminFaqDTO> dtoList);
}
