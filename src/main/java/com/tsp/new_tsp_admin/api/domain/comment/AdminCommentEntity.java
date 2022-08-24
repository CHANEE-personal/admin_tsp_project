package com.tsp.new_tsp_admin.api.domain.comment;

import com.tsp.new_tsp_admin.api.domain.common.NewCommonMappedClass;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioEntity;
import com.tsp.new_tsp_admin.api.domain.production.AdminProductionEntity;
import com.tsp.new_tsp_admin.api.domain.user.AdminUserEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tsp_admin_comment")
public class AdminCommentEntity extends NewCommonMappedClass {
    @Transient
    private Integer rnum;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Integer idx;

    @Column(name = "comment")
    @Lob
    @NotEmpty(message = "코멘트 입력은 필수입니다.")
    private String comment;

    @Column(name = "comment_type_idx")
    @ApiModelProperty(value = "코멘트 타입 idx", required = true)
    private Integer commentTypeIdx;

    @Column(name = "comment_type")
    @ApiModelProperty(value = "코멘트 타입", required = true)
    private String commentType;

    @Column(name = "visible")
    @NotEmpty(message = "FAQ 노출 여부 선택은 필수입니다.")
    private String visible;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "comment_type_idx", referencedColumnName = "idx", insertable = false, updatable = false)
    private AdminUserEntity adminUserEntity;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "comment_type_idx", referencedColumnName = "idx", insertable = false, updatable = false)
    private AdminModelEntity adminModelEntity;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "comment_type_idx", referencedColumnName = "idx", insertable = false, updatable = false)
    private AdminProductionEntity adminProductionEntity;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "comment_type_idx", referencedColumnName = "idx", insertable = false, updatable = false)
    private AdminPortFolioEntity adminPortfolioEntity;
}
