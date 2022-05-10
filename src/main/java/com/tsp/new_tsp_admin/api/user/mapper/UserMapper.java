package com.tsp.new_tsp_admin.api.user.mapper;

import com.tsp.new_tsp_admin.api.domain.user.AdminUserDTO;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity;
import com.tsp.new_tsp_admin.common.StructMapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

public interface UserMapper extends StructMapper<AdminUserDTO, AdminUserEntity> {

	UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

	@Override
	AdminUserDTO toDto(AdminUserEntity entity);

	@Override
	AdminUserEntity toEntity(AdminUserDTO dto);

	@Override
	List<AdminUserDTO> toDtoList(List<AdminUserEntity> entityList);

	@Override
	List<AdminUserEntity> toEntityList(List<AdminUserDTO> dtoList);
}
