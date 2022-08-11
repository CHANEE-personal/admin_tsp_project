package com.tsp.new_tsp_admin.api.support.mapper.evaluate;

import com.tsp.new_tsp_admin.api.domain.support.evaluation.EvaluationDTO;
import com.tsp.new_tsp_admin.api.domain.support.evaluation.EvaluationEntity;
import com.tsp.new_tsp_admin.common.StructMapper;

import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

public interface EvaluateMapper extends StructMapper<EvaluationDTO, EvaluationEntity> {
    EvaluateMapper INSTANCE = getMapper(EvaluateMapper.class);

    @Override
    EvaluationDTO toDto(EvaluationEntity entity);

    @Override
    EvaluationEntity toEntity(EvaluationDTO dto);

    @Override
    List<EvaluationDTO> toDtoList(List<EvaluationEntity> entityList);

    @Override
    List<EvaluationEntity> toEntityList(List<EvaluationDTO> dtoList);
}
