package com.tsp.new_tsp_admin.api.notice.mapper;

import com.tsp.new_tsp_admin.api.domain.notice.AdminNoticeDTO;
import com.tsp.new_tsp_admin.api.domain.notice.AdminNoticeEntity;

import java.util.ArrayList;
import java.util.List;

public class NoticeMapperImpl implements NoticeMapper {

    @Override
    public AdminNoticeDTO toDto(AdminNoticeEntity entity) {
        if (entity == null) return null;

        return AdminNoticeDTO.builder().idx(entity.getIdx())
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
    public AdminNoticeEntity toEntity(AdminNoticeDTO dto) {
        if (dto == null) return null;

        return AdminNoticeEntity.builder()
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
    public List<AdminNoticeDTO> toDtoList(List<AdminNoticeEntity> entityList) {
        if (entityList == null) return null;

        List<AdminNoticeDTO> list = new ArrayList<>(entityList.size());
        for (AdminNoticeEntity adminNoticeEntity : entityList) {
            list.add(toDto(adminNoticeEntity));
        }

        return list;
    }

    @Override
    public List<AdminNoticeEntity> toEntityList(List<AdminNoticeDTO> dtoList) {
        if (dtoList == null) return null;

        List<AdminNoticeEntity> list = new ArrayList<>(dtoList.size());
        for (AdminNoticeDTO adminNoticeDTO : dtoList) {
            list.add(toEntity(adminNoticeDTO));
        }

        return list;
    }
}
