package com.tsp.new_tsp_admin.api.model.mapper.schedule;

import com.tsp.new_tsp_admin.api.domain.model.schedule.AdminScheduleDTO;
import com.tsp.new_tsp_admin.api.domain.model.schedule.AdminScheduleEntity;
import com.tsp.new_tsp_admin.common.StructMapper;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper
public interface ScheduleMapper extends StructMapper<AdminScheduleDTO, AdminScheduleEntity> {
    ScheduleMapper INSTANCE = getMapper(ScheduleMapper.class);

    @Override
    AdminScheduleDTO toDto(AdminScheduleEntity entity);

    @Override
    AdminScheduleEntity toEntity(AdminScheduleDTO dto);

    @Override
    List<AdminScheduleDTO> toDtoList(List<AdminScheduleEntity> entityList);

    @Override
    List<AdminScheduleEntity> toEntityList(List<AdminScheduleDTO> dtoList);
}
