package com.tsp.new_tsp_admin.api.domain.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tsp_cmm_code")
public class CommonCodeEntity {
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
}
