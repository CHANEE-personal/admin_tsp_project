package com.tsp.new_tsp_admin.api.domain.notice;

import com.tsp.new_tsp_admin.api.domain.common.NewCommonMappedClass;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicUpdate
@Table(name = "tsp_notice")
public class AdminNoticeEntity extends NewCommonMappedClass {
    @Transient
    private Integer rowNum;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "title")
    @NotEmpty(message = "제목 입력은 필수입니다.")
    private String title;

    @Column(name = "description")
    @Lob
    @NotEmpty(message = "공지사항 내용 입력은 필수입니다.")
    private String description;

    @Column(name = "view_count")
    private Integer viewCount;

    @Column(name = "visible")
    @NotEmpty(message = "공지사항 노출 여부 선택은 필수입니다.")
    private String visible;

    @Column(name = "top_fixed")
    private Boolean topFixed;

    public void update(AdminNoticeEntity adminNoticeEntity) {
        this.title = adminNoticeEntity.title;
        this.description = adminNoticeEntity.description;
        this.visible = adminNoticeEntity.visible;
        this.topFixed = adminNoticeEntity.topFixed;
    }

    public void toggleTopFixed(Boolean topFixed) {
        this.topFixed = !topFixed;
    }

    public static AdminNoticeDTO toDto(AdminNoticeEntity entity) {
        if (entity == null) return null;
        return AdminNoticeDTO.builder()
                .idx(entity.getIdx())
                .rowNum(entity.getRowNum())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .viewCount(entity.getViewCount())
                .topFixed(entity.getTopFixed())
                .visible(entity.getVisible())
                .creator(entity.getCreator())
                .createTime(entity.getCreateTime())
                .updater(entity.getUpdater())
                .updateTime(entity.getUpdateTime())
                .build();
    }

    public static List<AdminNoticeDTO> toDtoList(List<AdminNoticeEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(AdminNoticeEntity::toDto)
                .collect(Collectors.toList());
    }
}
