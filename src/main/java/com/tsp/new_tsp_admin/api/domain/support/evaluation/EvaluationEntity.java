package com.tsp.new_tsp_admin.api.domain.support.evaluation;

import com.tsp.new_tsp_admin.api.domain.common.NewCommonMappedClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import static javax.persistence.GenerationType.*;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tsp_evaluation")
public class EvaluationEntity extends NewCommonMappedClass {
    @Transient
    private Integer rnum;

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "idx")
    private Integer idx;

    @Column(name = "support_idx")
    @NotEmpty(message = "지원모델 idx는 필수입니다.")
    private Integer support_idx;

    @Column(name = "visible")
    @NotEmpty(message = "지원모델 노출 여부 선택은 필수입니다.")
    private String visible;

    @Column(name = "evaluate_comment")
    @Lob
    @NotEmpty(message = "지원모델 평가 내용 입력은 필수입니다.")
    private String evaluate_comment;
}
