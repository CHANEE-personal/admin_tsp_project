package com.tsp.new_tsp_admin.api.common.image;

import com.tsp.new_tsp_admin.api.common.EntityType;
import com.tsp.new_tsp_admin.api.domain.common.CommonImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminCommonImageJpaRepository extends JpaRepository<CommonImageEntity, Long> {
}
