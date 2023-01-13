package com.tsp.new_tsp_admin.api.model.service.negotiation;

import com.tsp.new_tsp_admin.api.domain.model.negotiation.AdminNegotiationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AdminNegotiationJpaRepository extends JpaRepository<AdminNegotiationEntity, Long> {
}
