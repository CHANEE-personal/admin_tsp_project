package com.tsp.new_tsp_admin.api.model.mapper.schedule;

import com.tsp.new_tsp_admin.api.domain.model.schedule.AdminScheduleDTO;
import com.tsp.new_tsp_admin.api.domain.model.schedule.AdminScheduleEntity;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper
public class ScheduleMapperImpl implements ScheduleMapper {
    @Override
    public AdminScheduleDTO toDto(AdminScheduleEntity entity) {
        if (entity == null) return null;

        return AdminScheduleDTO.builder().idx(entity.getIdx())
                .rnum(entity.getRnum())
                .modelIdx(entity.getModelIdx())
                .modelSchedule(entity.getModelSchedule())
                .modelScheduleTime(entity.getModelScheduleTime())
                .visible(entity.getVisible())
                .creator(entity.getCreator())
                .createTime(entity.getCreateTime())
                .updater(entity.getUpdater())
                .updateTime(entity.getUpdateTime())
                .build();
    }

    @Override
    public AdminScheduleEntity toEntity(AdminScheduleDTO dto) {
        if (dto == null) return null;

        return AdminScheduleEntity.builder()
                .rnum(dto.getRnum())
                .idx(dto.getIdx())
                .modelIdx(dto.getModelIdx())
                .modelSchedule(dto.getModelSchedule())
                .modelScheduleTime(dto.getModelScheduleTime())
                .visible(dto.getVisible())
                .creator(dto.getCreator())
                .createTime(dto.getCreateTime())
                .updater(dto.getUpdater())
                .updateTime(dto.getUpdateTime())
                .build();
    }

    @Override
    public List<AdminScheduleDTO> toDtoList(List<AdminScheduleEntity> entityList) {
        if (entityList == null) return null;

        List<AdminScheduleDTO> list = new ArrayList<>(entityList.size());
        for (AdminScheduleEntity AdminScheduleEntity : entityList) {
            list.add(toDto(AdminScheduleEntity));
        }

        return list;
    }

    @Override
    public List<AdminScheduleEntity> toEntityList(List<AdminScheduleDTO> dtoList) {
        if (dtoList == null) return null;

        List<AdminScheduleEntity> list = new ArrayList<>(dtoList.size());
        for (AdminScheduleDTO adminScheduleDTO : dtoList) {
            list.add(toEntity(adminScheduleDTO));
        }

        return list;
    }
}
