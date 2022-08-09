package com.tsp.new_tsp_admin.api.domain.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "공통코드 관련 변수")
public class CommonCodeDTO {

    @ApiModelProperty(required = true, value = "rnum", hidden = true)
    private Integer rnum;

    @ApiModelProperty(required = true, value = "idx", hidden = true)
    private Integer idx;

    @Range(min = 1, max = 3, message = "categoryCd는 1~3 사이 값만 입력할 수 있습니다.")
    @ApiModelProperty(position = 1, required = true, value = "남자,여자,시니어 모델 구분((ex)1,2,3)")
    private Integer categoryCd;

    @ApiModelProperty(required = true, value = "남자,여자,시니어 모델 구분((ex)1,2,3)")
    private String categoryNm;

    @ApiModelProperty(required = true, value = "노출 여부((ex)Y,N")
    @NotEmpty(message = "노출 여부 선택은 필수입니다.")
    private String visible;

    @ApiModelProperty(required = true, value = "공통 코드 구분")
    private String cmmType;
}
