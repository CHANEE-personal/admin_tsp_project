package com.tsp.new_tsp_admin.api.notice.service;

import com.tsp.new_tsp_admin.api.domain.notice.AdminNoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AdminNoticeJpaRepository extends JpaRepository<AdminNoticeEntity, Long> {
}
