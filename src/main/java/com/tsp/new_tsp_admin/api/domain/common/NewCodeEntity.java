package com.tsp.new_tsp_admin.api.domain.common;

import com.tsp.new_tsp_admin.api.domain.model.AdminModelEntity;
import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@Table(name = "tsp_cmm_code")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class NewCodeEntity extends NewCommonMappedClass {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    Integer idx;

    @Column(name = "category_cd")
    Integer categoryCd;

    @Column(name = "category_nm")
    String categoryNm;

    @Column(name = "visible")
    String visible;

    @Column(name = "cmm_type")
    String cmmType;

    @OneToMany(mappedBy = "newModelCodeJpaDTO", cascade = MERGE, fetch = LAZY)
    private List<AdminModelEntity> adminModelEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "newPortFolioJpaDTO", cascade = MERGE, fetch = LAZY)
    private List<AdminPortFolioEntity> adminPortFolioEntityList = new ArrayList<>();

}
