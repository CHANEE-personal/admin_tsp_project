package com.tsp.new_tsp_admin.api.notice.mapper;

import com.tsp.new_tsp_admin.api.domain.notice.AdminNoticeDTO;
import com.tsp.new_tsp_admin.api.domain.notice.AdminNoticeEntity;
import com.tsp.new_tsp_admin.common.StructMapper;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper
public interface NoticeMapper extends StructMapper<AdminNoticeDTO, AdminNoticeEntity> {
    NoticeMapper INSTANCE = getMapper(NoticeMapper.class);

    @Override
    AdminNoticeDTO toDto(AdminNoticeEntity entity);

    @Override
    AdminNoticeEntity toEntity(AdminNoticeDTO dto);

    @Override
    List<AdminNoticeDTO> toDtoList(List<AdminNoticeEntity> entityList);

    @Override
    List<AdminNoticeEntity> toEntityList(List<AdminNoticeDTO> dtoList);
}
