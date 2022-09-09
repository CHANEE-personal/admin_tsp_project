package com.tsp.new_tsp_admin.api.model.mapper.negotiation;

import com.tsp.new_tsp_admin.api.domain.model.negotiation.AdminNegotiationDTO;
import com.tsp.new_tsp_admin.api.domain.model.negotiation.AdminNegotiationEntity;

import java.util.ArrayList;
import java.util.List;

public class NegotiationMapperImpl implements NegotiationMapper {

    @Override
    public AdminNegotiationDTO toDto(AdminNegotiationEntity entity) {
        if (entity == null) return null;

        return AdminNegotiationDTO.builder()
                .idx(entity.getIdx())
                .rnum(entity.getRnum())
                .modelIdx(entity.getModelIdx())
                .modelNegotiationDesc(entity.getModelNegotiationDesc())
                .modelNegotiationDate(entity.getModelNegotiationDate())
                .name(entity.getName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .visible(entity.getVisible())
                .creator(entity.getCreator())
                .createTime(entity.getCreateTime())
                .updater(entity.getUpdater())
                .updateTime(entity.getUpdateTime())
                .build();
    }

    @Override
    public AdminNegotiationEntity toEntity(AdminNegotiationDTO dto) {
        if (dto == null) return null;

        return AdminNegotiationEntity.builder()
                .idx(dto.getIdx())
                .rnum(dto.getRnum())
                .modelIdx(dto.getModelIdx())
                .modelNegotiationDesc(dto.getModelNegotiationDesc())
                .modelNegotiationDate(dto.getModelNegotiationDate())
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .visible(dto.getVisible())
                .creator(dto.getCreator())
                .createTime(dto.getCreateTime())
                .updater(dto.getUpdater())
                .updateTime(dto.getUpdateTime())
                .build();
    }

    @Override
    public List<AdminNegotiationDTO> toDtoList(List<AdminNegotiationEntity> entityList) {
        if (entityList == null) return null;

        List<AdminNegotiationDTO> list = new ArrayList<>(entityList.size());
        for (AdminNegotiationEntity adminNegotiationEntity : entityList) {
            list.add(toDto(adminNegotiationEntity));
        }

        return list;
    }

    @Override
    public List<AdminNegotiationEntity> toEntityList(List<AdminNegotiationDTO> dtoList) {
        if (dtoList == null) return null;

        List<AdminNegotiationEntity> list = new ArrayList<>(dtoList.size());
        for (AdminNegotiationDTO adminNegotiationDTO : dtoList) {
            list.add(toEntity(adminNegotiationDTO));
        }

        return list;
    }
}
