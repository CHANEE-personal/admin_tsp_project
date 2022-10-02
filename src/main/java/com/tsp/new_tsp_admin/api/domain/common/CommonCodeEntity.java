package com.tsp.new_tsp_admin.api.domain.common;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

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
    private Integer rnum;

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
