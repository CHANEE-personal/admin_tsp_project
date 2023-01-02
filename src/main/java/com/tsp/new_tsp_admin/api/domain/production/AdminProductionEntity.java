package com.tsp.new_tsp_admin.api.domain.production;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentEntity;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import com.tsp.new_tsp_admin.api.domain.common.NewCommonMappedClass;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.GenerationType.*;

@Entity
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tsp_production")
public class AdminProductionEntity extends NewCommonMappedClass {
    @Transient
    private Integer rowNum;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "title")
    @NotEmpty(message = "프로덕션 제목 입력은 필수입니다.")
    private String title;

    @Column(name = "description")
    @Lob
    @NotEmpty(message = "프로덕션 상세내용 입력은 필수입니다.")
    private String description;

    @Column(name = "view_count")
    private Integer viewCount;

    @Column(name = "visible")
    @NotEmpty(message = "프로덕션 노출 여부 선택은 필수입니다.")
    private String visible;

    @JsonIgnore
    @BatchSize(size = 5)
    @Where(clause = "type_name = 'production'")
    @OneToMany(mappedBy = "adminProductionEntity")
    private List<CommonImageEntity> commonImageEntityList = new ArrayList<>();

    @JsonIgnore
    @BatchSize(size = 20)
    @Where(clause = "comment_type = 'production'")
    @OneToMany(mappedBy = "adminProductionEntity")
    private List<AdminCommentEntity> commentList = new ArrayList<>();

    public static AdminProductionDTO toDto(AdminProductionEntity entity) {
        if (entity == null) return null;
        return AdminProductionDTO.builder()
                .rowNum(entity.getRowNum())
                .idx(entity.getIdx())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .viewCount(entity.getViewCount())
                .visible(entity.getVisible())
                .creator(entity.getCreator())
                .createTime(entity.getCreateTime())
                .updater(entity.getUpdater())
                .updateTime(entity.getUpdateTime())
                .productionImage(CommonImageEntity.toDtoList(entity.getCommonImageEntityList()))
                .build();
    }

    public static List<AdminProductionDTO> toDtoList(List<AdminProductionEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(AdminProductionEntity::toDto)
                .collect(Collectors.toList());
    }
}
