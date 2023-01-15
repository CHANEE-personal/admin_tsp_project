package com.tsp.new_tsp_admin.api.model.service.agency;

import com.tsp.new_tsp_admin.api.domain.model.agency.AdminAgencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminAgencyJpaRepository extends JpaRepository<AdminAgencyEntity, Long> {
}
