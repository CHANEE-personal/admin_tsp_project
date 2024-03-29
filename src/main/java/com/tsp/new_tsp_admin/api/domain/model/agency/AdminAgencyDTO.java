package com.tsp.new_tsp_admin.api.domain.model.agency;

import com.tsp.new_tsp_admin.api.domain.common.CommonImageDTO;
import com.tsp.new_tsp_admin.api.domain.common.NewCommonDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "소속사 관련 변수")
public class AdminAgencyDTO extends NewCommonDTO {
    @ApiModelProperty(required = true, value = "rowNum", hidden = true, example = "1")
    private Integer rowNum;

    @ApiModelProperty(required = true, value = "idx", hidden = true, example = "1")
    private Long idx;

    @NotEmpty(message = "소속사명 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "agencyName")
    private String agencyName;

    @NotEmpty(message = "소속사 내용 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "agencyDescription")
    private String agencyDescription;

    @ApiModelProperty(value = "소속사 좋아요 수((ex)0)", example = "1")
    private Integer favoriteCount;

    @NotEmpty(message = "소속사 노출 여부 선택은 필수입니다.")
    @ApiModelProperty(required = true, value = "visible")
    private String visible;

    @ApiModelProperty(required = true, value = "agencyImageList", hidden = true)
    private List<CommonImageDTO> agencyImage = new ArrayList<>();
}
