package com.tsp.new_tsp_admin.api.support.mapper;

import com.tsp.new_tsp_admin.api.domain.support.AdminSupportDTO;
import com.tsp.new_tsp_admin.api.domain.support.AdminSupportEntity;
import com.tsp.new_tsp_admin.api.support.mapper.evaluate.EvaluateMapper;

import java.util.ArrayList;
import java.util.List;

public class SupportMapperImpl implements SupportMapper {
    @Override
    public AdminSupportDTO toDto(AdminSupportEntity entity) {
        if (entity == null) return null;

        return AdminSupportDTO.builder()
                .rnum(entity.getRnum())
                .idx(entity.getIdx())
                .supportName(entity.getSupportName())
                .supportHeight(entity.getSupportHeight())
                .supportSize3(entity.getSupportSize3())
                .supportInstagram(entity.getSupportInstagram())
                .supportPhone(entity.getSupportPhone())
                .supportMessage(entity.getSupportMessage())
                .supportTime(entity.getSupportTime())
                .passYn(entity.getPassYn())
                .passTime(entity.getPassTime())
                .evaluationList(EvaluateMapper.INSTANCE.toDtoList(entity.getEvaluationEntityList()))
                .visible(entity.getVisible())
                .build();
    }

    @Override
    public AdminSupportEntity toEntity(AdminSupportDTO dto) {
        if (dto == null) return null;

        return AdminSupportEntity.builder()
                .rnum(dto.getRnum())
                .idx(dto.getIdx())
                .supportName(dto.getSupportName())
                .supportHeight(dto.getSupportHeight())
                .supportSize3(dto.getSupportSize3())
                .supportInstagram(dto.getSupportInstagram())
                .supportPhone(dto.getSupportPhone())
                .supportMessage(dto.getSupportMessage())
                .supportTime(dto.getSupportTime())
                .passYn(dto.getPassYn())
                .passTime(dto.getPassTime())
                .evaluationEntityList(EvaluateMapper.INSTANCE.toEntityList(dto.getEvaluationList()))
                .visible(dto.getVisible())
                .build();
    }

    @Override
    public List<AdminSupportDTO> toDtoList(List<AdminSupportEntity> entityList) {
        if (entityList == null) return null;

        List<AdminSupportDTO> list = new ArrayList<>(entityList.size());
        for (AdminSupportEntity adminSupportEntity : entityList) {
            list.add(toDto(adminSupportEntity));
        }

        return list;
    }

    @Override
    public List<AdminSupportEntity> toEntityList(List<AdminSupportDTO> dtoList) {
        if (dtoList == null) return null;

        List<AdminSupportEntity> list = new ArrayList<>(dtoList.size());
        for (AdminSupportDTO adminSupportDTO : dtoList) {
            list.add(toEntity(adminSupportDTO));
        }

        return list;
    }
}
