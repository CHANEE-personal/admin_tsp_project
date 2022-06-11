package com.tsp.new_tsp_admin.api.support.service;

import com.tsp.new_tsp_admin.api.domain.support.AdminSupportDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminSupportJpaServiceImpl implements AdminSupportJpaService {

    private final AdminSupportJpaRepository adminSupportJpaRepository;

    @Override
    public Long findSupportsCount(Map<String, Object> supportMap) throws Exception {
        return adminSupportJpaRepository.findSupportsCount(supportMap);
    }

    @Override
    public List<AdminSupportDTO> findSupportsList(Map<String, Object> supportMap) throws Exception {
        return adminSupportJpaRepository.findSupportsList(supportMap);
    }
}
