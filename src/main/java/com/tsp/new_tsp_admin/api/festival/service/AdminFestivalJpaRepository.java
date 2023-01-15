package com.tsp.new_tsp_admin.api.festival.service;

import com.tsp.new_tsp_admin.api.domain.festival.AdminFestivalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminFestivalJpaRepository extends JpaRepository<AdminFestivalEntity, Long> {
}
