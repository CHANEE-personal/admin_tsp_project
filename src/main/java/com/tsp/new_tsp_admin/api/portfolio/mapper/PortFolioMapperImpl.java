package com.tsp.new_tsp_admin.api.portfolio.mapper;

import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioDTO;
import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioEntity;

import java.util.ArrayList;
import java.util.List;


public class PortFolioMapperImpl implements PortFolioMapper {
	@Override
	public AdminPortFolioDTO toDto(AdminPortFolioEntity entity) {
		if (entity == null) {
			return null;
		}

		return AdminPortFolioDTO.builder()
				.rnum(entity.getRnum())
				.idx(entity.getIdx())
				.categoryCd(entity.getCategoryCd())
				.title(entity.getTitle())
				.description(entity.getDescription())
				.hashTag(entity.getHashTag())
				.videoUrl(entity.getVideoUrl())
				.visible(entity.getVisible())
				.creator(entity.getCreator())
				.createTime(entity.getCreateTime())
				.updater(entity.getUpdater())
				.updateTime(entity.getUpdateTime())
				.portfolioImage(PortfolioImageMapper.INSTANCE.toDtoList(entity.getCommonImageEntityList()))
				.build();
	}

	@Override
	public AdminPortFolioEntity toEntity(AdminPortFolioDTO dto) {

		if(dto == null) {
			return null;
		}

		return AdminPortFolioEntity.builder()
				.rnum(dto.getRnum())
				.idx(dto.getIdx())
				.categoryCd(dto.getCategoryCd())
				.title(dto.getTitle())
				.description(dto.getDescription())
				.hashTag(dto.getHashTag())
				.videoUrl(dto.getVideoUrl())
				.visible(dto.getVisible())
				.creator(dto.getCreator())
				.createTime(dto.getCreateTime())
				.updater(dto.getUpdater())
				.updateTime(dto.getUpdateTime())
				.build();
	}

	@Override
	public List<AdminPortFolioDTO> toDtoList(List<AdminPortFolioEntity> entityList) {
		if(entityList == null) return null;

		List<AdminPortFolioDTO> list = new ArrayList<>(entityList.size());
		for(AdminPortFolioEntity adminPortFolioEntity : entityList) {
			list.add(toDto(adminPortFolioEntity));
		}

		return list;
	}

	@Override
	public List<AdminPortFolioEntity> toEntityList(List<AdminPortFolioDTO> dtoList) {
		if(dtoList == null) return null;

		List<AdminPortFolioEntity> list = new ArrayList<>(dtoList.size());
		for(AdminPortFolioDTO adminPortFolioDTO : dtoList) {
			list.add(toEntity(adminPortFolioDTO));
		}

		return list;
	}
}
