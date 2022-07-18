package com.tsp.new_tsp_admin.api.domain.support;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tsp.new_tsp_admin.api.domain.common.NewCommonDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class AdminSupportDTO extends NewCommonDTO {
    @ApiModelProperty(required = true, value = "rnum", hidden = true)
    Integer rnum;

    @ApiModelProperty(value = "지원모델 IDX", required = true, hidden = true)
    private Integer idx;

    @NotEmpty(message = "지원모델 이름 입력은 필수입니다.")
    @ApiModelProperty(value = "지원모델 이름", required = true)
    private String supportName;

    @NotEmpty(message = "지원모델 키 입력은 필수입니다.")
    @ApiModelProperty(value = "지원모델 Height", required = true)
    private Integer supportHeight;

    @NotEmpty(message = "지원모델 사이즈 입력은 필수입니다.")
    @ApiModelProperty(value = "지원모델 3Size", required = true)
    private String supportSize3;

    @ApiModelProperty(value = "지원모델 instagram")
    private String supportInstagram;

    @NotEmpty(message = "지원모델 휴대폰번호 입력은 필수입니다.")
    @ApiModelProperty(value = "지원모델 휴대폰번호", required = true)
    private String supportPhone;

    @NotEmpty(message = "지원모델 상세 내용 입력은 필수입니다.")
    @ApiModelProperty(value = "지원모델 내용", required = true)
    private String supportMessage;

    @NotEmpty(message = "지원모델 노출 여부 선택은 필수입니다.")
    @ApiModelProperty(value = "노출여부", required = true)
    private String visible;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @ApiModelProperty(value = "지원 시각", required = true)
    private Date supportTime;

}
