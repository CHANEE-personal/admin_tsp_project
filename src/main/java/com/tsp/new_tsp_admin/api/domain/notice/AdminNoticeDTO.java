package com.tsp.new_tsp_admin.api.domain.notice;

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
@ApiModel(value = "공지사항 관련 변수")
public class AdminNoticeDTO extends NewCommonDTO {
    @ApiModelProperty(required = true, value = "rnum", hidden = true, example = "1")
    private Integer rnum;

    @ApiModelProperty(required = true, value = "idx", hidden = true, example = "1")
    private Integer idx;

    @NotEmpty(message = "제목 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "title")
    private String title;

    @NotEmpty(message = "공지사항 내용 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "description")
    private String description;

    @ApiModelProperty(required = true, value = "viewCount", example = "1")
    private Integer viewCount;

    @NotEmpty(message = "공지사항 노출 여부 선택은 필수입니다.")
    @ApiModelProperty(required = true, value = "visible")
    private String visible;

    @ApiModelProperty(value = "topFixed")
    private Boolean topFixed;
}
