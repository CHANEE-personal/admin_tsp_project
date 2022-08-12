package com.tsp.new_tsp_admin.api.domain.support.evaluation;

import com.tsp.new_tsp_admin.api.domain.common.CommonImageDTO;
import com.tsp.new_tsp_admin.api.domain.common.NewCommonDTO;
import com.tsp.new_tsp_admin.api.domain.support.AdminSupportDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "지원모델 평가 관련 변수")
public class EvaluationDTO extends NewCommonDTO {
    @ApiModelProperty(required = true, value = "rnum", hidden = true)
    private Integer rnum;

    @ApiModelProperty(required = true, value = "idx", hidden = true)
    private Integer idx;

    @ApiModelProperty(required = true, value = "support_idx")
    @NotNull(message = "지원모델 idx 입력은 필수입니다.")
    private Integer support_idx;

    @NotEmpty(message = "지원모델 평가 내용 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "지원모델 평가내용")
    private String evaluate_comment;

    @ApiModelProperty(required = true, value = "삭제 여부((ex)Y,N")
    @NotEmpty(message = "지원모델 평가 삭제 선택은 필수입니다.")
    private String visible;
}
