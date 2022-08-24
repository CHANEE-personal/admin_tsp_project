package com.tsp.new_tsp_admin.api.domain.comment;

import com.tsp.new_tsp_admin.api.domain.common.NewCommonDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "코멘트 관련 변수")
public class AdminCommentDTO extends NewCommonDTO {
    @ApiModelProperty(required = true, value = "rnum", hidden = true)
    private Integer rnum;

    @ApiModelProperty(required = true, value = "idx", hidden = true)
    private Integer idx;

    @NotEmpty(message = "코멘트 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "comment")
    private String comment;

    @ApiModelProperty(required = true, value = "commentTypeIdx")
    private Integer commentTypeIdx;

    @ApiModelProperty(required = true, value = "commentType")
    private String commentType;

    @NotEmpty(message = "코멘트 노출 여부 선택은 필수입니다.")
    @ApiModelProperty(required = true, value = "visible")
    private String visible;
}
