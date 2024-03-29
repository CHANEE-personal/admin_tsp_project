package com.tsp.new_tsp_admin.api.domain.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tsp.new_tsp_admin.api.common.EntityType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class CommonImageDTO {
    @ApiModelProperty(value = "파일 IDX", required = true, hidden = true, example = "1")
    private Long idx;

    @ApiModelProperty(value = "분야 IDX", required = true, hidden = true, example = "1")
    private Long typeIdx;

    @ApiModelProperty(value = "분야명", required = true, hidden = true)
    private EntityType typeName;

    @ApiModelProperty(value = "파일 Number", required = true, hidden = true, example = "1")
    private Integer fileNum;

    @ApiModelProperty(required = true, value = "파일명", hidden = true)
    private String fileName;

    @ApiModelProperty(value = "파일SIZE", hidden = true)
    private Long fileSize;

    @ApiModelProperty(value = "파일MASK", hidden = true)
    private String fileMask;

    @ApiModelProperty(value = "파일경로", hidden = true)
    private String filePath;

    @ApiModelProperty(value = "메인 이미지 구분", hidden = true)
    private String imageType;

    @ApiModelProperty(value = "이미지 사용 여부", hidden = true)
    private String visible;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @ApiModelProperty(value = "등록일자", hidden = true)
    private LocalDateTime regDate;
}
