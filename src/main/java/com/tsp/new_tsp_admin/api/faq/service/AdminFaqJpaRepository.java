package com.tsp.new_tsp_admin.api.faq.service;

import com.tsp.new_tsp_admin.api.domain.faq.AdminFaqEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AdminFaqJpaRepository extends JpaRepository<AdminFaqEntity, Long> {
}
