package com.tsp.new_tsp_admin.api.support.mapper.evaluate;

import com.tsp.new_tsp_admin.api.domain.support.evaluation.EvaluationDTO;
import com.tsp.new_tsp_admin.api.domain.support.evaluation.EvaluationEntity;

import java.util.ArrayList;
import java.util.List;

public class EvaluateMapperImpl implements EvaluateMapper {

    @Override
    public EvaluationDTO toDto(EvaluationEntity entity) {
        if (entity == null) return null;

        return EvaluationDTO.builder()
                .rnum(entity.getRnum())
                .idx(entity.getIdx())
                .supportIdx(entity.getSupportIdx())
                .evaluateComment(entity.getEvaluateComment())
                .visible(entity.getVisible())
                .build();
    }

    @Override
    public EvaluationEntity toEntity(EvaluationDTO dto) {
        if (dto == null) return null;

        return EvaluationEntity.builder()
                .rnum(dto.getRnum())
                .idx(dto.getIdx())
                .supportIdx(dto.getSupportIdx())
                .evaluateComment(dto.getEvaluateComment())
                .visible(dto.getVisible())
                .build();
    }

    @Override
    public List<EvaluationDTO> toDtoList(List<EvaluationEntity> entityList) {
        if (entityList == null) return null;

        List<EvaluationDTO> list = new ArrayList<>(entityList.size());
        for (EvaluationEntity evaluationEntity : entityList) {
            list.add(toDto(evaluationEntity));
        }

        return list;
    }

    @Override
    public List<EvaluationEntity> toEntityList(List<EvaluationDTO> dtoList) {
        if (dtoList == null) return null;

        List<EvaluationEntity> list = new ArrayList<>(dtoList.size());
        for (EvaluationDTO evaluationDTO : dtoList) {
            list.add(toEntity(evaluationDTO));
        }

        return list;
    }
}
