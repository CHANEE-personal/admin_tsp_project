package com.tsp.new_tsp_admin.api.comment.mapper;

import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentDTO;
import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentEntity;
import com.tsp.new_tsp_admin.common.StructMapper;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper
public interface AdminCommentMapper extends StructMapper<AdminCommentDTO, AdminCommentEntity> {
    AdminCommentMapper INSTANCE = getMapper(AdminCommentMapper.class);

    @Override
    AdminCommentDTO toDto(AdminCommentEntity entity);

    @Override
    AdminCommentEntity toEntity(AdminCommentDTO dto);

    @Override
    List<AdminCommentDTO> toDtoList(List<AdminCommentEntity> entityList);

    @Override
    List<AdminCommentEntity> toEntityList(List<AdminCommentDTO> dtoList);
}
