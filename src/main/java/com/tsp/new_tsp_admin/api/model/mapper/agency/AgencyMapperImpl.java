package com.tsp.new_tsp_admin.api.model.mapper.agency;

import com.tsp.new_tsp_admin.api.domain.model.agency.AdminAgencyDTO;
import com.tsp.new_tsp_admin.api.domain.model.agency.AdminAgencyEntity;

import java.util.ArrayList;
import java.util.List;

public class AgencyMapperImpl implements AgencyMapper {

    @Override
    public AdminAgencyDTO toDto(AdminAgencyEntity entity) {
        if (entity == null) return null;

        return AdminAgencyDTO.builder()
                .idx(entity.getIdx())
                .rnum(entity.getRnum())
                .agencyName(entity.getAgencyName())
                .agencyDescription(entity.getAgencyDescription())
                .visible(entity.getVisible())
                .creator(entity.getCreator())
                .createTime(entity.getCreateTime())
                .updater(entity.getUpdater())
                .updateTime(entity.getUpdateTime())
                .build();
    }

    @Override
    public AdminAgencyEntity toEntity(AdminAgencyDTO dto) {
        if (dto == null) return null;

        return AdminAgencyEntity.builder()
                .idx(dto.getIdx())
                .rnum(dto.getRnum())
                .agencyName(dto.getAgencyName())
                .agencyDescription(dto.getAgencyDescription())
                .visible(dto.getVisible())
                .creator(dto.getCreator())
                .createTime(dto.getCreateTime())
                .updater(dto.getUpdater())
                .updateTime(dto.getUpdateTime())
                .build();
    }

    @Override
    public List<AdminAgencyDTO> toDtoList(List<AdminAgencyEntity> entityList) {
        if (entityList == null) return null;

        List<AdminAgencyDTO> list = new ArrayList<>(entityList.size());
        for (AdminAgencyEntity adminAgencyEntity : entityList) {
            list.add(toDto(adminAgencyEntity));
        }

        return list;
    }

    @Override
    public List<AdminAgencyEntity> toEntityList(List<AdminAgencyDTO> dtoList) {
        if (dtoList == null) return null;

        List<AdminAgencyEntity> list = new ArrayList<>(dtoList.size());
        for (AdminAgencyDTO adminAgencyDTO : dtoList) {
            list.add(toEntity(adminAgencyDTO));
        }

        return list;
    }
}
