package com.tsp.new_tsp_admin.api.domain.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tsp.new_tsp_admin.api.common.EntityType;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import com.tsp.new_tsp_admin.api.domain.model.agency.AdminAgencyEntity;
import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioEntity;
import com.tsp.new_tsp_admin.api.domain.production.AdminProductionEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tsp_image")
public class CommonImageEntity {
    @Id
    @GeneratedValue
    @Column(name = "idx")
    @ApiModelProperty(value = "파일 IDX", required = true, hidden = true)
    private Long idx;

    @Column(name = "type_idx")
    @ApiModelProperty(value = "분야 IDX", required = true, hidden = true)
    private Long typeIdx;

    @Column(name = "type_name")
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "분야명", required = true, hidden = true)
    private EntityType typeName;

    @Column(name = "file_num")
    @ApiModelProperty(value = "파일 Number", required = true, hidden = true)
    private Integer fileNum;

    @Column(name = "file_name")
    @ApiModelProperty(required = true, value = "파일명", hidden = true)
    private String fileName;

    @Column(name = "file_size")
    @ApiModelProperty(value = "파일SIZE", hidden = true)
    private Long fileSize;

    @Column(name = "file_mask")
    @ApiModelProperty(value = "파일MASK", hidden = true)
    private String fileMask;

    @Column(name = "file_path")
    @ApiModelProperty(value = "파일경로", hidden = true)
    private String filePath;

    @Column(name = "image_type")
    @ApiModelProperty(value = "메인 이미지 구분", hidden = true)
    private String imageType;

    @Column(name = "visible")
    @ApiModelProperty(value = "사용 여부", hidden = true)
    private String visible;

    @Column(name = "reg_date", insertable = false, updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @ApiModelProperty(value = "등록일자", hidden = true)
    private LocalDateTime regDate;

    @ManyToOne(fetch = LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "type_idx", referencedColumnName = "idx", insertable = false, updatable = false)
    private AdminModelEntity adminModelEntity;

    @ManyToOne(fetch = LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "type_idx", referencedColumnName = "idx", insertable = false, updatable = false)
    private AdminProductionEntity adminProductionEntity;

    @ManyToOne(fetch = LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "type_idx", referencedColumnName = "idx", insertable = false, updatable = false)
    private AdminPortFolioEntity adminPortfolioEntity;

    @ManyToOne(fetch = LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "type_idx", referencedColumnName = "idx", insertable = false, updatable = false)
    private AdminAgencyEntity adminAgencyEntity;

    public static CommonImageDTO toDto(CommonImageEntity entity) {
        if (entity == null) return null;
        return CommonImageDTO.builder()
                .idx(entity.getIdx())
                .typeIdx(entity.getTypeIdx())
                .typeName(entity.getTypeName())
                .fileMask(entity.getFileMask())
                .fileSize(entity.getFileSize())
                .fileName(entity.getFileName())
                .fileNum(entity.getFileNum())
                .filePath(entity.getFilePath())
                .imageType(entity.getImageType())
                .visible(entity.getVisible())
                .regDate(entity.getRegDate())
                .build();
    }

    public static List<CommonImageDTO> toDtoList(List<CommonImageEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(CommonImageEntity::toDto)
                .collect(Collectors.toList());
    }
}
