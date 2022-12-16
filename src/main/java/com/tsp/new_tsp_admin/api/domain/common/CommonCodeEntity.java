package com.tsp.new_tsp_admin.api.domain.common;

import com.tsp.new_tsp_admin.api.domain.model.AdminModelDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tsp_cmm_code")
public class CommonCodeEntity {
    @Transient
    private Integer rowNum;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Long idx;

    @Column(name = "category_cd")
    private Integer categoryCd;

    @Column(name = "category_nm")
    private String categoryNm;

    @Column(name = "visible")
    private String visible;

    @Column(name = "cmm_type")
    private String cmmType;

    public static CommonCodeDTO toDto(CommonCodeEntity entity) {
        return CommonCodeDTO.builder().idx(entity.getIdx())
                .rowNum(entity.getRowNum())
                .categoryCd(entity.getCategoryCd())
                .categoryNm(entity.getCategoryNm())
                .cmmType(entity.getCmmType())
                .visible(entity.getVisible())
                .build();
    }

    public List<CommonCodeDTO> toDtoList(List<CommonCodeEntity> entityList) {
        List<CommonCodeDTO> list = new ArrayList<>(entityList.size());
        entityList.forEach(commonCodeEntity -> list.add(toDto(commonCodeEntity)));
        return list;
    }
}
