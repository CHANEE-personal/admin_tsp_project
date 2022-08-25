package com.tsp.new_tsp_admin.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel
public class Page {
    @ApiModelProperty(value = "페이지 번호(0..N)", example = "1")
    private Integer page;

    @ApiModelProperty(value = "페이지 크기", allowableValues = "range[0,100]", example = "1")
    private Integer size;

    // 시작페이지
    @ApiModelProperty(value = "시작페이지", hidden = true, example = "1")
    public Integer getStartPage() {
        return (page - 1) * size;
    }
}
