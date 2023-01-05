package com.tsp.new_tsp_admin.api.domain.model.recommend;

import com.tsp.new_tsp_admin.api.domain.common.NewCommonDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "추천 검색어 관련 변수")
public class AdminRecommendDTO extends NewCommonDTO {
    @ApiModelProperty(required = true, value = "rowNum", hidden = true, example = "1")
    private Integer rowNum;

    @ApiModelProperty(required = true, value = "idx", hidden = true, example = "1")
    private Long idx;

    @Type(type = "json")
    @Nullable
    @ApiModelProperty(value = "recommend keyword")
    private List<String> recommendKeyword = new ArrayList<>();
}
