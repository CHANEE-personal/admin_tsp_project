package com.tsp.new_tsp_admin.api.domain.model;

import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tsp_model")
public class AdminModelEntity {

    @Transient
    private Integer rnum;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    private Integer idx;

    @Column(name = "category_cd")
    @NotNull(message = "모델 카테고리 선택은 필수입니다.")
    private Integer categoryCd;

    @Column(name = "category_age")
    @NotEmpty(message = "모델 연령대 선택은 필수입니다.")
    private String categoryAge;

    @Column(name = "model_kor_name")
    @NotEmpty(message = "모델 국문 이름 입력은 필수입니다.")
    private String modelKorName;

    @Column(name = "model_eng_name")
    @NotEmpty(message = "모델 영문 이름 입력은 필수입니다.")
    private String modelEngName;

    @Column(name = "height")
    @NotEmpty(message = "모델 키 입력은 필수입니다.")
    private String height;

    @Column(name = "size3")
    @NotEmpty(message = "모델 사이즈 입력은 필수입니다.")
    private String size3;

    @Column(name = "shoes")
    @NotEmpty(message = "모델 발 사이즈 입력은 필수입니다.")
    private String shoes;

    @Column(name = "model_description")
    @Lob
    @NotEmpty(message = "모델 상세 내용 입력은 필수입니다.")
    private String modelDescription;

    @NotEmpty
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

    @OneToMany(mappedBy = "adminModelEntity")
    private List<CommonImageEntity> commonImageEntityList = new ArrayList<>();
}
