package com.tsp.new_tsp_admin.api.domain.comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageDTO;
import com.tsp.new_tsp_admin.api.domain.common.NewCommonMappedClass;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioEntity;
import com.tsp.new_tsp_admin.api.domain.production.AdminProductionEntity;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tsp_admin_comment")
public class AdminCommentEntity extends NewCommonMappedClass {
    @Transient
    private Integer rowNum;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "comment")
    @Lob
    @NotEmpty(message = "코멘트 입력은 필수입니다.")
    private String comment;

    @Column(name = "comment_type_idx")
    @ApiModelProperty(value = "코멘트 타입 idx", required = true)
    private Long commentTypeIdx;

    @Column(name = "comment_type")
    @ApiModelProperty(value = "코멘트 타입", required = true)
    private String commentType;

    @Column(name = "visible")
    @NotEmpty(message = "FAQ 노출 여부 선택은 필수입니다.")
    private String visible;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "comment_type_idx", referencedColumnName = "idx", insertable = false, updatable = false)
    private AdminUserEntity adminUserEntity;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "comment_type_idx", referencedColumnName = "idx", insertable = false, updatable = false)
    private AdminModelEntity adminModelEntity;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "comment_type_idx", referencedColumnName = "idx", insertable = false, updatable = false)
    private AdminProductionEntity adminProductionEntity;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "comment_type_idx", referencedColumnName = "idx", insertable = false, updatable = false)
    private AdminPortFolioEntity adminPortfolioEntity;

    public static AdminCommentDTO toDto(AdminCommentEntity entity) {
        if (entity == null) return null;
        return AdminCommentDTO.builder()
                .idx(entity.getIdx())
                .rowNum(entity.getRowNum())
                .comment(entity.getComment())
                .commentTypeIdx(entity.getCommentTypeIdx())
                .commentType(entity.getCommentType())
                .visible(entity.getVisible())
                .creator(entity.getCreator())
                .createTime(entity.getCreateTime())
                .updater(entity.getUpdater())
                .updateTime(entity.getUpdateTime())
                .build();
    }

    public static List<AdminCommentDTO> toDtoList(List<AdminCommentEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(AdminCommentEntity::toDto)
                .collect(Collectors.toList());
    }
}
