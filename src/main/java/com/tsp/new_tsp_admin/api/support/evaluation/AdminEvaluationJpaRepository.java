package com.tsp.new_tsp_admin.api.support.evaluation;

import com.tsp.new_tsp_admin.api.domain.support.evaluation.EvaluationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AdminEvaluationJpaRepository extends JpaRepository<EvaluationEntity, Long> {
}
