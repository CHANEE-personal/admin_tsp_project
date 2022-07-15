package com.tsp.new_tsp_admin.api.user.mapper;

import com.tsp.new_tsp_admin.api.domain.user.AdminUserDTO;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity;
import com.tsp.new_tsp_admin.common.StructMapper;

import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

public interface UserMapper extends StructMapper<AdminUserDTO, AdminUserEntity> {
    UserMapper INSTANCE = getMapper(UserMapper.class);

    @Override
    AdminUserDTO toDto(AdminUserEntity entity);

    @Override
    AdminUserEntity toEntity(AdminUserDTO dto);

    @Override
    List<AdminUserDTO> toDtoList(List<AdminUserEntity> entityList);

    @Override
    List<AdminUserEntity> toEntityList(List<AdminUserDTO> dtoList);
}
