package com.tsp.new_tsp_admin.api.domain.portfolio;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import com.tsp.new_tsp_admin.api.domain.common.NewCodeEntity;
import com.tsp.new_tsp_admin.api.domain.common.NewCommonMappedClass;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
@Table(name = "tsp_portfolio")
public class AdminPortFolioEntity extends NewCommonMappedClass {
    @Transient
    private Integer rowNum;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "category_cd")
    @NotNull(message = "포트폴리오 카테고리 선택은 필수입니다.")
    private Integer categoryCd;

    @Column(name = "title")
    @NotEmpty(message = "포트폴리오 제목 입력은 필수입니다.")
    private String title;

    @Column(name = "description")
    @Lob
    @NotEmpty(message = "포트폴리오 상세내용 입력은 필수입니다.")
    private String description;

    @Column(name = "hash_tag")
    @NotEmpty(message = "포트폴리오 해시태그 입력은 필수입니다.")
    private String hashTag;

    @Column(name = "video_url")
    @NotEmpty(message = "포트폴리오 비디오URL 입력은 필수입니다.")
    private String videoUrl;

    @Column(name = "view_count")
    private Integer viewCount;

    @Column(name = "visible")
    @NotEmpty(message = "포트폴리오 노출 여부 선택은 필수입니다.")
    private String visible;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_cd", insertable = false, updatable = false)
    private NewCodeEntity newPortFolioJpaDTO;

    @JsonIgnore
    @BatchSize(size = 5)
    @Where(clause = "type_name = 'portfolio'")
    @OneToMany(mappedBy = "adminPortfolioEntity")
    private List<CommonImageEntity> commonImageEntityList = new ArrayList<>();

    public static AdminPortFolioDTO toDto(AdminPortFolioEntity entity) {
        if (entity == null) return null;
        return AdminPortFolioDTO.builder()
                .rowNum(entity.getRowNum())
                .idx(entity.getIdx())
                .categoryCd(entity.getCategoryCd())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .hashTag(entity.getHashTag())
                .videoUrl(entity.getVideoUrl())
                .viewCount(entity.getViewCount())
                .visible(entity.getVisible())
                .creator(entity.getCreator())
                .createTime(entity.getCreateTime())
                .updater(entity.getUpdater())
                .updateTime(entity.getUpdateTime())
                .portfolioImage(CommonImageEntity.toDtoList(entity.getCommonImageEntityList()))
                .build();
    }

    public static List<AdminPortFolioDTO> toDtoList(List<AdminPortFolioEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(AdminPortFolioEntity::toDto)
                .collect(Collectors.toList());
    }
}
