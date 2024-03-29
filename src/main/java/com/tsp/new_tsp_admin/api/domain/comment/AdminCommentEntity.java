package com.tsp.new_tsp_admin.api.domain.comment;

import com.tsp.new_tsp_admin.api.domain.common.NewCommonMappedClass;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
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

    @Column(name = "comment_type")
    private String commentType;

    @Column(name = "visible")
    @NotEmpty(message = "FAQ 노출 여부 선택은 필수입니다.")
    private String visible;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "comment_type_idx", nullable = false)
    private AdminModelEntity adminModelEntity;

    public void update(AdminCommentEntity adminCommentEntity) {
        this.comment = adminCommentEntity.comment;
        this.visible = adminCommentEntity.visible;
    }

    public static AdminCommentDTO toDto(AdminCommentEntity entity) {
        if (entity == null) return null;
        return AdminCommentDTO.builder()
                .idx(entity.getIdx())
                .rowNum(entity.getRowNum())
                .comment(entity.getComment())
                .commentType(entity.getCommentType())
                .adminModelDTO(AdminModelEntity.toDto(entity.getAdminModelEntity()))
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
