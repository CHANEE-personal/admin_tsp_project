package com.tsp.new_tsp_admin.api.domain.festival;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tsp.new_tsp_admin.api.domain.common.NewCommonDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class AdminFestivalDTO extends NewCommonDTO {

    @ApiModelProperty(required = true, value = "rowNum", hidden = true, example = "1")
    private Integer rowNum;

    @ApiModelProperty(required = true, value = "idx", hidden = true, example = "1")
    private Long idx;

    @NotEmpty(message = "행사 제목은 필수입니다.")
    @ApiModelProperty(required = true, value = "행사 제목((ex)이 축제는...)")
    private String festivalTitle;

    @NotEmpty(message = "행사 상세 내용 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "행사 상세 내용((ex)이 축제는...)")
    private String festivalDescription;

    @NotNull(message = "행사가 열리는 월 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "festival month", hidden = true, example = "1")
    private Integer festivalMonth;

    @NotNull(message = "행사가 열리는 일 입력은 필수입니다.")
    @ApiModelProperty(required = true, value = "festival day", hidden = true, example = "1")
    private Integer festivalDay;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @ApiModelProperty(required = true, value = "행사 일자", hidden = true)
    private LocalDateTime festivalTime;
}
