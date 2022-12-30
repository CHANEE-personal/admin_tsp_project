package com.tsp.new_tsp_admin.api.domain.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Integer idx;

    @Column(name = "category_cd")
    private Integer categoryCd;

    @Column(name = "category_nm")
    private String categoryNm;

    @Column(name = "visible")
    private String visible;

    @Column(name = "cmm_type")
    private String cmmType;

    @JsonIgnore
    @OneToMany(mappedBy = "newModelCodeJpaDTO", cascade = MERGE, fetch = LAZY)
    private List<AdminModelEntity> adminModelEntityList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "newPortFolioJpaDTO", cascade = MERGE, fetch = LAZY)
    private List<AdminPortFolioEntity> adminPortFolioEntityList = new ArrayList<>();

}
