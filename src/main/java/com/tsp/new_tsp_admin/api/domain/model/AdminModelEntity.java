package com.tsp.new_tsp_admin.api.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tsp.new_tsp_admin.api.domain.comment.AdminCommentEntity;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import com.tsp.new_tsp_admin.api.domain.common.NewCodeEntity;
import com.tsp.new_tsp_admin.api.domain.common.NewCommonMappedClass;
import com.tsp.new_tsp_admin.api.domain.model.agency.AdminAgencyEntity;
import com.tsp.new_tsp_admin.api.domain.model.negotiation.AdminNegotiationEntity;
import com.tsp.new_tsp_admin.api.domain.model.schedule.AdminScheduleEntity;
import com.tsp.new_tsp_admin.common.CustomConverter;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tsp_model")
public class AdminModelEntity extends NewCommonMappedClass {
    @Transient
    private Integer rowNum;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "category_cd")
    @Range(min = 1, max = 3, message = "모델 categoryCd는 1~3 사이 값만 입력할 수 있습니다.")
    @NotNull(message = "모델 카테고리 선택은 필수입니다.")
    private Integer categoryCd;

    @Column(name = "category_age")
    @Range(min = 2, max = 6, message = "모델 연령대 값은 2~6 사이 값만 입력할 수 있습니다.")
    @NotNull(message = "모델 연령대 선택은 필수입니다.")
    private Integer categoryAge;

    @Column(name = "model_kor_name")
    @NotEmpty(message = "모델 국문 이름 입력은 필수입니다.")
    private String modelKorName;

    @Column(name = "model_eng_name")
    @NotEmpty(message = "모델 영문 이름 입력은 필수입니다.")
    private String modelEngName;

    @Column(name = "height")
    @NotNull(message = "모델 키 입력은 필수입니다.")
    private Integer height;

    @Column(name = "size3")
    @NotEmpty(message = "모델 사이즈 입력은 필수입니다.")
    private String size3;

    @Column(name = "shoes")
    @NotNull(message = "모델 발 사이즈 입력은 필수입니다.")
    private Integer shoes;

    @Column(name = "model_description")
    @Lob
    @NotEmpty(message = "모델 상세 내용 입력은 필수입니다.")
    private String modelDescription;

    @NotEmpty(message = "모델 노출 여부 선택은 필수입니다.")
    @Column(name = "visible")
    private String visible;

    @Column(name = "model_main_yn")
    @NotEmpty(message = "모델 메인 전시 여부는 필수입니다.")
    private String modelMainYn;

    @Column(name = "model_first_name")
    @NotEmpty(message = "모델 첫번째 이름 입력은 필수입니다.")
    private String modelFirstName;

    @Column(name = "model_second_name")
    @NotEmpty(message = "모델 두번째 이름 입력은 필수입니다.")
    private String modelSecondName;

    @Column(name = "model_kor_first_name")
    @NotEmpty(message = "모델 국문 첫번째 이름 입력은 필수입니다.")
    private String modelKorFirstName;

    @Column(name = "model_kor_second_name")
    @NotEmpty(message = "모델 국문 두번째 이름 입력은 필수입니다.")
    private String modelKorSecondName;

    @Column(name = "favorite_count")
    private Integer favoriteCount;

    @Column(name = "view_count")
    private Integer viewCount;

    @Column(name = "agency_idx")
    private Long agencyIdx;

    @Column(name = "career_list")
    @Convert(converter = CustomConverter.class)
    private ArrayList<CareerJson> careerList;

    @Column(name = "status")
    @NotEmpty(message = "모델 상태값 선택은 필수입니다.")
    private String status;

    @Column(name = "new_yn")
    @NotEmpty(message = "새로운 모델 선택은 필수입니다.")
    private String newYn;

    @JsonIgnore
    @Where(clause = "cmm_type = 'model'")
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_cd", insertable = false, updatable = false)
    private NewCodeEntity newModelCodeJpaDTO;

    @JsonIgnore
    @BatchSize(size = 5)
    @Where(clause = "type_name = 'model'")
    @OneToMany(mappedBy = "adminModelEntity", fetch = LAZY, cascade = REMOVE)
    private List<CommonImageEntity> commonImageEntityList = new ArrayList<>();

    @JsonIgnore
    @OneToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name = "agency_idx", referencedColumnName = "idx", insertable = false, updatable = false)
    private AdminAgencyEntity adminAgencyEntity;

    @JsonIgnore
    @BatchSize(size = 20)
    @OneToMany(mappedBy = "adminModelEntity", fetch = LAZY, cascade = REMOVE)
    private List<AdminScheduleEntity> scheduleList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "adminModelEntity", fetch = LAZY, cascade = REMOVE)
    private List<AdminNegotiationEntity> negotiationList = new ArrayList<>();

    @JsonIgnore
    @BatchSize(size = 20)
    @Where(clause = "comment_type = 'model'")
    @OneToMany(mappedBy = "adminModelEntity")
    private List<AdminCommentEntity> commentList = new ArrayList<>();

    public static AdminModelDTO toDto(AdminModelEntity entity) {
        if (entity == null) return null;
        return AdminModelDTO.builder().idx(entity.getIdx())
                .rowNum(entity.getRowNum())
                .categoryCd(entity.getCategoryCd())
                .modelKorName(entity.getModelKorName())
                .modelEngName(entity.getModelEngName())
                .modelDescription(entity.getModelDescription())
                .visible(entity.getVisible())
                .height(entity.getHeight())
                .shoes(entity.getShoes())
                .size3(entity.getSize3())
                .categoryAge(entity.getCategoryAge())
                .modelMainYn(entity.getModelMainYn())
                .modelFirstName(entity.getModelFirstName())
                .modelSecondName(entity.getModelSecondName())
                .modelKorFirstName(entity.getModelKorFirstName())
                .modelKorSecondName(entity.getModelKorSecondName())
                .favoriteCount(entity.getFavoriteCount())
                .viewCount(entity.getViewCount())
                .agencyIdx(entity.getAgencyIdx())
                .careerList(entity.getCareerList())
                .status(entity.getStatus())
                .newYn(entity.getNewYn())
                .creator(entity.getCreator())
                .createTime(entity.getCreateTime())
                .updater(entity.getUpdater())
                .updateTime(entity.getUpdateTime())
                .modelImage(CommonImageEntity.toDtoList(entity.getCommonImageEntityList()))
                .build();
    }

    public static List<AdminModelDTO> toDtoList(List<AdminModelEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(AdminModelEntity::toDto)
                .collect(Collectors.toList());
    }
}
