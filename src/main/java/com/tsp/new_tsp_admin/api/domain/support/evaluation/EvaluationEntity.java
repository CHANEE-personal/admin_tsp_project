package com.tsp.new_tsp_admin.api.domain.support.evaluation;

import com.tsp.new_tsp_admin.api.domain.common.NewCommonMappedClass;
import com.tsp.new_tsp_admin.api.domain.support.AdminSupportEntity;
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

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.*;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tsp_evaluate")
public class EvaluationEntity extends NewCommonMappedClass {
    @Transient
    private Integer rnum;

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Integer idx;

    @Column(name = "support_idx")
    @NotNull(message = "지원모델 idx는 필수입니다.")
    private Integer supportIdx;

    @Column(name = "visible")
    @NotEmpty(message = "지원모델 노출 여부 선택은 필수입니다.")
    private String visible;

    @Column(name = "evaluate_comment")
    @Lob
    @NotEmpty(message = "지원모델 평가 내용 입력은 필수입니다.")
    private String evaluateComment;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "support_idx", referencedColumnName = "idx", insertable = false, updatable = false)
    private AdminSupportEntity adminSupportEntity;
}
