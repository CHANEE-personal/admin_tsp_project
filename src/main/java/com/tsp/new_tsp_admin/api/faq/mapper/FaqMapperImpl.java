package com.tsp.new_tsp_admin.api.faq.mapper;

import com.tsp.new_tsp_admin.api.domain.faq.AdminFaqDTO;
import com.tsp.new_tsp_admin.api.domain.faq.AdminFaqEntity;

import java.util.ArrayList;
import java.util.List;

public class FaqMapperImpl implements FaqMapper {

    @Override
    public AdminFaqDTO toDto(AdminFaqEntity entity) {
        if (entity == null) return null;

        return AdminFaqDTO.builder().idx(entity.getIdx())
                .rnum(entity.getRnum())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .viewCount(entity.getViewCount())
                .visible(entity.getVisible())
                .creator(entity.getCreator())
                .createTime(entity.getCreateTime())
                .updater(entity.getUpdater())
                .updateTime(entity.getUpdateTime())
                .build();
    }

    @Override
    public AdminFaqEntity toEntity(AdminFaqDTO dto) {
        if (dto == null) return null;

        return AdminFaqEntity.builder()
                .rnum(dto.getRnum())
                .idx(dto.getIdx())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .viewCount(dto.getViewCount())
                .visible(dto.getVisible())
                .creator(dto.getCreator())
                .createTime(dto.getCreateTime())
                .updater(dto.getUpdater())
                .updateTime(dto.getUpdateTime())
                .build();
    }

    @Override
    public List<AdminFaqDTO> toDtoList(List<AdminFaqEntity> entityList) {
        if (entityList == null) return null;

        List<AdminFaqDTO> list = new ArrayList<>(entityList.size());
        for (AdminFaqEntity AdminFaqEntity : entityList) {
            list.add(toDto(AdminFaqEntity));
        }

        return list;
    }

    @Override
    public List<AdminFaqEntity> toEntityList(List<AdminFaqDTO> dtoList) {
        if (dtoList == null) return null;

        List<AdminFaqEntity> list = new ArrayList<>(dtoList.size());
        for (AdminFaqDTO adminFaqDTO : dtoList) {
            list.add(toEntity(adminFaqDTO));
        }

        return list;
    }
}
