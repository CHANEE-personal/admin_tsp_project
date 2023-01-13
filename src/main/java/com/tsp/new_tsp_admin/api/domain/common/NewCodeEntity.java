package com.tsp.new_tsp_admin.api.domain.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.*;

@Getter
@Setter
@Entity
@SuperBuilder
@EqualsAndHashCode(of = "idx", callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tsp_cmm_code")
public class NewCodeEntity extends NewCommonMappedClass {

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

    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "newModelCodeJpaDTO", cascade = MERGE, fetch = LAZY)
    private List<AdminModelEntity> adminModelEntityList = new ArrayList<>();

    @Builder.Default
    @JsonIgnore
    @OneToMany(mappedBy = "newPortFolioJpaDTO", cascade = MERGE, fetch = LAZY)
    private List<AdminPortFolioEntity> adminPortFolioEntityList = new ArrayList<>();

    public void addModel(AdminModelEntity adminModelEntity) {
        adminModelEntity.setNewModelCodeJpaDTO(this);
        this.adminModelEntityList.add(adminModelEntity);
    }

    public void addPortfolio(AdminPortFolioEntity adminPortFolioEntity) {
        adminPortFolioEntity.setNewPortFolioJpaDTO(this);
        this.adminPortFolioEntityList.add(adminPortFolioEntity);
    }

    public static NewCodeDTO toDto(NewCodeEntity entity) {
        if (entity == null) return null;
        return NewCodeDTO.builder().idx(entity.getIdx())
                .rowNum(entity.getRowNum())
                .categoryCd(entity.getCategoryCd())
                .categoryNm(entity.getCategoryNm())
                .cmmType(entity.getCmmType())
                .visible(entity.getVisible())
                .build();
    }

    public static List<NewCodeDTO> toDtoList(List<NewCodeEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .map(NewCodeEntity::toDto)
                .collect(Collectors.toList());
    }
}
