package com.tsp.new_tsp_admin.api.model.mapper;

import com.tsp.new_tsp_admin.api.domain.model.AdminModelDTO;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import org.mapstruct.Mapper;

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
        return null;
    }

    @Override
    public List<AdminModelDTO> toDtoList(List<AdminModelEntity> entityList) {
        return null;
    }

    @Override
    public List<AdminModelEntity> toEntityList(List<AdminModelDTO> dtoList) {
        return null;
    }
}
