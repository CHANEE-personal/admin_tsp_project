package com.tsp.new_tsp_admin.api.model.mapper;

import com.tsp.new_tsp_admin.api.domain.model.AdminModelDTO;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper
public class ModelMapperImpl implements ModelMapper {

    @Override
    public AdminModelDTO toDto(AdminModelEntity entity) {
        return AdminModelDTO.builder().idx(entity.getIdx())
                .rnum(entity.getRnum())
                .categoryCd(entity.getCategoryCd())
                .modelKorName(entity.getModelKorName())
                .modelEngName(entity.getModelEngName())
                .modelDescription(entity.getModelDescription())
                .visible(entity.getVisible())
                .height(entity.getHeight())
                .shoes(entity.getShoes())
                .size3(entity.getSize3())
                .categoryAge(entity.getCategoryAge())
                .modelMainYn(entity.getModelMainYn())
                .modelFirstName(entity.getModelFirstName())
                .modelSecondName(entity.getModelSecondName())
                .modelKorFirstName(entity.getModelKorFirstName())
                .modelKorSecondName(entity.getModelKorSecondName())
                .careerList(entity.getCareerList())
                .creator(entity.getCreator())
                .createTime(entity.getCreateTime())
                .updater(entity.getUpdater())
                .updateTime(entity.getUpdateTime())
                .modelImage(ModelImageMapperImpl.INSTANCE.toDtoList(entity.getCommonImageEntityList()))
                .build();
    }

    @Override
    public AdminModelEntity toEntity(AdminModelDTO dto) {

        if(dto == null) {
            return null;
        }

        return AdminModelEntity.builder()
                .rnum(dto.getRnum())
                .idx(dto.getIdx())
                .categoryCd(dto.getCategoryCd())
                .modelKorName(dto.getModelKorName())
                .modelEngName(dto.getModelEngName())
                .modelDescription(dto.getModelDescription())
                .modelFirstName(dto.getModelFirstName())
                .modelSecondName(dto.getModelSecondName())
                .modelKorFirstName(dto.getModelKorFirstName())
                .modelKorSecondName(dto.getModelKorSecondName())
                .modelMainYn(dto.getModelMainYn())
                .visible(dto.getVisible())
                .height(dto.getHeight())
                .shoes(dto.getShoes())
                .size3(dto.getSize3())
                .categoryAge(dto.getCategoryAge())
                .creator(dto.getCreator())
                .createTime(dto.getCreateTime())
                .updater(dto.getUpdater())
                .updateTime(dto.getUpdateTime())
                .build();
    }

    @Override
    public List<AdminModelDTO> toDtoList(List<AdminModelEntity> entityList) {

        if(entityList == null) {
            return null;
        }

        List<AdminModelDTO> list = new ArrayList<>(entityList.size());
        for(AdminModelEntity adminModelEntity : entityList) {
            list.add(toDto(adminModelEntity));
        }

        return list;
    }

    @Override
    public List<AdminModelEntity> toEntityList(List<AdminModelDTO> dtoList) {

        if(dtoList == null) {
            return null;
        }

        List<AdminModelEntity> list = new ArrayList<>(dtoList.size());
        for(AdminModelDTO adminModelDTO : dtoList) {
            list.add(toEntity(adminModelDTO));
        }

        return list;
    }
}
