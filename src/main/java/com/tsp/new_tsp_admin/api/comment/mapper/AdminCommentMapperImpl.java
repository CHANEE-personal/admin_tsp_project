package com.tsp.new_tsp_admin.api.comment.mapper;

import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentDTO;
import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentEntity;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper
public class AdminCommentMapperImpl implements AdminCommentMapper {

    @Override
    public AdminCommentDTO toDto(AdminCommentEntity entity) {
        if (entity == null) return null;

        return AdminCommentDTO.builder().idx(entity.getIdx())
                .rnum(entity.getRnum())
                .comment(entity.getComment())
                .commentTypeIdx(entity.getCommentTypeIdx())
                .commentType(entity.getCommentType())
                .visible(entity.getVisible())
                .creator(entity.getCreator())
                .createTime(entity.getCreateTime())
                .updater(entity.getUpdater())
                .updateTime(entity.getUpdateTime())
                .build();
    }

    @Override
    public AdminCommentEntity toEntity(AdminCommentDTO dto) {
        if (dto == null) return null;

        return AdminCommentEntity.builder()
                .rnum(dto.getRnum())
                .idx(dto.getIdx())
                .comment(dto.getComment())
                .commentTypeIdx(dto.getCommentTypeIdx())
                .commentType(dto.getCommentType())
                .visible(dto.getVisible())
                .creator(dto.getCreator())
                .createTime(dto.getCreateTime())
                .updater(dto.getUpdater())
                .updateTime(dto.getUpdateTime())
                .build();
    }

    @Override
    public List<AdminCommentDTO> toDtoList(List<AdminCommentEntity> entityList) {
        if (entityList == null) return null;

        List<AdminCommentDTO> list = new ArrayList<>(entityList.size());
        for (AdminCommentEntity adminCommentEntity : entityList) {
            list.add(toDto(adminCommentEntity));
        }

        return list;
    }

    @Override
    public List<AdminCommentEntity> toEntityList(List<AdminCommentDTO> dtoList) {
        if (dtoList == null) return null;

        List<AdminCommentEntity> list = new ArrayList<>(dtoList.size());
        for (AdminCommentDTO adminCommentDTO : dtoList) {
            list.add(toEntity(adminCommentDTO));
        }

        return list;
    }
}
