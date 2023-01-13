package com.tsp.new_tsp_admin.api.portfolio.service;

import com.tsp.new_tsp_admin.api.domain.portfolio.AdminPortFolioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AdminPortfolioJpaRepository extends JpaRepository<AdminPortFolioEntity, Long> {
}
